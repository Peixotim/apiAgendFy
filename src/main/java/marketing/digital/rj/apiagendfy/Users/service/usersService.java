package marketing.digital.rj.apiagendfy.Users.service;

import marketing.digital.rj.apiagendfy.Users.dto.LoginRequestDTO;
import marketing.digital.rj.apiagendfy.Users.dto.ResponseDTO;
import marketing.digital.rj.apiagendfy.Users.dto.UserProfileDTO;
import marketing.digital.rj.apiagendfy.Users.dto.usersDTO;
import marketing.digital.rj.apiagendfy.Users.mapper.usersMapper;
import marketing.digital.rj.apiagendfy.Users.model.Role;
import marketing.digital.rj.apiagendfy.Users.model.usersModel;
import marketing.digital.rj.apiagendfy.Users.repository.usersRepository;
import marketing.digital.rj.apiagendfy.infra.exception.NotFoundException;
import marketing.digital.rj.apiagendfy.infra.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class usersService {

    private final usersRepository usersRepository;
    private final usersMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public usersService(usersRepository repository, usersMapper mapper, PasswordEncoder encoder, TokenService tokenService) {
        this.usersRepository = repository;
        this.mapper = mapper;
        this.passwordEncoder = encoder;
        this.tokenService = tokenService;
    }

    public usersDTO createUser(usersDTO req) {
        usersRepository.findByEmail(req.getEmail())
                .ifPresent(u -> { throw new IllegalArgumentException("E-mail já cadastrado !"); });

        usersModel model = mapper.map(req);
        model.setEmail(req.getEmail().toLowerCase());

        if (model.getPassword() == null || model.getPassword().isBlank()) {
            throw new RuntimeException("Senha obrigatória.");
        }
        model.setPassword(passwordEncoder.encode(model.getPassword()));
        Set<String> dtoRoles = req.getRoles();
        if (dtoRoles != null && !dtoRoles.isEmpty()) {
            try {
                Set<Role> roles = dtoRoles.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(String::toUpperCase)
                        .map(Role::valueOf)          // lança IllegalArgumentException se for inválido
                        .collect(Collectors.toSet());
                if (roles.isEmpty()) roles = Set.of(Role.USER);
                model.setRoles(roles);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Role inválida no payload. Use: USER, COLLABORATOR ou ADMIN.");
            }
        } else {
            model.setRoles(Set.of(Role.USER));
        }

        // 5) timestamps e enabled
        if (model.getCreatedAt() == null) model.setCreatedAt(LocalDateTime.now());
        model.setUpdatedAt(LocalDateTime.now());
        if (!model.isEnabled()) model.setEnabled(true);

        // 6) salva
        model = usersRepository.save(model);

        // 7) monta DTO de saída
        usersDTO out = mapper.map(model);

        // garanta que o DTO exponha um array de roles (Set<String>)
        out.setRoles(model.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));

        // nunca devolva senha
        out.setPassword(null);

        return out;
    }

    public ResponseEntity<ResponseDTO> loginUser(LoginRequestDTO req) {
        var user = usersRepository.findByEmail(req.email().toLowerCase())
                .orElse(null);

        //Se nao existir o email a conta nao existe ou seja not found
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Se a senha nao for true ela retorna BAD_REQUEST ou seja o email existe mas a senha nao condiz com a conta
        if(!passwordEncoder.matches(req.password(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        //Agora se nem a senha e nem o email existe retorna 401
        if (user == null || !passwordEncoder.matches(req.password(), user.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        // Gera o token JWT
        String token = tokenService.generateToken(user);

        // Atualiza o Laslogin
        user.setLastLogin(LocalDateTime.now());
        usersRepository.save(user);

        //Aqui converte a role em string
        Set<String> roles = user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        var body = new ResponseDTO(
                user.getUsername(),
                token,
                "Bearer",
                user.getProfileUrl(),
                roles
        );

        return ResponseEntity.ok(body);
    }

    public UserProfileDTO userData(String email){
        //Filtra o usuario pelo email
        usersModel m = usersRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Found !"));

        //Retorna um Record com os dados essenciais (Sem a senha igual a dica do chat)
        return new UserProfileDTO(
                m.getUsername(),
                m.getEmail(),
                m.getProfileUrl(),
                m.getDescription(),
                m.getCreatedAt(),
                m.getUpdatedAt(),
                m.getLastLogin()
        );
    }
}
