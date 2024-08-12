package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_user_configuration")
@Data
@Schema(description = "主表：用户登录注册等相关配置")
public class BaseUserConfigurationDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "是否启用：用户名注册功能，默认启用")
    private Boolean usernameSignUpEnable;

    @Schema(description = "是否启用：邮箱注册功能，默认启用")
    private Boolean emailSignUpEnable;

    @Schema(description = "是否启用：手机号码注册功能，默认启用")
    private Boolean phoneSignUpEnable;

}
