package com.kar20240703.be.base.web.service.impl;

import com.kar20240703.be.base.web.model.dto.SignUserNameSignDeleteDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignInPasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignUpDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameUpdateUserNameDTO;
import com.kar20240703.be.base.web.service.SignUserNameService;
import com.kar20240703.be.temp.web.model.vo.SignInVO;
import org.springframework.stereotype.Service;

@Service
public class SignUserNameServiceImpl implements SignUserNameService {

    /**
     * 注册
     */
    @Override
    public String signUp(SignUserNameSignUpDTO dto) {
        return "";
    }

    /**
     * 账号密码登录
     */
    @Override
    public SignInVO signInPassword(SignUserNameSignInPasswordDTO dto) {
        return null;
    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignUserNameUpdatePasswordDTO dto) {
        return "";
    }

    /**
     * 修改用户名
     */
    @Override
    public String updateUserName(SignUserNameUpdateUserNameDTO dto) {
        return "";
    }

    /**
     * signDelete
     */
    @Override
    public String signDelete(SignUserNameSignDeleteDTO dto) {
        return "";
    }
}
