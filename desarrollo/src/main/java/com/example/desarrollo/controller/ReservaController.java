package com.example.desarrollo.controller;
import com.example.desarrollo.dto.ReservaRequestDTO;
import com.example.desarrollo.dto.ReservaResponseDTO;
import com.example.desarrollo.service.ReservaService;
import jakarta.validation.Valid;
import com.example.desarrollo.model.Reserva;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("reservas")
public class ReservaController {
    private final ReservaService reservaService;
    public ReservaController(ReservaService reservaService){
        this.reservaService=reservaService;
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> getALlReserva(){
        List<Reserva> reservas=reservaService.findAll();
        return ResponseEntity.ok(reservas);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Long id){
        Reserva reserva= reservaService.findById(id);
        if(reserva!=null){
            return ResponseEntity.ok(reserva);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> createReserva(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO){
        ReservaResponseDTO reservaResponseDTO=reservaService.createReserva(reservaRequestDTO);
        URI location = URI.create("reservas/"+reservaResponseDTO.getId());
        return ResponseEntity.created(location).body(reservaResponseDTO);
    }
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> updateReserva(@PathVariable Long id){
        ReservaResponseDTO reservaResponseDTO=reservaService.cancelReserva(id);
        return ResponseEntity.ok(reservaResponseDTO);
    }
}
