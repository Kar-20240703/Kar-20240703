package com.kar20240703.be.temp.web.model.configuration;

import cn.hutool.jwt.JWT;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public interface IJwtGetAuthListConfiguration {

    List<String> getAuthList(String jwtStr, JWT jwt, HttpServletResponse response);

}
