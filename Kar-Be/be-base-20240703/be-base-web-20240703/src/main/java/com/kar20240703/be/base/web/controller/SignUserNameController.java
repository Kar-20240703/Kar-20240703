package com.kar20240703.be.base.web.controller;

import com.kar20240703.be.base.web.model.dto.SignUserNameJwtRefreshTokenDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignDeleteDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignInPasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignUpDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameUpdateUserNameDTO;
import com.kar20240703.be.base.web.service.SignUserNameService;
import com.kar20240703.be.temp.web.model.constant.OperationDescriptionConstant;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sign/userName")
@Tag(name = "基础-登录注册-用户名")
public class SignUserNameController {

    @Resource
    SignUserNameService baseService;

    @PostMapping(value = "/signUp")
    @Operation(summary = "注册")
    public R<String> signUp(@RequestBody @Valid SignUserNameSignUpDTO dto) {
        return R.okMsg(baseService.signUp(dto));
    }

    @PostMapping(value = "/signIn/password")
    @Operation(summary = "账号密码登录", description = OperationDescriptionConstant.SIGN_IN)
    public R<SignInVO> signInPassword(@RequestBody @Valid SignUserNameSignInPasswordDTO dto) {
        return R.okData(baseService.signInPassword(dto));
    }

    @PostMapping(value = "/updatePassword")
    @Operation(summary = "修改密码")
    public R<String> updatePassword(@RequestBody @Valid SignUserNameUpdatePasswordDTO dto) {
        return R.okMsg(baseService.updatePassword(dto));
    }

    @PostMapping(value = "/updateUserName")
    @Operation(summary = "修改用户名")
    public R<String> updateUserName(@RequestBody @Valid SignUserNameUpdateUserNameDTO dto) {
        return R.okMsg(baseService.updateUserName(dto));
    }

    @PostMapping(value = "/signDelete")
    @Operation(summary = "账号注销")
    public R<String> signDelete(@RequestBody @Valid SignUserNameSignDeleteDTO dto) {
        return R.okMsg(baseService.signDelete(dto));
    }

    @PostMapping(value = "/jwtRefreshToken")
    @Operation(summary = "刷新token")
    public R<SignInVO> jwtRefreshToken(@RequestBody @Valid SignUserNameJwtRefreshTokenDTO dto) {
        return R.okData(baseService.jwtRefreshToken(dto));
    }

}
