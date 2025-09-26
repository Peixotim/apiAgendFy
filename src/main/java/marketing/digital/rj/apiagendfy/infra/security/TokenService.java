package marketing.digital.rj.apiagendfy.infra.security;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import marketing.digital.rj.apiagendfy.Users.model.usersModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private Integer expire = 10; //Horas

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(usersModel users){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String[] roles = users.getRoles()
                    .stream()
                    .map(Enum::name)
                    .toArray(String[]:: new);

            String token = JWT.create()
                    .withIssuer("api-AgendFy")  //Quem esta gerando a api
                    .withSubject(users.getEmail()) //Dono do token (Ou seja aqui vamos saber quem gerou)
                    .withClaim("name",users.getUsername())
                    .withArrayClaim("role" , roles)
                    .withExpiresAt(this.getExpirationDate())
                    .sign(algorithm);

            return token;
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error while authenticating");
        }
        }

        public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("api-AgendFy")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException exception){
           return null;
        }
        }
    public Instant getExpirationDate(){
        return LocalDateTime.now().plusHours(expire).toInstant(ZoneOffset.of("-03:00")); //30 dias
    }
    }
