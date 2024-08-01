package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.constant.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class SignNameNotBlankDTO {

    @Size(max = 20)
    @NotBlank
    @Pattern(regexp = TempRegexConstant.SIGN_NAME_REGEXP)
    @Schema(description = "登录名")
    private String signName;

}
