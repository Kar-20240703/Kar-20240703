package com.kar20240703.be.auth.web.service.impl;

import com.kar20240703.be.auth.web.service.AuthService;
import com.kar20240703.be.auth.web.util.MyJwtUtil;
import com.kar20240703.be.auth.web.util.UserUtil;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    /**
     * 获取权限集合
     * <p>
     * 备注：会检查 jwt的合法性，以及用户的状态
     *
     * @return null 所有权限 空集合 无权限
     */
    @Override
    public List<String> getAuthList() {

        Long userId = UserUtil.getCurrentUserId();

        // 获取：获取用户权限集合
        return MyJwtUtil.getAuthListByUserId(userId);

    }

}
