package marketing.digital.rj.apiagendfy.scheduling.model;

import jakarta.persistence.*;

import java.util.UUID;

// WorkingHoursRepository.java
@Entity
@Table(name = "working_hours")
public class WorkingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID enterpriseId;
    private int dayOfWeek;         // 1=Mon ... 7=Sun (use ISO)
    private String timezone;       // "America/Sao_Paulo"
    private int stepMinutes;       // 30, 20, 60...

    private java.time.LocalTime startTime;
    private java.time.LocalTime endTime;

    private int capacity;          // 1, 2...

    // getters/setters
}