package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "base_role_ref_menu")
@Data
@Schema(description = "关联表：角色表，菜单表")
@NoArgsConstructor
@AllArgsConstructor
public class BaseRoleRefMenuDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "角色主键id")
    private Long roleId;

    @Schema(description = "菜单主键id")
    private Long menuId;

}