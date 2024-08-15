package com.kar20240703.be.temp.web.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotEmptyIdSet {

    @NotEmpty
    @Schema(description = "主键 idSet")
    private Set<Long> idSet;

}
