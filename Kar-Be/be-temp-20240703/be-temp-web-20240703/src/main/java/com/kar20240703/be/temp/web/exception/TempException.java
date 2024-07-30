package com.kar20240703.be.temp.web.exception;

import cn.hutool.json.JSONUtil;
import com.kar20240703.be.temp.web.model.vo.R;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TempException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private R<?> r;

    public TempException(R<?> r) {

        super(JSONUtil.toJsonStr(r)); // 把信息封装成json格式

        setR(r);

    }

}
