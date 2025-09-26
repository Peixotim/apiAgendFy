package marketing.digital.rj.apiagendfy.infra.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marketing.digital.rj.apiagendfy.Users.model.usersModel;
import marketing.digital.rj.apiagendfy.Users.repository.usersRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;


@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final usersRepository usersRepository;

    public SecurityFilter(TokenService tokenService, usersRepository usersRepository){
        this.tokenService = tokenService;
        this.usersRepository = usersRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        // 1) Pule preflight e rotas públicas
        if ("OPTIONS".equalsIgnoreCase(method)
                || path.startsWith("/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2) Só tenta autenticar se houver Bearer
        String token = recoverToken(request);
        if (token != null) {
            String email = tokenService.validateToken(token); // retorne null se inválido/expirado
            if (email != null && !email.isBlank()) {
                usersRepository.findByEmail(email.toLowerCase()).ifPresent(user -> {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
                // Se não achar, NÃO lance exceção — segue anônimo
            }
        }

        chain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7).trim();
        return token.isEmpty() ? null : token;
    }
}
