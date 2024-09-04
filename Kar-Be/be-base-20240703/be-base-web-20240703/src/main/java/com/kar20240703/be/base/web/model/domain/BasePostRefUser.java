package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName(value = "base_post_ref_user")
@Data
@Schema(description = "关联表：岗位表，用户表")
public class BasePostRefUser {

    @TableId(type = IdType.INPUT)
    @Schema(description = "岗位主键id")
    private Long postId;

    @Schema(description = "用户主键id")
    private Long userId;

}