package com.kar20240703.be.base.web.service;

import com.kar20240703.be.base.web.model.dto.SignSignNameSignDeleteDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameSignInPasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameSignUpDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.dto.SignSignNameUpdateSignNameDTO;
import com.kar20240703.be.temp.web.model.vo.SignInVO;

public interface SignSignNameService {

    String signUp(SignSignNameSignUpDTO dto);

    SignInVO signInPassword(SignSignNameSignInPasswordDTO dto);

    String updatePassword(SignSignNameUpdatePasswordDTO dto);

    String updateSignName(SignSignNameUpdateSignNameDTO dto);

    String signDelete(SignSignNameSignDeleteDTO dto);

}
