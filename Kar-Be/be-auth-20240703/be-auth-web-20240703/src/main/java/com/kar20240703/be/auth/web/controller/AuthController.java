package com.kar20240703.be.auth.web.controller;

import com.kar20240703.be.auth.web.model.vo.R;
import com.kar20240703.be.auth.web.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@Tag(name = "权限-管理")
public class AuthController {

    @Resource
    AuthService baseService;

    @PostMapping(value = "/getUserId")
    @Operation(summary = "获取用户id")
    public R<Long> getUserId() {
        return R.okData(baseService.getUserId());
    }

    @PostMapping(value = "/getAuthList")
    @Operation(summary = "获取权限集合")
    public R<List<String>> getAuthList() {
        return R.okData(baseService.getAuthList());
    }

}
