package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.constant.TempRegexConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BaseUserSelfUpdateInfoDTO {

    @Pattern(regexp = TempRegexConstant.NICK_NAME_REGEXP)
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

}
