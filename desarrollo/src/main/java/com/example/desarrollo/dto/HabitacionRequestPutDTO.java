package com.example.desarrollo.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HabitacionRequestPutDTO {
    @NotNull @Positive private Double precio;
    @NotBlank private String direccion;
    @NotNull @Positive private Integer area;
}
