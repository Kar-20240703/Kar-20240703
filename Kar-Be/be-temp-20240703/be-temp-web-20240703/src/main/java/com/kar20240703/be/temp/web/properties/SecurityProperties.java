package com.kar20240703.be.temp.web.properties;

import cn.hutool.crypto.digest.DigestUtil;
import com.kar20240703.be.temp.web.model.constant.PropertiesPrefixConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = PropertiesPrefixConstant.SECURITY)
public class SecurityProperties {

    @Schema(description = "jwt 密钥前缀")
    private String jwtSecretPre =
        "202e5c4e94c60b8e96cc6c8c2471309c11123a40ef996dd5ab3b180ba9a0dabefe99123edeff526e1d3d264f8dde85eaf6ace1ea236d826fda32080d00f64b47ad0111";

    @Schema(description = "jwt 刷新token密钥前缀")
    private String jwtRefreshTokenSecretPre =
        "202e5c4e94c60b8e96cc6c8c2331309c11123a40ef996dd5ab3b180ba9a123cefe99123edeff526e1d3d264f8dde85eaf6ace1ea235d826fda32080d00f64b47ad0111";

    @Schema(description = "admin 的昵称")
    private String adminNickname = "系统管理员";

    @Schema(
        description = "admin 的密码，默认为 karadmin，下面是 karadmin经过 sha加密之后的字符串，加密次数和方法和前端需进行统一，输入 karadmin即可登录，也可以使用本类的 generateAdminPassword方法，快速生成新的 admin密码")
    private String adminPassword = "19b5818d7fe851f4510715cbf2193204df3f5b82808a84faaa02634752886ff2";

    @Schema(description = "通过jwt获取权限集合的url")
    private String jwtGetAuthListUrl = "http://localhost:8001/auth/getAuthList";

    @Schema(description = "通过jwt获取用户id的url")
    private String jwtGetUserIdUrl = "http://localhost:8001/auth/getUserId";

    public static void main(String[] args) {

        generateAdminPassword();

    }

    /**
     * 生成 admin的密码 备注：需要和前端一致：先 512，然后再 256
     */
    private static void generateAdminPassword() {

        String password = "karadmin";

        password = DigestUtil.sha256Hex((DigestUtil.sha512Hex(password)));

        System.out.println(password); // 19b5818d7fe851f4510715cbf2193204df3f5b82808a84faaa02634752886ff2

    }

}
