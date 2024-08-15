package com.kar20240703.be.temp.generate.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Page;
import cn.hutool.json.JSONUtil;
import com.kar20240703.be.temp.generate.bo.BeApi;
import com.kar20240703.be.temp.generate.bo.BeApiParameter;
import com.kar20240703.be.temp.generate.bo.BeApiSchema;
import com.kar20240703.be.temp.generate.interfaces.BeApiField;
import com.kar20240703.be.temp.web.model.dto.MyOrderDTO;
import com.kar20240703.be.temp.web.model.vo.R;
import com.kar20240703.be.temp.web.util.CallBack;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * 生成 api的工具类
 */
@Slf4j
@Data
public class GenerateApiUtil {

    // 读取：接口的地址
    // public String SPRING_DOC_ENDPOINT = "http://43.154.37.130:10001/v3/api-docs/be";
    private String springDocEndpoint = "http://127.0.0.1:8002/v3/api-docs/be-base-web-20240703";

    private String systemUserDir = System.getProperty("user.dir"); // 例如：D:\GitHub\Kar-20240703

    private String apiPath = getSystemUserDir() + "/Kar-Fe/fe-base-20240703/src/api/http/base/";

    private String ts = ".ts";

    private String apiInterfaceTemp = "\nexport interface {} {\n{}\n}\n";

    private String apiInterfaceFieldTemp = "    {}{}: {}{} // {}";

    private String apiRequestTemp = "\n" + "// {}\n" + "export function {}({}config?: PureHttpRequestConfig) {\n"
        + "    return http.{}<{}>('{}', baseApi('{}'), {}, config)\n" + "}\n";

    private String apiRequestFormName = "form";

    private String apiRequestFormTemp = apiRequestFormName + ": {}, ";

    private String apiImportBase =
        "import { http } from \"@/utils/http\";\nimport { baseApi } from \"@/api/http/utils\";\n";

    private String apiImportBaseConfig = "import type { PureHttpRequestConfig } from \"@/utils/http/types\";\n";

    private String apiImportBaseMyOrderDTO = "import MyOrderDTO from \"@/model/dto/MyOrderDTO\";\n";

    private String undefined = "undefined";

    public String getSystemUserDir() {

        if (!systemUserDir.contains("Kar-20240703")) {
            return systemUserDir + "/Kar-20240703";
        }

        return systemUserDir;

    }

    public static void main(String[] args) {

        GenerateApiUtil generateApiUtil = new GenerateApiUtil();

        // 执行
        generateApiUtil.exec();

    }

    /**
     * 执行
     */
    public void exec() {

        HashMap<String, HashMap<String, BeApi>> apiMap = SpringDocUtil.get(getSpringDocEndpoint());

        System.out.println(JSONUtil.toJsonStr(apiMap));

        log.info("清除：api文件夹：{}", getApiPath());
        FileUtil.del(getApiPath());

        log.info("生成：api文件夹：执行开始 =====================>");
        long startTs = System.currentTimeMillis();

        StrBuilder strBuilder = StrBuilder.create();

        for (Map.Entry<String, HashMap<String, BeApi>> item : apiMap.entrySet()) {

            // 生成：api文件
            File apiFile = FileUtil.touch(getApiPath() + item.getKey() + "Controller" + getTs());

            // 要导入的基础内容
            strBuilder.append(getApiImportBase());

            strBuilder.append(getApiImportBaseConfig());

            Set<String> classNameSet = new HashSet<>(); // 防止重复写入，类名 set

            for (BeApi subItem : item.getValue().values()) {

                if (CollUtil.newArrayList("/base/file/upload").contains(subItem.getPath())) {
                    continue;
                }

                // 处理
                handler(subItem, strBuilder, classNameSet);

            }

            String content = strBuilder.toStringAndReset();

            FileUtil.writeUtf8String(content, apiFile); // 写入文件里

        }

        log.info("生成：api文件夹：执行结束 =====================> 耗时：{}毫秒", System.currentTimeMillis() - startTs);

    }

