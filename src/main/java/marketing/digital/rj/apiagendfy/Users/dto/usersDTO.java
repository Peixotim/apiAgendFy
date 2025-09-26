package marketing.digital.rj.apiagendfy.Users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class usersDTO {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String profileUrl; //Foto de perfil
    private String description;
    private Set<String> roles;
    private enterpriseModel enterprise;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private LocalDateTime lastLogin;

}
