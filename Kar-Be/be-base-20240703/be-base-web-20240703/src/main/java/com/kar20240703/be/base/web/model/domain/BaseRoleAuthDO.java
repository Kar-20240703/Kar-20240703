package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_role_auth")
@Data
@Schema(description = "关联表：角色表，权限表")
public class BaseRoleAuthDO {

    @Schema(description = "权限主键id")
    private Long authId;

    @Schema(description = "角色主键id")
    private Long roleId;

}