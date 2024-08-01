package com.kar20240703.be.base.web.controller;

import com.kar20240703.be.base.web.model.dto.SignSignNameSignDeleteDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameSignInPasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameSignUpDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameUpdateSignNameDTO;
import com.kar20240703.be.base.web.service.SignSignNameService;
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
@RequestMapping(value = "/sign/signName")
@Tag(name = "基础-登录注册-登录名")
public class SignSignNameController {

    @Resource
    SignSignNameService baseService;

    @PostMapping(value = "/sign/up")
    @Operation(summary = "注册")
    public R<String> signUp(@RequestBody @Valid SignSignNameSignUpDTO dto) {
        return R.okMsg(baseService.signUp(dto));
    }

    @PostMapping(value = "/sign/in/password")
    @Operation(summary = "账号密码登录", description = OperationDescriptionConstant.SIGN_IN)
    public R<SignInVO> signInPassword(@RequestBody @Valid SignSignNameSignInPasswordDTO dto) {
        return R.okData(baseService.signInPassword(dto));
    }

    @PostMapping(value = "/updatePassword")
    @Operation(summary = "修改密码")
    public R<String> updatePassword(@RequestBody @Valid SignSignNameUpdatePasswordDTO dto) {
        return R.okMsg(baseService.updatePassword(dto));
    }

    @PostMapping(value = "/updateSignName")
    @Operation(summary = "修改登录名")
    public R<String> updateSignInName(@RequestBody @Valid SignSignNameUpdateSignNameDTO dto) {
        return R.okMsg(baseService.updateSignName(dto));
    }

    @PostMapping(value = "/signDelete")
    @Operation(summary = "账号注销")
    public R<String> signDelete(@RequestBody @Valid SignSignNameSignDeleteDTO dto) {
        return R.okMsg(baseService.signDelete(dto));
    }

}
