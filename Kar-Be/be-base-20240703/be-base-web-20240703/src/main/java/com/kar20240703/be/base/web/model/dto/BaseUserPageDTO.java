package com.kar20240703.be.base.web.model.dto;

import com.kar20240703.be.temp.web.model.dto.MyPageDTO;
import com.kar20240703.be.temp.web.model.enums.TempRequestCategoryEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserPageDTO extends MyPageDTO {

    @Schema(description = "主键 id")
    private Long id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "微信 appId")
    private String wxAppId;

    @Schema(description = "微信 openId")
    private String wxOpenId;

    @Schema(description = "是否正常")
    private Boolean enableFlag;

    @Schema(description = "是否有密码")
    private Boolean passwordFlag;

    @Schema(description = "创建开始时间")
    private Date beginCreateTime;

    @Schema(description = "创建结束时间")
    private Date endCreateTime;

    @Schema(description = "最近活跃开始时间")
    private Date beginLastActiveTime;

    @Schema(description = "最近活跃结束时间")
    private Date endLastActiveTime;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "Ip2RegionUtil.getRegion() 获取到的 ip所处区域")
    private String region;

    @Schema(description = "注册终端")
    private TempRequestCategoryEnum signUpType;

}
