package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "base_area_ref_user")
@Data
@Schema(description = "关联表：区域表，用户表")
@NoArgsConstructor
@AllArgsConstructor
public class BaseAreaRefUserDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "区域主键id")
    private Long areaId;

    @Schema(description = "用户主键id")
    private Long userId;

}