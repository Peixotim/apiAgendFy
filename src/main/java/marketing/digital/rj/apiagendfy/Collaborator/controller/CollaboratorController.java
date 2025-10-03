package marketing.digital.rj.apiagendfy.Collaborator.controller;

import jakarta.validation.Valid;
import marketing.digital.rj.apiagendfy.Collaborator.model.CreateCollaboratorDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;

import marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorModel;
import marketing.digital.rj.apiagendfy.Collaborator.model.Role;
import marketing.digital.rj.apiagendfy.Collaborator.repository.CollaboratorRepository;
import marketing.digital.rj.apiagendfy.Enterprise.repository.enterpriseRepository;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/collaborators")
@RequiredArgsConstructor
public class CollaboratorController {

    private final enterpriseRepository enterpriseRepo;
    private final CollaboratorRepository collabRepo;

    // DTO simples para cr

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public ResponseEntity<List<CollaboratorModel>> list(@RequestParam UUID enterpriseId) {
        var list = collabRepo.findByEnterprise_IdAndActiveTrueOrderByNameAsc(enterpriseId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public ResponseEntity<CollaboratorModel> create(@Valid @RequestBody CreateCollaboratorDTO dto) {
        var ent = enterpriseRepo.getReferenceById(dto.enterpriseId());
        var c = new CollaboratorModel();
        c.setName(dto.name());
        c.setEmail(dto.email());
        c.setPhone(dto.phone());
        c.setRoles(dto.role());
        c.setActive(true);
        c.setEnterprise(ent);
        return ResponseEntity.ok(collabRepo.save(c));
    }

    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public ResponseEntity<CollaboratorModel> toggle(@PathVariable UUID id) {
        var c = collabRepo.findById(id).orElseThrow();
        c.setActive(!c.isActive());
        return ResponseEntity.ok(collabRepo.save(c));
    }
}