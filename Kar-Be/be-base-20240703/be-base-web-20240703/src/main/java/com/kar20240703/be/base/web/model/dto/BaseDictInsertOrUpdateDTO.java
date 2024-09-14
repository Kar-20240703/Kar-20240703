package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.TempInsertOrUpdateDTO;
import com.kar20240703.be.temp.web.model.enums.TempDictTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseDictInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "字典 key（不能重复），备注：字典项要冗余这个 key，目的：方便操作")
    private String dictKey;

    @NotBlank
    @Schema(description = "字典/字典项 名")
    private String name;

    @NotNull
    @Schema(description = "字典类型")
    private TempDictTypeEnum type;

    @Schema(description = "字典项 value（数字 123...）备注：字典为 -1")
    private Integer value;

    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "该字典的 uuid，备注：不能重复")
    private String uuid;

}
