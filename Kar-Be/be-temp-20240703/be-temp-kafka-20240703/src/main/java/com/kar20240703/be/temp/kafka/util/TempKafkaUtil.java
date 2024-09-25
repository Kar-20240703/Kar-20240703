package com.kar20240703.be.temp.kafka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kar20240703.be.temp.kafka.model.domain.TempKafkaUserInfoDO;
import com.kar20240703.be.temp.kafka.model.enums.TempKafkaTopicEnum;
import com.kar20240703.be.temp.kafka.model.interfaces.IKafkaTopic;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka 工具类
 */
@Component
public class TempKafkaUtil {

    private static KafkaTemplate<String, String> kafkaTemplate;

    // 目的：Long 转 String，Enum 转 code
    private static ObjectMapper objectMapper;

    public TempKafkaUtil(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {

        TempKafkaUtil.kafkaTemplate = kafkaTemplate;
        TempKafkaUtil.objectMapper = objectMapper;

    }

    /**
     * 发送消息，备注：建议封装一层
     */
    @SneakyThrows
    public static void send(IKafkaTopic iKafkaTopic, Object data) {

        if (data instanceof String) {

            kafkaTemplate.send(iKafkaTopic.name(), (String)data);

        } else {

            kafkaTemplate.send(iKafkaTopic.name(), objectMapper.writeValueAsString(data));

        }

    }

    /**
     * 发送消息：更新：用户信息
     */
    public static void sendUpdateUserInfoTopic(TempKafkaUserInfoDO tempKafkaUserInfoDO) {

        send(TempKafkaTopicEnum.UPDATE_USER_LAST_ACTIVE, tempKafkaUserInfoDO);

    }

}
