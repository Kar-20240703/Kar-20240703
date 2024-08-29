package com.kar20240703.be.base.web.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
public class BaseUserSelfInfoVO {

    @Schema(description = "用户主键 id")
    private Long id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "邮箱，会脱敏")
    private String email;

    @Schema(description = "是否有密码，用于前端显示，修改密码/设置密码")
    private Boolean passwordFlag;

    @Schema(description = "用户名，会脱敏")
    private String username;

    @Schema(description = "手机号码，会脱敏")
    private String phone;

    @Schema(description = "微信 openId，会脱敏")
    private String wxOpenId;

    @Schema(description = "微信 appId，会脱敏")
    private String wxAppId;

    @Schema(description = "账号注册时间")
    private Date createTime;

    @Schema(description = "头像 fileId（文件主键 id）")
    private Long avatarFileId;

}
