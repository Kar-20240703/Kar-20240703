package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.MyPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseMenuPageDTO extends MyPageDTO {

    @Schema(description = "菜单名")
    private String name;

    @Schema(description = "页面的 path，备注：不能重复")
    private String path;

    @Schema(description = "父节点id（顶级则为0）")
    private Long pid;

    @Schema(description = "是否显示在 左侧的菜单栏里面，如果为 false，也可以通过 $router.push()访问到")
    private Boolean showFlag;

    @Schema(description = "是否启用")
    private Boolean enableFlag;

    @Schema(description = "是否外链，即，打开页面会在一个新的窗口打开")
    private Boolean linkFlag;

    @Schema(description = "路由")
    private String router;

    @Schema(description = "重定向，优先级最高")
    private String redirect;

}
