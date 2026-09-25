package com.example.desarrollo.dto.Logueo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenRequestDTO {
    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;
}
