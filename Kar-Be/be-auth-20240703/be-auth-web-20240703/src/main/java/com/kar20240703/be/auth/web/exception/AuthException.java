package com.kar20240703.be.auth.web.exception;

import cn.hutool.json.JSONUtil;
import com.kar20240703.be.auth.web.model.vo.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private R<?> r;

    public AuthException(R<?> r) {

        super(JSONUtil.toJsonStr(r)); // 把信息封装成json格式

        setR(r);

    }

}