    /**
     * 处理
     */
    public void handler(BeApi beApi, StrBuilder strBuilder, Set<String> classNameSet) {

        // 生成 dto
        generateDTO(beApi, strBuilder, classNameSet);

        // 生成 api的时候，是否使用：myProPagePost
        CallBack<Boolean> myProPagePostCallBack = new CallBack<>(false);

        // 生成 vo
        generateVO(beApi, strBuilder, classNameSet, myProPagePostCallBack);

        // 生成 api
        generateApi(beApi, strBuilder, classNameSet, myProPagePostCallBack);

    }

    /**
     * 生成 api
     */
    public void generateApi(BeApi beApi, StrBuilder strBuilder, Set<String> classNameSet,
        CallBack<Boolean> myProPagePostCallBack) {

        // api的方法名
        String apiName = getApiName(beApi.getPath());

        String formStr = ""; // 拼接 form参数
        String formValueStr = getUndefined(); // 拼接 form值

        Map<String, BeApiField> parameter = beApi.getParameter();

        if (beApi.getRequestBody() != null && StrUtil.isNotBlank(beApi.getRequestBody().getClassName())) {

            formStr = StrUtil.format(getApiRequestFormTemp(), beApi.getRequestBody().getClassName());
            formValueStr = getApiRequestFormName();

        } else if (CollUtil.isNotEmpty(parameter)) {

            // 备注：这里只获取一个参数
            BeApiField beApiField = parameter.get(CollUtil.getFirst(parameter.keySet()));

            // 如果是：对象类型
            if (beApiField instanceof BeApiSchema) {

                BeApiSchema parameterBeApiSchema = (BeApiSchema)beApiField;

                formStr = StrUtil.format(getApiRequestFormTemp(), parameterBeApiSchema.getClassName());
                formValueStr = getApiRequestFormName();

            }

        }

        String returnTypeStr = beApi.getReturnTypeStr(); // 返回的类型

        if (StrUtil.isBlank(returnTypeStr)) {
            returnTypeStr = "void";
        }

        String httpStr; // 请求的类型

        //        if (beApi.getPath().contains("infoById")) {
        //
        //            httpStr = "myProPost";
        //
        //        } else if (myProPagePostCallBack.getValue()) {
        //
        //            httpStr = "myProPagePost";
        //
        //        } else if (beApi.getPath().contains("tree")) {
        //
        //            httpStr = "myProTreePost";
        //
        //        } else {

        httpStr = "request";

        if (BooleanUtil.isTrue(beApi.getReturnTypeArrFlag())) {
            returnTypeStr = returnTypeStr + "[]";
        }

        //        }

        strBuilder.append(
            StrUtil.format(getApiRequestTemp(), beApi.getSummary(), apiName, formStr, httpStr, returnTypeStr,
                beApi.getMethod(), beApi.getPath(), formValueStr));

    }

    /**
     * 通过：path，获取：api的方法名
     */
    @NotNull
    public static String getApiName(String path) {

        List<String> splitTrimList = StrUtil.splitTrim(path, CharPool.SLASH);

        return splitTrimList.stream().reduce((x, y) -> StrUtil.upperFirst(x) + StrUtil.upperFirst(y)).orElse("");

    }

