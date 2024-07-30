package com.kar20240703.be.base.web.properties;

import com.kar20240703.be.base.web.model.constant.PropertiesPrefixConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = PropertiesPrefixConstant.COMMON)
public class CommonProperties {

    @Schema(description = "平台名称")
    private String platformName = "Kar";

}
