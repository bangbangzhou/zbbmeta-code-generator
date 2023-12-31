package com.zbbmeta.service.impl;

import cn.hutool.core.io.IoUtil;
import com.zbbmeta.mapper.GeneratorMapper;
import com.zbbmeta.service.GeneratorService;
import com.zbbmeta.utils.GenUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author springboot葵花宝典
 * @description: TODO
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {
    @Autowired
    private GeneratorMapper generatorMapper;


    @Override
    public List<Map<String, Object>> queryTableList(String map) {
        return generatorMapper.queryTableList(map);
    }

    @Override
    public Map<String, String> queryTable(String tableName) {
        return generatorMapper.queryTable(tableName);
    }

    @Override
    public List<Map<String, String>> queryColumns(String tableName) {
        return generatorMapper.queryColumns(tableName);
    }

    @Override
    public byte[] generatorCodeZip(String[] tableNames) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        //1.循环表名
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> tableInfo = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columnsMaps = queryColumns(tableName);
            //根据表和列信息代码生成
            GenUtils.generatorCodeZip(tableInfo, columnsMaps, zip);
        }
        IoUtil.close(zip);
        return outputStream.toByteArray();
    }

    @Override
    public void generatorCodeFile(String[] tableNames) throws Exception {

        //1.循环表名
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> tableInfo = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columnsMaps = queryColumns(tableName);
            //根据表和列信息代码生成
            GenUtils.generatorCodeFile(tableInfo, columnsMaps);
        }

    }
}
