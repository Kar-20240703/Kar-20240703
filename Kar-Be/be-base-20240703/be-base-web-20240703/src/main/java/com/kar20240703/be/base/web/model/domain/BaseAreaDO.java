package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240703.be.temp.web.model.domain.TempEntityTree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_area")
@Data
@Schema(description = "主表：区域表")
public class BaseAreaDO extends TempEntityTree<BaseAreaDO> {

    @Schema(description = "区域名")
    private String name;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "该区域的 uuid，备注：不能重复")
    private String uuid;

}