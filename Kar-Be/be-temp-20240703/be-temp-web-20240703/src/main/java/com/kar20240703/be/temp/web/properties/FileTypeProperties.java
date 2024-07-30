package com.kar20240703.be.temp.web.properties;

import com.kar20240703.be.temp.web.model.constant.PropertiesPrefixConstant;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = PropertiesPrefixConstant.FILE_TYPE)
public class FileTypeProperties {

    /**
     * 如果识别不出来文件类型时，允许的文件名后缀
     */
    private Set<String> allowSet;

}
