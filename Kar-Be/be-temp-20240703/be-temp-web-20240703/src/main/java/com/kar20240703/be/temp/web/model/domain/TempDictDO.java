package com.kar20240703.be.temp.web.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240703.be.temp.web.model.enums.TempDictTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_dict")
@Data
@Schema(description = "主表：字典表")
public class TempDictDO extends TempEntity {

    @Schema(description = "字典 key（不能重复），字典项要冗余这个 key，目的：方便操作")
    private String dictKey;

    @Schema(description = "字典/字典项 名")
    private String name;

    @Schema(description = "字典类型")
    private TempDictTypeEnum type;

    @Schema(description = "字典项 value（数字 123...）备注：字典为 -1")
    private Integer value;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "排序号（值越大越前面，默认为 0）")
    private Integer orderNo;

    @TableField(exist = false)
    @Schema(description = "字典的子节点")
    private List<TempDictDO> children;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "该字典的 uuid")
    private String uuid;

}
