package com.example.desarrollo.service;

import com.example.desarrollo.dto.EstudianteSimpleDTO;
import com.example.desarrollo.dto.HabitacionIdArrendadorDTO;
import com.example.desarrollo.dto.ReservaRequestDTO;
import com.example.desarrollo.dto.ReservaResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Estado;
import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.Reserva;
import com.example.desarrollo.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service

public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final ModelMapper modelMapper;
    private HabitacionService habitacionService;
    private EstudianteService estudianteService;

    public ReservaService(ReservaRepository reservaRepository, ModelMapper modelMapper, HabitacionService habitacionService, EstudianteService estudianteService) {
        this.reservaRepository = reservaRepository;
        this.modelMapper = modelMapper;
        this.habitacionService = habitacionService;
        this.estudianteService = estudianteService;
    }

    public Reserva findById(Long id){
        return reservaRepository.findById(id).orElse(null);
    }

    public List<Reserva> findAll(){
        return reservaRepository.findAll();
    }

    public void deleteById(Long id){
        if (reservaRepository.existsById(id)){
            reservaRepository.deleteById(id);
        }
        throw new  ResourceNotFoundException("No existe reserva con id: " + id);
    }

    public Reserva save (Reserva reserva){
        return reservaRepository.save(reserva);
    }


    @Transactional
    public ReservaResponseDTO crearReserva(ReservaRequestDTO dto) {

        // 1. Validaciones
        Habitacion habitacion = habitacionService.findById(dto.getHabitacion().getId());
        if (habitacion == null) {
            throw new ResourceNotFoundException("Habitación no encontrada");
        }

        Estudiante estudiante = estudianteService.findById(dto.getEstudiante().getId());
        if (estudiante == null) {
            throw new ResourceNotFoundException("Estudiante no encontrado");
        }

        // 2. Creación del objeto Reserva
        Reserva reserva = new Reserva();
        reserva.setFecha_inicio(LocalDate.now());
        reserva.setFecha_fin(dto.getFecha_fin());
        reserva.setHabitacion(habitacion);
        reserva.setEstudiante(estudiante);
        reserva.setEstado(Estado.PENDIENTE);

        // 3. Guardado en BD de forma síncrona
        Reserva reservaGuardada = reservaRepository.save(reserva);

        // 4. Retorno de resultado

        ReservaResponseDTO reservaResponseDTO = new ReservaResponseDTO();
        reservaResponseDTO.setId(reservaGuardada.getId());
        reservaResponseDTO.setEstado(reservaGuardada.getEstado());
        reservaResponseDTO.setFecha_inicio(reservaGuardada.getFecha_inicio());
        reservaResponseDTO.setFecha_fin(reservaGuardada.getFecha_fin());
        reservaResponseDTO.setHabitacion(modelMapper.map(reservaGuardada.getHabitacion(), HabitacionIdArrendadorDTO.class));
        reservaResponseDTO.setEstudiante(modelMapper.map(reservaGuardada.getEstudiante(), EstudianteSimpleDTO.class));

        return reservaResponseDTO;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ReservaResponseDTO cancelReserva(Long id){
        Reserva reserva=reservaRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No existe reserva con el ID:"+id));
        reserva.setEstado(Estado.CANCELADO);
        Reserva newReserva=reservaRepository.save(reserva);
        return modelMapper.map(newReserva,ReservaResponseDTO.class);
    }
}
