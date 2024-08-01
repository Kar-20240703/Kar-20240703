package com.kar20240703.be.base.web.service.impl;

import com.kar20240703.be.base.web.model.dto.SignSignNameSignDeleteDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameSignInPasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameSignUpDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameUpdateSignNameDTO;
import com.kar20240703.be.base.web.service.SignSignNameService;
import com.kar20240703.be.temp.web.model.vo.SignInVO;

public class SignSignNameServiceImpl implements SignSignNameService {

    /**
     * 注册
     */
    @Override
    public String signUp(SignSignNameSignUpDTO dto) {
        return "";
    }

    /**
     * 账号密码登录
     */
    @Override
    public SignInVO signInPassword(SignSignNameSignInPasswordDTO dto) {
        return null;
    }

    /**
     * 修改密码
     */
    @Override
    public String updatePassword(SignSignNameUpdatePasswordDTO dto) {
        return "";
    }

    /**
     * 修改登录名
     */
    @Override
    public String updateSignName(SignSignNameUpdateSignNameDTO dto) {
        return "";
    }

    /**
     * signDelete
     */
    @Override
    public String signDelete(SignSignNameSignDeleteDTO dto) {
        return "";
    }
}
