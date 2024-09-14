package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseParamInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "参数名，备注：以 uuid为不变值进行使用，不要用此属性")
    private String name;

    @NotBlank
    @Schema(description = "值")
    private String value;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "该参数的 uuid，备注：不能重复")
    private String uuid;

}
