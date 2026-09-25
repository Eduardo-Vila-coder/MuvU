package com.example.desarrollo.dto.Logueo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequestDTO {
    @NotBlank(message = "El token es obligatorio")
    private String token;

    // Misma regla de fuerza que en el registro
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un símbolo")
    private String nuevaContrasena;
}
