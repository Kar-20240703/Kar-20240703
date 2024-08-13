package com.kar20240703.be.base.web.util;

import cn.hutool.core.lang.func.VoidFunc0;
import java.util.function.Supplier;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

@Component
public class TransactionUtil {

    private static DataSourceTransactionManager dataSourceTransactionManager;

    private static TransactionDefinition transactionDefinition;

    @Resource
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        TransactionUtil.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    @Resource
    public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
        TransactionUtil.transactionDefinition = transactionDefinition;
    }

    /**
     * 携带事务，执行方法
     */
    @SneakyThrows
    public static void exec(VoidFunc0 voidFunc0) {

        // 开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);

        try {

            voidFunc0.call();

            dataSourceTransactionManager.commit(transactionStatus); // 提交

        } catch (Exception e) {

            dataSourceTransactionManager.rollback(transactionStatus); // 回滚
            throw e;

        }

    }

    /**
     * 携带事务，执行方法
     */
    public static <T> T exec(Supplier<T> supplier) {

        // 开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);

        try {

            T resObject = supplier.get();

            dataSourceTransactionManager.commit(transactionStatus); // 提交

            return resObject;

        } catch (Exception e) {

            dataSourceTransactionManager.rollback(transactionStatus); // 回滚
            throw e;

        }

    }

}
