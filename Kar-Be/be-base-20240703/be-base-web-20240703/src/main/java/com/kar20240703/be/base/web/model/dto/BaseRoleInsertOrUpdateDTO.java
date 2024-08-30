package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseRoleInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @NotBlank
    @Schema(description = "角色名，不能重复")
    private String name;

    @Schema(description = "唯一标识（不能重复）")
    private String uuid;

    @Schema(description = "菜单idSet")
    private Set<Long> menuIdSet;

    @Schema(description = "用户idSet")
    private Set<Long> userIdSet;

    @Schema(description = "权限idSet")
    private Set<Long> authIdSet;

    @Schema(description = "是否是默认角色，备注：只会有一个默认角色")
    private Boolean defaultFlag;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "备注")
    private String remark;

}
