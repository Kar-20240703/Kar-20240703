package com.kar20240703.be.base.web.model.vo;

import com.kar20240703.be.base.web.model.domain.BaseRoleDO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseRoleInfoByIdVO extends BaseRoleDO {

    @Schema(description = "用户idSet")
    private Set<Long> userIdSet;

    @Schema(description = "菜单idSet")
    private Set<Long> menuIdSet;

    @Schema(description = "权限idSet")
    private Set<Long> authIdSet;

}
