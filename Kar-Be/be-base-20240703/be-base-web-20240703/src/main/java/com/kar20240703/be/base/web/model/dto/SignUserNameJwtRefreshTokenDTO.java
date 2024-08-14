package com.kar20240703.be.base.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUserNameJwtRefreshTokenDTO {

    @NotBlank
    @Schema(description = "jwtRefreshToken")
    private String jwtRefreshToken;

}
