package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseDeptInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @Schema(description = "父节点id（顶级则为0）")
    private Long pid;

    @NotBlank
    @Schema(description = "部门名")
    private String name;

    @Schema(description = "该部门的 uuid，备注：不能重复")
    private String uuid;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "用户idSet")
    private Set<Long> userIdSet;

}
