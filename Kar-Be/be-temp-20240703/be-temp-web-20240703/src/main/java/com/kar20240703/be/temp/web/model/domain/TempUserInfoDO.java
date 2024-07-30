package com.kar20240703.be.temp.web.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_user_info")
@Data
@Schema(description = "子表：用户基本信息表，主表：用户表")
public class TempUserInfoDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "用户主键 id")
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "该用户的 uuid，本系统使用 id，不使用此字段（uuid），备注：不允许修改")
    private String uuid;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "头像 fileId（文件主键 id），备注：没有时则为 -1")
    private Long avatarFileId;

    @Schema(description = "注册终端")
    private TempRequestCategoryEnum signUpType;

    @Schema(description = "最近活跃时间")
    private Date lastActiveTime;

    @Schema(description = "最近 ip")
    private String lastIp;

    @Schema(description = "最近 ip所处区域")
    private String lastRegion;

}
