package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasePostPageDTO extends MyPageDTO {

    @Schema(description = "岗位名")
    private String name;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

}
