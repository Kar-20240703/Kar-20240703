package com.kar20240703.be.base.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240703.be.base.web.model.domain.BaseAreaDO;
import com.kar20240703.be.base.web.model.dto.BaseAreaInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseAreaPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseAreaInfoByIdVO;
import com.kar20240703.be.base.web.service.BaseAreaService;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
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
@RequestMapping(value = "/base/area")
@Tag(name = "基础-区域-管理")
public class BaseAreaController {

    @Resource
    BaseAreaService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseArea:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseAreaInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseArea:page')")
    public R<Page<BaseAreaDO>> myPage(@RequestBody @Valid BaseAreaPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "查询：树结构")
    @PostMapping("/tree")
    @PreAuthorize("hasAuthority('baseArea:page')")
    public R<List<BaseAreaDO>> tree(@RequestBody @Valid BaseAreaPageDTO dto) {
        return R.okData(baseService.tree(dto));
    }

    @Operation(summary = "下拉树形列表")
    @PostMapping("/dictTreeList")
    @PreAuthorize("hasAuthority('baseArea:dictList')")
    public R<List<BaseAreaDO>> dictTreeList() {
        return R.okData(baseService.dictTreeList());
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseArea:infoById')")
    public R<BaseAreaInfoByIdVO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseArea:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "通过主键 idSet，加减排序号")
    @PostMapping("/addOrderNo")
    @PreAuthorize("hasAuthority('baseArea:insertOrUpdate')")
    public R<String> addOrderNo(@RequestBody @Valid ChangeNumberDTO dto) {
        return R.okMsg(baseService.addOrderNo(dto));
    }

    @Operation(summary = "通过主键 idSet，修改排序号")
    @PostMapping("/updateOrderNo")
    @PreAuthorize("hasAuthority('baseArea:insertOrUpdate')")
    public R<String> updateOrderNo(@RequestBody @Valid ChangeNumberDTO dto) {
        return R.okMsg(baseService.updateOrderNo(dto));
    }

}
