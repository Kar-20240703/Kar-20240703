package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseAuthInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "权限名，不能重复")
    private String name;

    @NotBlank
    @Schema(description = "权限，例子：base:menu:edit")
    private String auth;

    @Schema(description = "角色idSet")
    private Set<Long> roleIdSet;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
