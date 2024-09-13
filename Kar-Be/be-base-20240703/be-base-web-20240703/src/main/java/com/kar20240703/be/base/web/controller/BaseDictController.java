package com.kar20240703.be.base.web.controller;

import com.kar20240703.be.base.web.service.BaseDictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/base/dict")
@Tag(name = "基础-字典-管理")
public class BaseDictController {

    @Resource
    BaseDictService baseService;

}
