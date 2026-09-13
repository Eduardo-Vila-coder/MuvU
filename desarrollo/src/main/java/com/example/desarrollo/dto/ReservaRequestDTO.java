package com.example.desarrollo.dto;

import com.example.desarrollo.model.Estado;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequestDTO {
    @NotNull
    private Estado estado;
}
