package marketing.digital.rj.apiagendfy.Collaborator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "tb_collaborator_availability_rule")
@Entity(name = "tb_collaborator_availability_rule")
@AllArgsConstructor
@NoArgsConstructor
public class CollaboratorAvailability {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "weekedy",nullable = false)
    private int weekedy;

    @Column(name = "start_time",nullable = false)
    private LocalDateTime start_time;

    @Column(name = "end_time",nullable = false)
    private LocalDateTime end_time;

    @Column(name = "internal_minutes",nullable = false)
    private int internal_minutes = 30;

    @ManyToOne
    @JoinColumn(name ="collaboratorId" , nullable = false)
    private CollaboratorModel collaboratorId;
}
