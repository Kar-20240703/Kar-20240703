package com.kar20240703.be.auth.web.properties;

import com.kar20240703.be.auth.web.model.constant.PropertiesPrefixConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = PropertiesPrefixConstant.SECURITY)
public class SecurityProperties {

    /**
     * jwt密钥前缀
     */
    private String jwtSecretPre =
        "202e5c4e94c60b8e96cc6c8c2471309c11123a40ef996dd5ab3b180ba9a0dabefe99123edeff526e1d3d264f8dde85eaf6ace1ea236d826fda32080d00f64b47ad0111";

}
