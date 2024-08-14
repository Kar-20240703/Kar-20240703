package com.kar20240703.be.base.web.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.kar20240703.be.temp.web.exception.TempBizCodeEnum;
import com.kar20240703.be.temp.web.model.constant.TempConstant;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.SeparatorUtil;
import org.springframework.stereotype.Component;

@Component
public class MyRsaUtil {

    public static final int EXPIRE_TIME = TempConstant.HOUR_3_EXPIRE_TIME;

    public static final String SEPARATOR = SeparatorUtil.SEMICOLON_SEPARATOR;

    public static final String PRIVATE_KEY =
        "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM5EwQFY/x+xH5YaZlz1mX+f0BwHDnn0sjH7Ar1BwbCrtTGRVA8LEaSOp4qbk6k1NI++wBOj9ghyBtnQeb+skyCDV1iDANiqLV6hhoKA6Wt7WcCiBvNDA5cZVaEjmY9qFxNJ7L3CEmJohNaf1NkvuJeh6UlfngUOImn/hGqGebtNAgMBAAECgYAo03PuG9rXP3wUfiIvmQ7qM0wCGCV13whcGyYWJos7OCkzZ+Pe3F6AFxozNYra7WR7ZRJocMX0MvmHKBvI49P0uoqDQcE/kxFHQv6jRaI5ZWbhDapHMwhBMwgDmQr/BcuCg+FF2UmGeXWQmEtB8Ss+mMBN8qrzT2g/dxN/sGf+8QJBAP5nTMdQi826WJQ3YdBZwviXFHR0XRJz3SEuJ42oeknLYprrfMHHXy/PDwCPbUTewKtLL+rkFGqBzWRtjJSgqwkCQQDPkCAEG7bnbNmK+jND78efFd5neGFpH97ZeJXqPYlumI3aPWdG8GG0tTniUNqJZyGc/YvuxeuAOHrAvtnXcqslAkEAi8PqzZG1zOe0pHEsvs+hqvstlLEdNPcMpJ77wfqufH5NhPkdCETlSZnDDza8/Fo4laLwB2cYxE7drW/2DO2cKQJBAM7H3L17BsqjrE5CWJRT6uFYHgui6BicwZyR/3gu+h2OKbPD/IG1tQEckqAewLimX9xQ0/l6f9Vrbw9akxeKi2kCQGA1Z1dUHjXZHSuHRFC7nUyyRQhlbhrebDriTBytLFI5G17GwwqKOr4nkcfT4sl+tlkEaXLeDKflwkC3ZEAacbY=";

    public static final String PUBLIC_KEY =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDORMEBWP8fsR+WGmZc9Zl/n9AcBw559LIx+wK9QcGwq7UxkVQPCxGkjqeKm5OpNTSPvsATo/YIcgbZ0Hm/rJMgg1dYgwDYqi1eoYaCgOlre1nAogbzQwOXGVWhI5mPahcTSey9whJiaITWn9TZL7iXoelJX54FDiJp/4Rqhnm7TQIDAQAB";

    /**
     * 非对称：解密
     */
    public static String rsaDecrypt(String str) {

        return rsaDecrypt(str, PRIVATE_KEY); // 返回解密之后的 字符串

    }

    /**
     * 非对称：解密
     */
    public static String rsaDecrypt(String str, String privateKey) {

        if (StrUtil.isBlank(str)) {

            R.error(TempBizCodeEnum.PARAMETER_CHECK_ERROR);

        }

        if (StrUtil.isBlank(privateKey)) {

            R.sysError();

        }

        RSA rsa = new RSA(privateKey, null);

        String decryptStr = null;

        try {

            decryptStr = rsa.decryptStr(str, KeyType.PrivateKey);

        } catch (Exception e) {

            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);

        }

        if (StrUtil.isBlank(decryptStr)) {
            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);
        }

        return decryptStr; // 返回解密之后的 字符串

    }

    /**
     * 非对称：加密
     */
    public static String rsaEncrypt(String str) {

        return rsaEncrypt(str, PUBLIC_KEY); // 返回解密之后的 字符串

    }

    /**
     * 非对称：加密
     */
    public static String rsaEncrypt(String str, String publicKey) {

        if (StrUtil.isBlank(str)) {

            R.error(TempBizCodeEnum.PARAMETER_CHECK_ERROR);

        }

        if (StrUtil.isBlank(publicKey)) {

            R.sysError();

        }

        RSA rsa = new RSA(null, publicKey);

        String encryptStr = null;

        str = str + SEPARATOR + System.currentTimeMillis(); // 需要加上当前时间戳

        try {

            encryptStr = rsa.encryptBase64(str, KeyType.PublicKey);

        } catch (CryptoException e) {

            R.error(TempBizCodeEnum.ILLEGAL_REQUEST);

        }

        return encryptStr; // 返回加密之后的 字符串

    }

}
