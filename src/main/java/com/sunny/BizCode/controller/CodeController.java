package com.sunny.BizCode.controller;

import com.sunny.BizCode.Service.GeneratorService;
import com.sunny.BizCode.entity.Table;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@RequestMapping("/code")
@Controller
public class CodeController {
    @Resource
    private GeneratorService generatorService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/generator")
    public Object generator(@RequestBody Table table) throws Exception {
        Integer result = generatorService.generator(table);
        if (result == 1) {
            return "sucess";
        } else {
            return "fail";
        }

    }
}
