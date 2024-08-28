package com.kar20240703.be.base.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseUserDictListDTO {

    @Schema(description = "是否追加 admin账号，默认：true")
    private Boolean addAdminFlag;

}
