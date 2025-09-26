package marketing.digital.rj.apiagendfy.Collaborator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@Entity(name = "tb_collaborator_availability_exception")
@Table(name = "tb_collaborator_availability_exception")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaboratorAvailabilityExceptions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "collaboratorId" , nullable = false)
    private CollaboratorModel collaboratorId;

    @Column(name = "date")
    private Date date;

    @Column(name = "start_time")
    private LocalDateTime start_time;

    @Column(name = "end_time")
    private LocalDateTime end_time;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private type type;
}
