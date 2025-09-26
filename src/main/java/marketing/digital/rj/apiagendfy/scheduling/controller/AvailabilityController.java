package marketing.digital.rj.apiagendfy.scheduling.controller;

import marketing.digital.rj.apiagendfy.scheduling.dto.AppointmentDTO;
import marketing.digital.rj.apiagendfy.scheduling.dto.AvailabilityResponseDTO;
import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;
import marketing.digital.rj.apiagendfy.scheduling.service.AppointmentService;
import marketing.digital.rj.apiagendfy.scheduling.service.AvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    private final AvailabilityService service;
    private final AppointmentService appointmentService;

    public AvailabilityController(AvailabilityService s,AppointmentService service) {
        this.service = s;
        this.appointmentService = service;
    }


    // GET /availability?enterpriseId=1&date=2025-09-23&tz=America/Sao_Paulo
    @GetMapping
    public ResponseEntity<AvailabilityResponseDTO> get(
            @RequestParam UUID enterpriseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required=false) String tz
    ) {
        return ResponseEntity.ok(service.getDay(enterpriseId, date, tz));
    }

}