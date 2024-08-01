package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_role_user")
@Data
@Schema(description = "关联表：角色表，用户表")
public class BaseRoleUserDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "角色主键id")
    private Long roleId;

    @Schema(description = "用户主键id")
    private Long userId;

}