    /**
     * 生成 vo
     */
    public void generateVO(BeApi beApi, StrBuilder strBuilder, Set<String> classNameSet,
        CallBack<Boolean> myProPagePostCallBack) {

        BeApiSchema response = (BeApiSchema)beApi.getResponse();

        if (response == null) {
            return;
        }

        boolean rFlag = response.getClassName().startsWith(R.class.getSimpleName());

        if (rFlag) {

            BeApiSchema beApiSchema = (BeApiSchema)response.getFieldMap().get(response.getClassName());

            BeApiField data = beApiSchema.getFieldMap().get("data");

            if (data instanceof BeApiSchema) {

                BeApiSchema dataBeApiSchema = (BeApiSchema)data;

                beApi.setReturnTypeArrFlag(dataBeApiSchema.getArrFlag());

                BeApiSchema dataRealBeApiSchema =
                    (BeApiSchema)dataBeApiSchema.getFieldMap().get(dataBeApiSchema.getClassName());

                // 如果是：分页排序查询相关
                if (dataRealBeApiSchema.getClassName().startsWith(Page.class.getSimpleName())) {

                    BeApiSchema records = (BeApiSchema)dataRealBeApiSchema.getFieldMap().get("records");

                    BeApiSchema recordsBeApiSchema = (BeApiSchema)records.getFieldMap().get(records.getClassName());

                    beApi.setReturnTypeStr(recordsBeApiSchema.getClassName());

                    // 生成：interface
                    generateInterface(strBuilder, classNameSet, recordsBeApiSchema, "vo-page：");

                    myProPagePostCallBack.setValue(true); // 设置：回调值

                } else {

                    beApi.setReturnTypeStr(dataRealBeApiSchema.getClassName());

                    // 生成：interface
                    generateInterface(strBuilder, classNameSet, dataRealBeApiSchema, "vo：");

                }

            } else if (data instanceof BeApiParameter) {

                BeApiParameter dataBeApiParameter = (BeApiParameter)data;

                String type = dataBeApiParameter.getType();

                // 处理：integer类型
                type = handleIntegerType(dataBeApiParameter, type);

                beApi.setReturnTypeStr(type);

                beApi.setReturnTypeArrFlag(dataBeApiParameter.getArrFlag());

            }

        } else {

            log.info("暂不支持其他类型的 vo：{}，name：{}", beApi.getPath(), response.getName());

        }

    }

    /**
     * 生成 dto
     */
    public void generateDTO(BeApi beApi, StrBuilder strBuilder, Set<String> classNameSet) {

        BeApiSchema requestBody = null;

        Map<String, BeApiField> parameter = beApi.getParameter();

        if (CollUtil.isNotEmpty(parameter)) {

            // 备注：这里只获取一个参数
            BeApiField beApiField = parameter.get(CollUtil.getFirst(parameter.keySet()));

            // 如果是：对象类型
            if (beApiField instanceof BeApiSchema) {

                BeApiSchema parameterBeApiSchema = (BeApiSchema)beApiField;

                requestBody = (BeApiSchema)parameterBeApiSchema.getFieldMap().get(parameterBeApiSchema.getClassName());

            }

        } else {

            requestBody = beApi.getRequestBody();

        }

        if (requestBody == null) {

            log.info("处理失败，requestBody是 null，path：{}", beApi.getPath());
            return;

        }

        String objectStr = "object";

        if (BooleanUtil.isFalse(objectStr.equals(requestBody.getType()))) {

            log.info("处理失败，requestBody不是 object类型，path：{}", beApi.getPath());
            return;

        }

        // 生成：interface
        generateInterface(strBuilder, classNameSet, requestBody, "dto：");

    }

    /**
     * 生成：interface
     */
    public void generateInterface(StrBuilder strBuilder, Set<String> classNameSet, BeApiSchema beApiSchema,
        String preMsg) {

        String className = beApiSchema.getClassName();

        if (classNameSet.contains(className)) {
            return;
        }

        classNameSet.add(className);

        // 所有字段的 StrBuilder
        StrBuilder interfaceBuilder = StrBuilder.create();

        int index = 0;

        if (CollUtil.isEmpty(beApiSchema.getFieldMap())) {

            log.info("处理失败，fieldMap是空，preMsg：{}，className：{}", preMsg, className);
            return;

        }

        for (Map.Entry<String, BeApiField> item : beApiSchema.getFieldMap().entrySet()) {

            // 一个字段
            BeApiField beApiField = item.getValue();

            if (beApiField instanceof BeApiParameter) {

                // 生成 interface，BeApiParameter类型
                generateInterfaceParameter(interfaceBuilder, item.getKey(), (BeApiParameter)beApiField);

            } else if (beApiField instanceof BeApiSchema) {

                // 生成 interface，BeApiSchema类型
                generateInterfaceSchema(strBuilder, classNameSet, interfaceBuilder, (BeApiSchema)beApiField);

            }

            if (index != beApiSchema.getFieldMap().size() - 1) {

                interfaceBuilder.append("\n");

            }

            index++;

        }

        strBuilder.append(StrUtil.format(getApiInterfaceTemp(), className, interfaceBuilder.toString()));

    }

