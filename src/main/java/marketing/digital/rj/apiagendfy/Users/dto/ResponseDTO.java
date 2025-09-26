package marketing.digital.rj.apiagendfy.Users.dto;
//Oque o front-end espera receber
import java.util.Set;
public record ResponseDTO (String name, String token, String tokenType, String profileUrl, Set<String> roles){}
