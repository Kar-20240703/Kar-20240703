package com.kar20240703.be.auth.web.controller;

import com.kar20240703.be.auth.web.model.vo.R;
import com.kar20240703.be.auth.web.service.AuthService;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限-管理
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Resource
    AuthService baseService;

    /**
     * 获取用户id
     */
    @PostMapping(value = "/getUserId")
    public R<Long> getUserId() {
        return R.okData(baseService.getUserId());
    }

    /**
     * 获取权限集合
     */
    @PostMapping(value = "/getAuthColl")
    public R<Set<String>> getAuthColl() {
        return R.okData(baseService.getAuthSet());
    }

}
