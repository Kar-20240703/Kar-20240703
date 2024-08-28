package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserUpdatePasswordDTO extends NotEmptyIdSet {

    @Schema(description = "前端加密之后的，新密码")
    private String newPassword;

    @Schema(description = "前端加密之后的原始密码，新密码")
    private String newOriginPassword;

}
