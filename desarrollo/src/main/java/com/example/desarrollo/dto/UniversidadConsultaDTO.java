package com.example.desarrollo.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UniversidadConsultaDTO {
    @NotNull private Long id;
}
