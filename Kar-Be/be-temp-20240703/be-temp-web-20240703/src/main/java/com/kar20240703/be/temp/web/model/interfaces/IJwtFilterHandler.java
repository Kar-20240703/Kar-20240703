package com.kar20240703.be.temp.web.model.interfaces;

import cn.hutool.jwt.JWT;

public interface IJwtFilterHandler {

    void handleJwt(Long userId, String ip, JWT jwt);

}
