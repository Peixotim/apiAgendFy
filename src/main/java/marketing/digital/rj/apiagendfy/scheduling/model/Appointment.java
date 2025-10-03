// marketing.digital.rj.apiagendfy.scheduling.model.Appointment
package marketing.digital.rj.apiagendfy.scheduling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorModel;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "tb_appointment",
        indexes = {
                @Index(name = "idx_appt_enterprise_start", columnList = "enterprise_id,start_at")
        }
)
public class Appointment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private enterpriseModel enterprise;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "collaborator_id", nullable = false)
    private CollaboratorModel collaborator; // ✅ nome do ATRIBUTO é 'collaborator'

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.BOOKED;

    private String customerName;
    private String customerEmail;
    private String customerPhone;

    public enum Status { BOOKED, CANCELLED }
}