package marketing.digital.rj.apiagendfy.Users.mapper;

import marketing.digital.rj.apiagendfy.Users.dto.usersDTO;
import marketing.digital.rj.apiagendfy.Users.model.Role;
import marketing.digital.rj.apiagendfy.Users.model.usersModel;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class usersMapper {

    // DTO -> Entity
    public usersModel map(usersDTO dto) {
        if (dto == null) return null;

        usersModel model = new usersModel();
        model.setUsername(dto.getUsername());
        model.setEmail(dto.getEmail());
        model.setPhone(dto.getPhone());
        model.setProfileUrl(dto.getProfileUrl());
        model.setPassword(dto.getPassword()); // será hasheada no service
        model.setDescription(dto.getDescription());
        model.setEnterprise(dto.getEnterprise());
        model.setEnabled(dto.isEnabled());

        // Converte Set<String> -> Set<Role>
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> roles = dto.getRoles().stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toUpperCase)
                    .map(Role::valueOf) // lança IllegalArgumentException se inválido
                    .collect(Collectors.toSet());
            model.setRoles(roles);
        }
        // Se vier vazio/null, o service decide o default (USER)

        return model;
    }

    // Entity -> DTO
    public usersDTO map(usersModel model) {
        if (model == null) return null;

        usersDTO dto = new usersDTO();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setEmail(model.getEmail());
        dto.setPhone(model.getPhone());
        dto.setProfileUrl(model.getProfileUrl());
        dto.setDescription(model.getDescription());
        dto.setEnabled(model.isEnabled());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdateAt(model.getUpdatedAt());
        dto.setLastLogin(model.getLastLogin());
        dto.setEnterprise(model.getEnterprise());

        // Converte Set<Role> -> Set<String>
        dto.setRoles(model.getRoles() == null ? Set.of()
                : model.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
        dto.setPassword(null);

        return dto;
    }
}
