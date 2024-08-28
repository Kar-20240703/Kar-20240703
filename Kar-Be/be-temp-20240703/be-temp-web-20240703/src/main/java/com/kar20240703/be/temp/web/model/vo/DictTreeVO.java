package com.kar20240703.be.temp.web.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictTreeVO extends DictVO {

    @Schema(description = "父级id")
    private Long pid;

    public DictTreeVO() {
    }

    public DictTreeVO(Long id, String name, Long pid) {
        super(id, name);
        this.pid = pid;
    }

}
