package marketing.digital.rj.apiagendfy.scheduling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;

import java.time.LocalTime;
import java.util.UUID;
@Table(name = "tb_availability_rule")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enterpriseId")
    private enterpriseModel enterprise;
    @Column(nullable=false) private Integer weekday;   // 0=domingo ... 6=sábado
    @Column(nullable=false) private LocalTime startTime;
    @Column(nullable=false) private LocalTime endTime;
    @Column(nullable=false) private Integer intervalMinutes; // tamanho do slot
    @Column(nullable=false) private Integer capacity;        // qtd por horário
    @Column(nullable=false) private String timezone = "America/Sao_Paulo";

}
