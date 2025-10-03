// marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorAvailabilityExceptions
package marketing.digital.rj.apiagendfy.Collaborator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_collaborator_availability_exceptions")
public class CollaboratorAvailabilityExceptions {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "collaborator_id", nullable = false)
    private CollaboratorModel collaborator; // ✅ bate com mappedBy

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    // Ex.: motivo/observação
    @Column(name = "note")
    private String note;
}