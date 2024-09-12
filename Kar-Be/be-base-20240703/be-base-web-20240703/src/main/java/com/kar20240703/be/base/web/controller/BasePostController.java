package com.kar20240703.be.base.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240703.be.base.web.model.domain.BasePostDO;
import com.kar20240703.be.base.web.model.dto.BasePostInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BasePostPageDTO;
import com.kar20240703.be.base.web.model.vo.BasePostInfoByIdVO;
import com.kar20240703.be.base.web.service.BasePostService;
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
@RequestMapping(value = "/base/post")
@Tag(name = "基础-岗位-管理")
public class BasePostController {

    @Resource
    BasePostService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('basePost:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BasePostInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('basePost:page')")
    public R<Page<BasePostDO>> myPage(@RequestBody @Valid BasePostPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "查询：树结构")
    @PostMapping("/tree")
    @PreAuthorize("hasAuthority('basePost:page')")
    public R<List<BasePostDO>> tree(@RequestBody @Valid BasePostPageDTO dto) {
        return R.okData(baseService.tree(dto));
    }

    @Operation(summary = "下拉树形列表")
    @PostMapping("/dictTreeList")
    @PreAuthorize("hasAuthority('basePost:dictList')")
    public R<List<BasePostDO>> dictTreeList() {
        return R.okData(baseService.dictTreeList());
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('basePost:infoById')")
    public R<BasePostInfoByIdVO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('basePost:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "通过主键 idSet，加减排序号")
    @PostMapping("/addOrderNo")
    @PreAuthorize("hasAuthority('basePost:insertOrUpdate')")
    public R<String> addOrderNo(@RequestBody @Valid ChangeNumberDTO dto) {
        return R.okMsg(baseService.addOrderNo(dto));
    }

    @Operation(summary = "通过主键 idSet，修改排序号")
    @PostMapping("/updateOrderNo")
    @PreAuthorize("hasAuthority('basePost:insertOrUpdate')")
    public R<String> updateOrderNo(@RequestBody @Valid ChangeNumberDTO dto) {
        return R.okMsg(baseService.updateOrderNo(dto));
    }

}
