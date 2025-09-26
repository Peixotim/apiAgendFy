package marketing.digital.rj.apiagendfy.Enterprise.mapper;
import marketing.digital.rj.apiagendfy.Enterprise.dto.enterpriseDTO;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.Users.model.usersModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class enterpriseMapper {

    public enterpriseModel map(enterpriseDTO dto){
        enterpriseModel model = new enterpriseModel();
        model.setId(dto.getId());
        model.setEmail(dto.getEmail());
        model.setCnpj(dto.getCnpj());
        model.setName(dto.getName());
        model.setActive(dto.isActive());
        model.setUsers(dto.getUsers());
        model.setCreatedAt(dto.getCreatedAt());
        model.setUpdatedAt(dto.getUpdatedAt());
        return model;
    }

    public enterpriseDTO map(enterpriseModel model){
        enterpriseDTO dto = new enterpriseDTO();
        dto.setActive(model.isActive());
        dto.setEmail(model.getEmail());
        dto.setUsers(model.getUsers());
        dto.setUpdatedAt(model.getUpdatedAt());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setId(model.getId());
        dto.setCnpj(model.getCnpj());
        dto.setName(model.getName());

        return dto;
    }
}
