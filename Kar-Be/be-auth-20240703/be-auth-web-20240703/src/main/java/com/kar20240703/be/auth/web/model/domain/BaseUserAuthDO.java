package com.kar20240703.be.auth.web.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_user_auth")
@Data
@Schema(description = "关联表：用户表，权限表")
public class BaseUserAuthDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "用户主键id")
    private Long userId;

    @Schema(description = "权限，例子：base:menu:edit")
    private Long auth;

}