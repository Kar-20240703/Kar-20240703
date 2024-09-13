package com.kar20240703.be.base.web.controller;

import com.kar20240703.be.base.web.service.BaseParamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/param")
@Tag(name = "基础-参数-管理")
public class BaseParamController {

    @Resource
    BaseParamService baseService;

}