    /**
     * 生成 interface，BeApiSchema类型
     */
    public void generateInterfaceSchema(StrBuilder strBuilder, Set<String> classNameSet, StrBuilder interfaceBuilder,
        BeApiSchema beApiSchema) {

        // 如果是：排序字段
        if (beApiSchema.getClassName().equals(MyOrderDTO.class.getSimpleName())) {

            if (BooleanUtil.isFalse(classNameSet.contains(beApiSchema.getClassName()))) {

                classNameSet.add(beApiSchema.getClassName());

                strBuilder.insert(0, getApiImportBaseMyOrderDTO()); // 在顶部添加导入

            }

            interfaceBuilder.append(
                StrUtil.format(getApiInterfaceFieldTemp(), beApiSchema.getName(), "?", beApiSchema.getClassName(), "",
                    "排序字段"));

        } else {

            // 如果：没有该对象的 ts类，则生成一个
            if (BooleanUtil.isFalse(classNameSet.contains(beApiSchema.getClassName()))) {

                BeApiField beApiField = beApiSchema.getFieldMap().get(beApiSchema.getClassName());

                if (beApiField instanceof BeApiSchema) {

                    // 生成：interface
                    generateInterface(strBuilder, classNameSet, (BeApiSchema)beApiField, "dto：内部类");

                }

            }

            interfaceBuilder.append(
                StrUtil.format(getApiInterfaceFieldTemp(), beApiSchema.getName(), "?", beApiSchema.getClassName(),
                    BooleanUtil.isTrue(beApiSchema.getArrFlag()) ? "[]" : "", beApiSchema.getDescription()));

        }

    }

    /**
     * 生成 interface，BeApiParameter类型
     */
    public void generateInterfaceParameter(StrBuilder interfaceBuilder, String fieldName,
        BeApiParameter beApiParameter) {

        String type = beApiParameter.getType();

        // 处理：integer类型
        type = handleIntegerType(beApiParameter, type);

        interfaceBuilder.append(StrUtil.format(getApiInterfaceFieldTemp(), fieldName, "?", type,
            BooleanUtil.isTrue(beApiParameter.getArrFlag()) ? "[]" : "", beApiParameter.getDescription()));

        if (StrUtil.isNotBlank(beApiParameter.getPattern())) {

            interfaceBuilder.append("，正则表达式：").append(beApiParameter.getPattern());

        }

        if (beApiParameter.getMaxLength() != null) {

            interfaceBuilder.append("，maxLength：").append(beApiParameter.getMaxLength());

        }

        if (beApiParameter.getMinLength() != null) {

            interfaceBuilder.append("，minLength：").append(beApiParameter.getMinLength());

        }

        if (BooleanUtil.isTrue(beApiParameter.getRequired())) {

            interfaceBuilder.append("，required：").append(beApiParameter.getRequired());

        }

        if (StrUtil.isNotBlank(beApiParameter.getFormat())) {

            interfaceBuilder.append("，format：").append(beApiParameter.getFormat());

        }

    }

    /**
     * 处理：integer类型
     */
    public String handleIntegerType(BeApiParameter beApiParameter, String type) {

        String integerStr = "integer";
        String formatInt64 = "int64";

        if (integerStr.equals(type)) {

            if (formatInt64.equals(beApiParameter.getFormat())) {

                type = "string"; // long 转换为 string

            } else {

                type = "number";

            }

        }

        return type;

    }

}
