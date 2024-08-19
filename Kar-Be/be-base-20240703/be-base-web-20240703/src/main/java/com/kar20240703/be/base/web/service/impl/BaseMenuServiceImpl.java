package com.kar20240703.be.base.web.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kar20240703.be.base.web.mapper.BaseMenuMapper;
import com.kar20240703.be.base.web.model.domain.BaseMenuDO;
import com.kar20240703.be.base.web.model.dto.BaseMenuInsertOrUpdateDTO;
import com.kar20240703.be.base.web.model.dto.BaseMenuPageDTO;
import com.kar20240703.be.base.web.service.BaseMenuRefUserService;
import com.kar20240703.be.base.web.service.BaseMenuService;
import com.kar20240703.be.base.web.service.BaseRoleRefMenuService;
import com.kar20240703.be.base.web.service.BaseRoleRefUserService;
import com.kar20240703.be.temp.web.model.dto.ChangeNumberDTO;
import com.kar20240703.be.temp.web.model.dto.NotEmptyIdSet;
import com.kar20240703.be.temp.web.model.dto.NotNullId;
import com.kar20240703.be.temp.web.util.UserUtil;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BaseMenuServiceImpl extends ServiceImpl<BaseMenuMapper, BaseMenuDO> implements BaseMenuService {

    @Resource
    BaseRoleRefMenuService baseRoleRefMenuService;

    @Resource
    BaseRoleRefUserService baseRoleRefUserService;

    @Resource
    BaseMenuRefUserService baseMenuRefUserService;

    /**
     * 新增/修改
     */
    @Override
    public String insertOrUpdate(BaseMenuInsertOrUpdateDTO dto) {

        return "";

    }

    /**
     * 分页排序查询
     */
    @Override
    public Page<BaseMenuDO> myPage(BaseMenuPageDTO dto) {
        return null;
    }

    /**
     * 查询：树结构
     */
    @Override
    public List<BaseMenuDO> tree(BaseMenuPageDTO dto) {
        return Collections.emptyList();
    }

    /**
     * 通过主键id，查看详情
     */
    @Override
    public BaseMenuDO infoById(NotNullId notNullId) {
        return null;
    }

    /**
     * 批量删除
     */
    @Override
    public String deleteByIdSet(NotEmptyIdSet notEmptyIdSet, boolean checkChildrenFlag, boolean checkDeleteFlag) {
        return "";
    }

    /**
     * 获取：当前用户绑定的菜单
     */
    @Override
    public List<BaseMenuDO> userSelfMenuList() {

        Long userId = UserUtil.getCurrentUserId();

        if (UserUtil.getCurrentUserAdminFlag(userId)) {

            userId = null;

        }

        return baseMapper.getMenuListByUserId(userId);

    }

    /**
     * 通过主键 idSet，加减排序号
     */
    @Override
    public String addOrderNo(ChangeNumberDTO dto) {
        return "";
    }

}
