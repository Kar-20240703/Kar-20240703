package com.kar20240703.be.temp.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotNullId {

    @Min(1)
    @NotNull
    @Schema(description = "主键 id")
    private Long id;

}
