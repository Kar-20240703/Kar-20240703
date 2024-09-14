package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseParamPageDTO extends MyPageDTO {

    @Schema(description = "参数名，备注：以 uuid为不变值进行使用，不要用此属性")
    private String name;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
