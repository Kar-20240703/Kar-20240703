package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.constant.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserNameNotBlankDTO {

    @Size(max = 100)
    @NotBlank
    @Pattern(regexp = TempRegexConstant.USER_NAME_REGEXP)
    @Schema(description = "用户名")
    private String username;

}
