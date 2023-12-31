package com.zbbmeta.controller;

import cn.hutool.core.io.IoUtil;
import com.zbbmeta.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author springboot葵花宝典
 * @description: TODO
 */
@Controller
@RequestMapping("/generator")
public class GeneratorController {
    @Autowired
    private GeneratorService generatorService;

    /**
     * 生成代码
     */
    @RequestMapping("/code/zip")
    public void code(String tables, boolean isgeneratorzip, HttpServletResponse response) throws Exception {
        byte[] data = generatorService.generatorCodeZip(tables.split(","));

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"zbbmeta.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(),true,data);

    }


    /**
     * 生成代码
     */
    @RequestMapping("/code/file")
    public void codeFile(String tables, HttpServletResponse response) throws Exception {
        generatorService.generatorCodeFile(tables.split(","));

//        response.reset();
//        response.setHeader("Content-Disposition", "attachment; filename=\"zbbmeta.zip\"");
//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");
//
//        IoUtil.write(response.getOutputStream(),true,data);

    }
}
