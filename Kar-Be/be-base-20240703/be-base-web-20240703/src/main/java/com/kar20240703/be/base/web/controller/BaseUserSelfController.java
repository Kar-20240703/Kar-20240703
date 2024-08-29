package com.kar20240703.be.base.web.controller;

import com.kar20240703.be.base.web.model.dto.BaseUserSelfUpdateInfoDTO;
import com.kar20240703.be.base.web.model.vo.BaseUserSelfInfoVO;
import com.kar20240703.be.base.web.service.BaseUserSelfService;
import com.kar20240703.be.temp.web.model.vo.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/user/self")
@Tag(name = "基础-用户-自我-管理")
public class BaseUserSelfController {

    @Resource
    BaseUserSelfService baseService;

    @Operation(summary = "获取：当前用户，基本信息")
    @PostMapping(value = "/info")
    public R<BaseUserSelfInfoVO> userSelfInfo() {
        return R.okData(baseService.userSelfInfo());
    }

    @Operation(summary = "当前用户：基本信息：修改")
    @PostMapping(value = "/updateInfo")
    public R<String> userSelfUpdateInfo(@RequestBody @Valid BaseUserSelfUpdateInfoDTO dto) {
        return R.okMsg(baseService.userSelfUpdateInfo(dto));
    }

    @Operation(summary = "当前用户：重置头像")
    @PostMapping(value = "/resetAvatar")
    public R<String> userSelfResetAvatar() {
        return R.okMsg(baseService.userSelfResetAvatar());
    }

}
