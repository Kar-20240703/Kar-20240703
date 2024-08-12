package com.kar20240703.be.base.web.service;

import com.kar20240703.be.base.web.model.dto.SignUserNameSignDeleteDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignInPasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameSignUpDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignUserNameUpdateUserNameDTO;
import com.kar20240703.be.temp.web.model.vo.SignInVO;

public interface SignUserNameService {

    String signUp(SignUserNameSignUpDTO dto);

    SignInVO signInPassword(SignUserNameSignInPasswordDTO dto);

    String updatePassword(SignUserNameUpdatePasswordDTO dto);

    String updateUserName(SignUserNameUpdateUserNameDTO dto);

    String signDelete(SignUserNameSignDeleteDTO dto);

}
