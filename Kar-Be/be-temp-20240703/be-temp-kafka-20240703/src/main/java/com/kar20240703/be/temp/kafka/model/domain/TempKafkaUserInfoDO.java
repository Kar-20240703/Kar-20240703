package com.kar20240703.be.temp.kafka.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@TableName(value = "base_user_info")
@Data
@Schema(description = "子表：用户基本信息表，主表：用户表")
public class TempKafkaUserInfoDO {

    @TableId(type = IdType.INPUT)
    @Schema(description = "用户主键 id")
    private Long id;

    @Schema(description = "最近活跃时间")
    private Date lastActiveTime;

    @Schema(description = "最近 ip")
    private String lastIp;

    @Schema(description = "最近 ip所处区域")
    private String lastRegion;

}
