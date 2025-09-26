package marketing.digital.rj.apiagendfy.Enterprise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketing.digital.rj.apiagendfy.Users.model.usersModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class enterpriseDTO {
    private UUID id;
    private String name;
    private String email;
    private String cnpj;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<usersModel> users;
    private boolean active;
}
