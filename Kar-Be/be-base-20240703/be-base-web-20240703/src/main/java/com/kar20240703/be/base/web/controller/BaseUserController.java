package com.kar20240703.be.base.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kar20240703.be.base.web.model.dto.BaseUserDictListDTO;
import com.kar20240703.be.base.web.model.dto.BaseUserInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseUserPageDTO;
import com.kar20240703.be.base.web.model.dto.BaseUserUpdatePasswordDTO;
import com.kar20240703.be.base.web.model.vo.BaseUserInfoByIdVO;
import com.kar20240703.be.base.web.model.vo.BaseUserPageVO;
import com.kar20240703.be.base.web.service.BaseUserService;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.model.vo.DictVO;
import com.kar20240703.be.temp.web.model.vo.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/user")
@Tag(name = "基础-用户-管理")
public class BaseUserController {

    @Resource
    BaseUserService baseService;

    @Operation(summary = "分页排序查询")
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('baseUser:page')")
    public R<Page<BaseUserPageVO>> myPage(@RequestBody @Valid BaseUserPageDTO dto) {
        return R.okData(baseService.myPage(dto));
    }

    @Operation(summary = "下拉列表")
    @PostMapping("/dictList")
    @PreAuthorize("hasAuthority('baseUser:dictList')")
    public R<Page<DictVO>> dictList(@RequestBody @Valid BaseUserDictListDTO dto) {
        return R.okData(baseService.dictList(dto));
    }

    @Operation(summary = "新增/修改")
    @PostMapping("/insertOrUpdate")
    @PreAuthorize("hasAuthority('baseUser:insertOrUpdate')")
    public R<String> insertOrUpdate(@RequestBody @Valid BaseUserInsertOrUpdateDTO dto) {
        return R.okMsg(baseService.insertOrUpdate(dto));
    }

    @Operation(summary = "通过主键id，查看详情")
    @PostMapping("/infoById")
    @PreAuthorize("hasAuthority('baseUser:infoById')")
    public R<BaseUserInfoByIdVO> infoById(@RequestBody @Valid NotNullId notNullId) {
        return R.okData(baseService.infoById(notNullId));
    }

    @Operation(summary = "是否允许后台登录")
    @PostMapping("/manageSignInFlag")
    public R<Boolean> manageSignInFlag() {
        return R.okData(baseService.manageSignInFlag());
    }

    @Operation(summary = "批量：注销用户")
    @PostMapping("/deleteByIdSet")
    @PreAuthorize("hasAuthority('baseUser:deleteByIdSet')")
    public R<String> deleteByIdSet(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.deleteByIdSet(notEmptyIdSet));
    }

    @Operation(summary = "批量：重置头像")
    @PostMapping("/resetAvatar")
    @PreAuthorize("hasAuthority('baseUser:insertOrUpdate')")
    public R<String> resetAvatar(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.resetAvatar(notEmptyIdSet));
    }

    @Operation(summary = "批量：修改密码")
    @PostMapping("/updatePassword")
    @PreAuthorize("hasAuthority('baseUser:insertOrUpdate')")
    public R<String> updatePassword(@RequestBody @Valid BaseUserUpdatePasswordDTO dto) {
        return R.okMsg(baseService.updatePassword(dto));
    }

    @Operation(summary = "批量：解冻")
    @PostMapping("/thaw")
    @PreAuthorize("hasAuthority('baseUser:insertOrUpdate')")
    public R<String> thaw(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.thaw(notEmptyIdSet));
    }

    @Operation(summary = "批量：冻结")
    @PostMapping("/freeze")
    @PreAuthorize("hasAuthority('baseUser:insertOrUpdate')")
    public R<String> freeze(@RequestBody @Valid NotEmptyIdSet notEmptyIdSet) {
        return R.okMsg(baseService.freeze(notEmptyIdSet));
    }

}
