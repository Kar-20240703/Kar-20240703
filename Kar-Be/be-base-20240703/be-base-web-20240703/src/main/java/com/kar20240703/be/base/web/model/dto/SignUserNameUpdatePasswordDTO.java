package com.kar20240703.be.base.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUserNameUpdatePasswordDTO {

    @NotBlank
    @Schema(description = "前端加密之后的旧密码")
    private String oldPassword;

    @NotBlank
    @Schema(description = "前端加密之后的新密码")
    private String newPassword;

    @NotBlank
    @Schema(description = "前端加密之后的原始新密码")
    private String originNewPassword;

}
