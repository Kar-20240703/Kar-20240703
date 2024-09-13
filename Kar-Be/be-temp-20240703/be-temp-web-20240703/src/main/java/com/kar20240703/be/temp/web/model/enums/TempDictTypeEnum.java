package com.kar20240703.be.temp.web.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字典类型
 */
@AllArgsConstructor
@Getter
public enum TempDictTypeEnum {

    DICT(1), // 字典

    DICT_ITEM(2), // 字典项

    ;

    @EnumValue
    @JsonValue
    private final int code;

}
