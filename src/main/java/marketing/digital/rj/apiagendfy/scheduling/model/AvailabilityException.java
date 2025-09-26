package marketing.digital.rj.apiagendfy.scheduling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tb_availability_exception")
public class AvailabilityException {
    public enum Type { BLOCK, OPEN }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "enterpriseId")
    private enterpriseModel enterprise;
    @Column(nullable=false) private LocalDate date; // YYYY-MM-DD
    private LocalTime startTime; // opcional
    private LocalTime endTime;   // opcional

    @Enumerated(EnumType.STRING)
    @Column(nullable=false) private Type type = Type.BLOCK;

}