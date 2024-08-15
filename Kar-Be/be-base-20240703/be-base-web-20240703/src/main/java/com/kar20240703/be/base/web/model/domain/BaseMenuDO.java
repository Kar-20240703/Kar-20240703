package com.kar20240703.be.base.web.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kar20240703.be.temp.web.model.domain.TempEntityTree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName(value = "base_menu")
@Data
@Schema(description = "主表：菜单表")
public class BaseMenuDO extends TempEntityTree<BaseMenuDO> {

    @Schema(description = "菜单名")
    private String name;

    @Schema(description = "页面的 path，备注：相同父菜单下，子菜单 path不能重复")
    private String path;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "是否显示在 左侧的菜单栏里面，如果为 false，也可以通过 $router.push()访问到")
    private Integer showFlag;

    @Schema(description = "是否外链，即，打开页面会在一个新的窗口打开，可以配合 router")
    private Integer linkFlag;

    @Schema(description = "路由")
    private String router;

    @Schema(description = "重定向，优先级最高")
    private String redirect;

    @Schema(description = "是否是起始页面，备注：只能存在一个 firstFlag === true 的菜单")
    private Integer firstFlag;

    @Schema(description = "该菜单的 uuid，用于：同步租户菜单等操作，备注：不允许修改")
    private String uuid;

    @Schema(description = "是否隐藏：PageContainer")
    private Boolean hiddenPageContainerFlag;

}