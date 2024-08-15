package com.kar20240703.be.temp.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDTO {

    @Schema(description = "排序的字段名")
    private String name;

    @Schema(description = "ascend（升序，默认） descend（降序）")
    private String value;

}
