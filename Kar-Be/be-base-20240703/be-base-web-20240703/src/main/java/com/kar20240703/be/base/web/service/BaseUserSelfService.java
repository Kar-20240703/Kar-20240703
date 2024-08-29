package com.kar20240703.be.base.web.service;

import com.kar20240703.be.base.web.model.dto.BaseUserSelfUpdateInfoDTO;
import com.kar20240703.be.base.web.model.vo.BaseUserSelfInfoVO;

public interface BaseUserSelfService {

    BaseUserSelfInfoVO userSelfInfo();

    String userSelfUpdateInfo(BaseUserSelfUpdateInfoDTO dto);

    String userSelfResetAvatar();

}
