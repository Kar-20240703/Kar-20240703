package com.kar20240703.be.temp.web.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "实体类基类-没有主键 id")
public class TempEntityNoId extends TempEntityNoIdSuper {

    @Schema(description = "是否启用")
    private Boolean enableFlag;

}
