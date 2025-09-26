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
@Table(name="tb_appointment",
        indexes = {@Index(name="idx_appt_enterprise_start", columnList="enterpriseId,startAt"),})
public class Appointment {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enterpriseId")
    private enterpriseModel enterprise;
    @Column(nullable=false) private OffsetDateTime startAt; // com fuso
    @Column(nullable=false) private OffsetDateTime endAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "collaboratorId")
    private CollaboratorModel collaboratorId;

    @Enumerated(EnumType.STRING) // importante: salva como texto no banco
    @Column(nullable=false, length=20)
    private Status status = Status.BOOKED;

    // dados do cliente (mínimo)
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    public enum Status {
        BOOKED,
        CANCELLED
    }
}