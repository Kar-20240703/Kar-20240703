package com.kar20240703.be.base.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240703.be.base.web.model.dto.BaseDictInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseDictListByDictKeyDTO;
import com.kar20240703.be.base.web.model.dto.BaseDictPageDTO;
import com.kar20240703.be.base.web.service.BaseDictService;
import com.kar20240703.be.temp.web.model.domain.TempDictDO;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.DictIntegerVO;
import com.kar20240703.be.temp.web.model.vo.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/dict")
@Tag(name = "基础-字典-管理")
public class BaseDictController {

    @Resource
    BaseDictService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseDict:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseDictInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseDict:page')")
    public R<Page<TempDictDO>> myPage(@RequestBody @Valid BaseDictPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "通过：dictKey获取字典项集合，备注：会进行缓存")
    @PostMapping("/listByDictKey")
    public R<List<DictIntegerVO>> listByDictKey(@RequestBody @Valid BaseDictListByDictKeyDTO dto) {
        return R.okData(baseService.listByDictKey(dto));
    }

    @Operation(summary = "查询：树结构")
    @PostMapping("/tree")
    @PreAuthorize("hasAuthority('baseDict:page')")
    public R<List<TempDictDO>> tree(@RequestBody @Valid BaseDictPageDTO dto) {
        return R.okData(baseService.tree(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseDict:infoById')")
    public R<TempDictDO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseDict:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet, true));
    }

    @Operation(summary = "通过主键 idSet，加减排序号")
    @PostMapping("/addOrderNo")
    @PreAuthorize("hasAuthority('baseDict:insertOrUpdate')")
    public R<String> addOrderNo(@RequestBody @Valid ChangeNumberDTO dto) {
        return R.okMsg(baseService.addOrderNo(dto));
    }

    @Operation(summary = "通过主键 idSet，修改排序号")
    @PostMapping("/updateOrderNo")
    @PreAuthorize("hasAuthority('baseDict:insertOrUpdate')")
    public R<String> updateOrderNo(@RequestBody @Valid ChangeNumberDTO dto) {
        return R.okMsg(baseService.updateOrderNo(dto));
    }

}
