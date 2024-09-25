package com.kar20240703.be.base.web.configuration.security;

import cn.hutool.jwt.JWT;
import com.kar20240703.be.temp.kafka.model.domain.TempKafkaUserInfoDO;
import com.kar20240703.be.temp.kafka.util.TempKafkaUtil;
import com.kar20240703.be.temp.web.model.interfaces.IJwtFilterHandler;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class BaseJwtFilterHandler implements IJwtFilterHandler {

    @Override
    public void handleJwt(Long userId, String ip, JWT jwt) {

        TempKafkaUserInfoDO tempKafkaUserInfoDO = new TempKafkaUserInfoDO();

        tempKafkaUserInfoDO.setId(userId);
        tempKafkaUserInfoDO.setLastActiveTime(new Date());
        tempKafkaUserInfoDO.setLastIp(ip);

        TempKafkaUtil.sendUpdateUserInfoTopic(tempKafkaUserInfoDO);

    }

}
