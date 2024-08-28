package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.annotation.NotCheckBlankPattern;
import com.kar20240703.be.temp.web.model.constant.TempRegexConstant;
import com.kar20240703.be.temp.web.model.dto.TempInsertOrUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserInsertOrUpdateDTO extends TempInsertOrUpdateDTO {

    @Size(max = 100)
    @NotCheckBlankPattern(regexp = TempRegexConstant.USER_NAME_REGEXP)
    @Schema(description = "用户名")
    private String username;

    @Size(max = 200)
    @NotCheckBlankPattern(regexp = TempRegexConstant.EMAIL)
    @Schema(description = "邮箱")
    private String email;

    @Size(max = 100)
    @NotCheckBlankPattern(regexp = TempRegexConstant.PHONE)
    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "微信 appId")
    private String wxAppId;

    @Schema(description = "微信 openId")
    private String wxOpenId;

    @Schema(description = "前端加密之后的密码")
    private String password;

    @Schema(description = "前端加密之后的原始密码")
    private String originPassword;

    @Size(max = 100)
    @Pattern(regexp = TempRegexConstant.NICK_NAME_REGEXP)
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "正常/冻结")
    private Boolean enableFlag;

    @Schema(description = "角色 idSet")
    private Set<Long> roleIdSet;

    @Schema(description = "权限 idSet")
    private Set<Long> authIdSet;

    @Schema(description = "部门 idSet")
    private Set<Long> deptIdSet;

    @Schema(description = "岗位 idSet")
    private Set<Long> postIdSet;

    @Schema(description = "是否允许登录：后台管理系统")
    private Boolean manageSignInFlag;

}
