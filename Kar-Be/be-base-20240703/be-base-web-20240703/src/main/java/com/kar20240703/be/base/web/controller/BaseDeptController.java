package com.kar20240703.be.base.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240703.be.base.web.model.domain.BaseDeptDO;
import com.kar20240703.be.base.web.model.dto.BaseDeptInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseDeptPageDTO;
import com.kar20240703.be.base.web.model.vo.BaseDeptInfoByIdVO;
import com.kar20240703.be.base.web.service.BaseDeptService;
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
@RequestMapping(value = "/base/dept")
@Tag(name = "基础-部门-管理")
public class BaseDeptController {

    @Resource
    BaseDeptService baseService;

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseDept:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseDeptInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseDept:page')")
    public R<Page<BaseDeptDO>> myPage(@RequestBody @Valid BaseDeptPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "查询：树结构")
    @PostMapping("/tree")
    @PreAuthorize("hasAuthority('baseDept:page')")
    public R<List<BaseDeptDO>> tree(@RequestBody @Valid BaseDeptPageDTO dto) {
        return R.okData(baseService.tree(dto));
    }

    @Operation(summary = "下拉树形列表")
    @PostMapping("/dictTreeList")
    @PreAuthorize("hasAuthority('baseDept:dictList')")
    public R<List<BaseDeptDO>> dictTreeList() {
        return R.okData(baseService.dictTreeList());
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseDept:infoById')")
    public R<BaseDeptInfoByIdVO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "批量删除")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseDept:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

}
