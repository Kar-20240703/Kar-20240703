package com.kar20240703.be.temp.kafka.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.temp.kafka.mapper.TempKafkaUserInfoMapper;
import com.kar20240703.be.temp.kafka.model.domain.TempKafkaUserInfoDO;
import com.kar20240703.be.temp.kafka.service.TempKafkaUserInfoService;
import org.springframework.stereotype.Service;

@Service
public class TempKafkaUserInfoServiceImpl extends ServiceImpl<TempKafkaUserInfoMapper, TempKafkaUserInfoDO>
    implements TempKafkaUserInfoService {
}
