package com.kar20240703.be.base.web.model.vo;

import com.kar20240703.be.base.web.model.domain.BaseMenuDO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseMenuInfoByIdVO extends BaseMenuDO {

    @Schema(description = "角色idSet")
    private Set<Long> roleIdSet;

}
