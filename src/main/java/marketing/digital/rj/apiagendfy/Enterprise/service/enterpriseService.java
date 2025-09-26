package marketing.digital.rj.apiagendfy.Enterprise.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import marketing.digital.rj.apiagendfy.Enterprise.dto.enterpriseDTO;
import marketing.digital.rj.apiagendfy.Enterprise.mapper.enterpriseMapper;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.Enterprise.repository.enterpriseRepository;
import marketing.digital.rj.apiagendfy.infra.exception.AlreadyExistsException;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class enterpriseService {

    private final enterpriseRepository enterpriseRepository;
    private final enterpriseMapper mapper;

    public enterpriseService(enterpriseMapper mapper,enterpriseRepository enterpriseRepository){
        this.mapper = mapper;
        this.enterpriseRepository = enterpriseRepository;
    }

    public List<enterpriseDTO> listAll(){
        List<enterpriseModel> model = enterpriseRepository.findAll();
        return model
                .stream()
                .map(mapper ::map)
                .toList();
    }

    @Transactional
    public enterpriseDTO createEnterprise(@Valid enterpriseDTO dto) {
        // validações de unicidade (case-insensitive)
        if (enterpriseRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new AlreadyExistsException("E-mail de empresa já cadastrado.");
        }
        if (enterpriseRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new AlreadyExistsException("Nome de empresa já cadastrado.");
        }
        if(enterpriseRepository.existsBycnpjIgnoreCase(dto.getCnpj())){
            throw new AlreadyExistsException("Cnpj de empresa já cadastrado.");
        }

        var model = mapper.map(dto);
        var saved = enterpriseRepository.save(model);
        return mapper.map(saved);
    }
}
