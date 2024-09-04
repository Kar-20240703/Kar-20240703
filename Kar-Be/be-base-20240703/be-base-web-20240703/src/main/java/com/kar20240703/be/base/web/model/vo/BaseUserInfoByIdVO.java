package com.kar20240703.be.base.web.model.vo;

import com.kar20240703.be.temp.web.model.domain.TempUserDO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserInfoByIdVO extends TempUserDO {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "头像 fileId（文件主键 id）")
    private Long avatarFileId;

    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

    @Schema(description = "部门 idSet")
    private Set<Long> deptIdSet;

    @Schema(description = "岗位 idSet")
    private Set<Long> postIdSet;

    @Schema(description = "区域 idSet")
    private Set<Long> areaIdSet;

    @Schema(description = "权限 idSet")
    private Set<Long> authIdSet;

    @Schema(description = "是否允许登录：后台管理系统")
    private Boolean manageSignInFlag;

}
