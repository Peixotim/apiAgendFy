package marketing.digital.rj.apiagendfy.Enterprise.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorModel;
import marketing.digital.rj.apiagendfy.Users.model.usersModel;
import marketing.digital.rj.apiagendfy.scheduling.model.AvailabilityRule;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity(name = "enterprise")
@Table(name = "tb_enterprise")
public class enterpriseModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "cnpj" , nullable = false , unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "enterprise")
    private List<usersModel> users;

    @OneToMany(mappedBy="enterprise")
    private List<AvailabilityRule> availabilityRuleList;

    @Column(name = "isActived")
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "enterpriseId")
    List<CollaboratorModel> collaborators;

    @PrePersist
    public void prePersist() {
        final var now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.active == false) this.active = true; // default true
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
