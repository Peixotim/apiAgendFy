// marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorAvailability
package marketing.digital.rj.apiagendfy.Collaborator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_collaborator_availability")
public class CollaboratorAvailability {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "collaborator_id", nullable = false)
    private CollaboratorModel collaborator; // ✅ este nome precisa bater com mappedBy

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "timezone", nullable = false, length = 64)
    private String timezone = "America/Sao_Paulo";

    @Column(name = "step_minutes", nullable = false)
    private int stepMinutes = 30;

    @Column(name = "capacity", nullable = false)
    private int capacity = 1;
}