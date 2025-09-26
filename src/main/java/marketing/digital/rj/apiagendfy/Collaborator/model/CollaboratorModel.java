package marketing.digital.rj.apiagendfy.Collaborator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;
import org.hibernate.annotations.GenericGenerator;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tb_collaborator")
@Entity(name = "tb_collaborator")
public class CollaboratorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", length = 120,nullable = false)
    private String name;

    @Column(name = "email",length = 120,unique = true)
    private String email;

    @Column(name = "phone" , unique = true, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Role roles;

    @Column(name = "active")
    private boolean active = true;


    @ManyToOne
    @JoinColumn(name = "enterpriseId")
    private enterpriseModel enterpriseId;

    @OneToMany(mappedBy = "collaboratorId")
    private List<CollaboratorAvailability> availabilityList;

    @OneToMany(mappedBy = "collaboratorId")
    private List<CollaboratorAvailabilityExceptions> exceptions;

    @OneToMany(mappedBy = "collaboratorId")
    private List<Appointment> appointments;
}
