package marketing.digital.rj.apiagendfy.scheduling.controller;

import marketing.digital.rj.apiagendfy.scheduling.dto.AppointmentDTO;
import marketing.digital.rj.apiagendfy.scheduling.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentsController {

    private AppointmentService service;
    public AppointmentsController(AppointmentService service){
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody AppointmentDTO appointment){
        var saved = service.create(appointment);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancel(@PathVariable Long id){
        var saved = service.cancel(id);
        return ResponseEntity.ok(saved);
    }
}
