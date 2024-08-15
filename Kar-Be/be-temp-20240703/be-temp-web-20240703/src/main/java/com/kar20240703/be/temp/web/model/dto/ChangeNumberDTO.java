package com.kar20240703.be.temp.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChangeNumberDTO extends NotEmptyIdSet {

    @NotNull
    @Schema(description = "需要改变的数值")
    private Long number;

}
