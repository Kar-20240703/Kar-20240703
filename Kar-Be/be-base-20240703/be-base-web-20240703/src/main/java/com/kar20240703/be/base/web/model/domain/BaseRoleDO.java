package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240703.be.temp.web.model.domain.TempEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_role")
@Data
@Schema(description = "主表：角色表")
public class BaseRoleDO extends TempEntity {

    @Schema(description = "角色名（不能重复）")
    private String name;

    @Schema(description = "唯一标识（不能重复）")
    private String uuid;

    @Schema(description = "是否是默认角色，备注：只会有一个默认角色")
    private Boolean defaultFlag;

}