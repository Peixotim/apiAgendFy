package marketing.digital.rj.apiagendfy.Users.controller;

import marketing.digital.rj.apiagendfy.Users.dto.LoginRequestDTO;
import marketing.digital.rj.apiagendfy.Users.dto.ResponseDTO;
import marketing.digital.rj.apiagendfy.Users.dto.UserProfileDTO;
import marketing.digital.rj.apiagendfy.Users.dto.usersDTO;
import marketing.digital.rj.apiagendfy.Users.service.usersService;
import marketing.digital.rj.apiagendfy.infra.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class usersController {

    private final usersService service;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public usersController(usersService service, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<usersDTO> createUser(@RequestBody usersDTO dto){
        usersDTO saved = service.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginRequestDTO user){
        return service.loginUser(user);
    }

    @GetMapping("/user/Profile")
    public ResponseEntity<UserProfileDTO> userData(@RequestParam String email){
        return ResponseEntity.ok(service.userData(email));
    }

}
