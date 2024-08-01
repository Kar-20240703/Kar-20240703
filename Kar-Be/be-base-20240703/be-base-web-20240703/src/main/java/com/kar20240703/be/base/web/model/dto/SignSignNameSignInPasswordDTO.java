package com.kar20240703.be.base.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignSignNameSignInPasswordDTO extends SignNameNotBlankDTO {

    @NotBlank
    @Schema(description = "前端加密之后的密码")
    private String password;

}
