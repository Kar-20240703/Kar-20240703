package com.kar20240703.be.temp.web.configuration.mybatisplus;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kar20240703.be.temp.web.util.MyUserUtil;
import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MetaObjectHandlerConfiguration implements MetaObjectHandler {

    /**
     * 新增时
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        Date date = new Date();

        Long currentUserIdDefault = MyUserUtil.getCurrentUserIdDefault();

        // 实体类有值时，这里不会生效
        strictInsertFill(metaObject, "createId", Long.class, currentUserIdDefault);
        strictInsertFill(metaObject, "createTime", Date.class, date);

        strictInsertFill(metaObject, "updateId", Long.class, currentUserIdDefault);
        strictInsertFill(metaObject, "updateTime", Date.class, date);

        strictInsertFill(metaObject, "uuid", String.class, IdUtil.simpleUUID());

    }

    /**
     * 修改时
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        Date date = new Date();

        Long currentUserIdDefault = MyUserUtil.getCurrentUserIdDefault();

        // 实体类有值时，这里不会生效
        strictUpdateFill(metaObject, "updateTime", Date.class, date);
        strictUpdateFill(metaObject, "updateId", Long.class, currentUserIdDefault);

    }

}
