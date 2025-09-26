package marketing.digital.rj.apiagendfy.Users.dto;
import java.time.LocalDateTime;
//Aqui vou pegar apenas oque preciso para retornar
public record UserProfileDTO (
        String username,
        String email,
        String profileUrl,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogin){}
