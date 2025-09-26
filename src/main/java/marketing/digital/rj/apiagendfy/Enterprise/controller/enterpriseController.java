package marketing.digital.rj.apiagendfy.Enterprise.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import marketing.digital.rj.apiagendfy.Enterprise.dto.enterpriseDTO;
import marketing.digital.rj.apiagendfy.Enterprise.service.enterpriseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@RequestMapping("enterprise")
@RestController
public class enterpriseController {
    private final enterpriseService service;
    
    public enterpriseController(enterpriseService service){
        this.service = service;
    }

    @GetMapping
    public List<enterpriseDTO> enterprises(){
        return service.listAll();
    }


    @PostMapping("/cadastro")
    public ResponseEntity<enterpriseDTO> createEnterprise(
            @Valid @RequestBody enterpriseDTO dto,
            UriComponentsBuilder uriBuilder
    ) {
        var created = service.createEnterprise(dto);
        var uri = uriBuilder.path("/enterprise/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uri).body(created); // 201 Created + Location
    }
}
