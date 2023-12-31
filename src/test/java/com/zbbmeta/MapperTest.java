package com.zbbmeta;

import com.zbbmeta.mapper.GeneratorMapper;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @author springboot葵花宝典
 * @description: TODO
 */
@SpringBootTest
public class MapperTest {

    @Autowired
    GeneratorMapper generatorMapper;

    /**
     * 获取配置信息
     */
    @Test
    public  void getConfig() throws Exception {
        try {
            PropertiesConfiguration configuration = new PropertiesConfiguration("typeconverter.properties");
            String nvarchar = configuration.getString("nvarchar");
            System.out.println("nvarchar = " + nvarchar);
        } catch ( ConfigurationException e) {
            throw new Exception("获取配置文件失败，", e);
        }
    }


    /**
     * 模糊匹配查询表列表
     */
    @Test
    public void queryTableListTest(){

        List<Map<String, Object>> maps = generatorMapper.queryTableList("pe");

        maps.forEach(map->{
            System.out.println("=============================================================");
            map.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
            System.out.println("=============================================================");
        });
    }


    /**
     * 获取表信息
     */
    @Test
    public void queryTableTest(){

        Map<String, String> map = generatorMapper.queryTable("pe_user");


        System.out.println(map);



    }


    /**
     * 获取列信息
     */
    @Test
    public void queryColumnsTest(){

        List<Map<String, String>> maps = generatorMapper.queryColumns("pe_user");

        maps.forEach(map->{
            System.out.println("=============================================================");
            map.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
            System.out.println("=============================================================");
        });




    }

    @Test
    public void generatorCodeTest() throws Exception {

        Map<String, String> tableMap = generatorMapper.queryTable("pe_user");
        List<Map<String, String>> columnsMap = generatorMapper.queryColumns("pe_user");
//        GenUtils.generatorCode(tableMap,columnsMap);


    }




}
