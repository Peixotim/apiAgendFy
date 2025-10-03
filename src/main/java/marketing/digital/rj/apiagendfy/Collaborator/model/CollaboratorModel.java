// marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorModel
package marketing.digital.rj.apiagendfy.Collaborator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_collaborator")
public class CollaboratorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", length = 120, nullable = false)
    private String name;

    @Column(name = "email", length = 120, unique = true)
    private String email;

    @Column(name = "phone", unique = true, length = 20)
    private String phone;

    @Column(name = "photoUrl",nullable = true)
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles",nullable = false)
    private Role roles;

    @Column(name = "active")
    private boolean active = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private enterpriseModel enterprise; // ✅ atributo é 'enterprise'

    // ✅ mappedBy deve ser o NOME DO ATRIBUTO na outra entidade
    @OneToMany(mappedBy = "collaborator")
    private List<CollaboratorAvailability> availabilityList;

    @OneToMany(mappedBy = "collaborator")
    private List<CollaboratorAvailabilityExceptions> exceptions;

    @OneToMany(mappedBy = "collaborator")
    private List<Appointment> appointments;
}