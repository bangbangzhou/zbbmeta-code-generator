package com.zbbmeta.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.zbbmeta.entity.Column;
import com.zbbmeta.entity.Table;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Template;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author springboot葵花宝典
 * @description: TODO
 */
public class GenUtils {


    /**
     * 获取配置信息
     */
    public static Configuration getConfig() throws Exception {
        try {
            return new PropertiesConfiguration("typeconverter.properties");
        } catch ( ConfigurationException e) {
            throw new Exception("获取配置文件失败，", e);
        }
    }


    public static void generatorCodeFile(Map<String, String> tableInfo,
                                        List<Map<String, String>> columns
                                         ) throws Exception {
        //配置信息
        Configuration config = getConfig();
        //判断BigDecimal类型
        boolean hasBigDecimal = false;
        //判断list
        boolean hasList = false;
        //表信息
       Table table=  getTable( tableInfo, config);
        List<Column> columnArrayList = GenUtils.getColumnList(table, columns, config, hasBigDecimal, hasList);
        table.setColumns(columnArrayList);
        //没主键，则第一个字段为主键
        if (Objects.isNull(table.getPk())) {
            table.setPk(table.getColumns().get(0));
        }
        //设置freemarker资源加载器
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "com.zbbmeta" : mainPath;
        //封装模板数据
        Map<String, Object> map = GenUtils.getModelDataMap(table, config, mainPath, hasBigDecimal, hasList);
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        // 设置模板文件所在目录（可根据需要进行调整）

        //渲染模板
        //获取模板列表
        List<String> templatefiless = getTemplates();
        for (String templatefile : templatefiless) {
            //渲染模板
            StringWriter sw = new StringWriter();
            // 准备数据模型
            File file = ResourceUtils.getFile("classpath:template/");
            FileTemplateLoader ftl = new FileTemplateLoader(file);
            cfg.setTemplateLoader(ftl);
            // 加载模板

            Template template = cfg.getTemplate(templatefile);
            template.setOutputEncoding("utf-8");//指定生成文件的字符集编码
            String resultFile= getOutFileName(templatefile, table.getUpperFirstClassName(), config.getString("package"), config.getString("moduleName"));


            //将模板写入到文件
            File result = new File(resultFile);
            if(result.getParentFile() != null) {
                result.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(result);
            template.process(map, fw);
            fw.close();

        }
    }


    public static void generatorCodeZip(Map<String, String> tableInfo,
                                     List<Map<String, String>> columns,
                                     ZipOutputStream zip  ) throws Exception {
        //配置信息
        Configuration config = getConfig();


        //判断BigDecimal类型
        boolean hasBigDecimal = false;
        //判断list
        boolean hasList = false;
        //表信息
        Table table=  getTable( tableInfo, config);
        List<Column> columnArrayList = GenUtils.getColumnList(table, columns, config, hasBigDecimal, hasList);
        table.setColumns(columnArrayList);
        //没主键，则第一个字段为主键
        if (Objects.isNull(table.getPk())) {
            table.setPk(table.getColumns().get(0));
        }
        //设置freemarker资源加载器
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "com.zbbmeta" : mainPath;
        //封装模板数据
        Map<String, Object> map = GenUtils.getModelDataMap(table, config, mainPath, hasBigDecimal, hasList);
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        // 设置模板文件所在目录（可根据需要进行调整）

        //渲染模板
        //获取模板列表
        List<String> templatefiless = getTemplates();
        for (String templatefile : templatefiless) {
            //渲染模板
            StringWriter sw = new StringWriter();
            // 准备数据模型
            File file = ResourceUtils.getFile("classpath:templates/");
            FileTemplateLoader ftl = new FileTemplateLoader(file);
            cfg.setTemplateLoader(ftl);
            // 加载模板

            Template template = cfg.getTemplate(templatefile);
            template.setOutputEncoding("utf-8");//指定生成文件的字符集编码
            String resultFile= getOutFileName(templatefile, table.getUpperFirstClassName(), config.getString("package"), config.getString("moduleName"));

            template.process(map, sw);

                try {
                    //添加到zip
                    zip.putNextEntry(new ZipEntry(resultFile));

                    IoUtil.write(zip, false, sw.toString().getBytes(StandardCharsets.UTF_8));
                    IoUtil.close(sw);
                    zip.closeEntry();
                } catch (IOException e) {
                    throw new Exception("渲染模板失败，表名：" + table.getTableName(), e);
                }
        }
    }




    public static Table getTable(Map<String, String> tableInfo,Configuration config){


        //表信息
        Table table = new Table();
        table.setTableName(tableInfo.get("tableName"));
        table.setComments(tableInfo.get("tableComment"));
        //将表名转换为Java类名
        String className =   tableToJava(table.getTableName(), config.getStringArray("tablePrefix"));
        //类名(第一个字母大写)，如：sys_user => SysUser
        table.setUpperFirstClassName(StrUtil.upperFirst(className));
        //类名(第一个字母小写)，如：sys_user => sysUser
        table.setLowerFirstClassname(className);
        return  table;

    }

    public static List<Column> getColumnList(Table table, List<Map<String, String>> columns,Configuration config,  boolean hasBigDecimal,  boolean hasList){
        //列信息
        List<Column> columnArrayList = new ArrayList<>();
        for (Map<String, String> columnMap : columns) {
            Column column = new Column();
            //设置列名
            column.setColumnName(columnMap.get("columnName"));
            //设置Java类型
            column.setDataType(columnMap.get("dataType"));
            column.setComments(columnMap.get("columnComment"));
            column.setExtra(columnMap.get("extra"));
            //列名转换为Java类名称

            String attrName =StrUtil.toCamelCase(column.getColumnName());
            //属性名称(第一个字母小写)，如：user_name => userName
            column.setLowerFirstAttrName(attrName);
            //属性名称(第一个字母大写)，如：user_name => UserName
            column.setUpperFirstAttrName(StrUtil.upperFirst(attrName));
            //列的数据类型，转换成Java类型
            String attrType = config.getString(column.getDataType(), StrUtil.upperFirst(column.getDataType()));
            column.setAttrType(attrType);

            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            if (!hasList && "array".equals(column.getExtra())) {
                hasList = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(columnMap.get("columnKey")) && table.getPk() == null) {
                table.setPk(column);
            }
            columnArrayList.add(column);

        }
        return  columnArrayList;
    }

    public static  Map<String, Object>  getModelDataMap(Table table, Configuration config,String mainPath,  boolean hasBigDecimal,  boolean hasList){
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", table.getTableName());
        map.put("comments", table.getComments());
        map.put("pk", table.getPk());
        map.put("className", table.getUpperFirstClassName());
        map.put("classname", table.getLowerFirstClassname());
        map.put("pathName", table.getLowerFirstClassname().toLowerCase());
        map.put("columns", table.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("hasList", hasList);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(),  "yyyy-MM-dd HH:mm:ss"));

        return map;
    }

    /**
     * 表名转换成Java类名
     * @param tableName
     * @param tablePrefixArray
     * @return
     */
    public static String tableToJava(String tableName, String[] tablePrefixArray) {
        if (null != tablePrefixArray && tablePrefixArray.length > 0) {
            for (String tablePrefix : tablePrefixArray) {
                if (tableName.startsWith(tablePrefix)){
                    tableName = tableName.replaceFirst(tablePrefix, "");
                }
            }
        }


        return  StrUtil.toCamelCase(tableName);
    }



    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("Entity.java.ftl");
//        templates.add("Mapper.java.ftl");
//
//        templates.add("Mapper.xml.ftl");
//        templates.add("Service.java.ftl");
//        templates.add("ServiceImpl.java.ftl");
//        templates.add("Controller.java.ftl");
        return templates;
    }


    /**
     * 获取文件名
     */
    public static String getOutFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "src" +  File.separator+"main" + File.separator + "java" + File.separator;
        if (StrUtil.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }
        if (template.contains("Entity.java.ftl") ) {
            return packagePath + "entity" + File.separator + className + "Entity.java";
        }
//
//        if (template.contains("Mapper.java.ftl")) {
//            return packagePath + "mapper" + File.separator + className + "Mapper.java";
//        }
//
//        if (template.contains("Mapper.xml.ftl")) {
//            return  "src"+ File.separator + "main" + File.separator + "resources" + File.separator + "mapper"   + File.separator + className + "Mapper.xml";
//        }
//
//        if (template.contains("Service.java.ftl")) {
//            return packagePath + "service" + File.separator + className + "Service.java";
//        }
//
//        if (template.contains("ServiceImpl.java.ftl")) {
//
//            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
//        }
//        if (template.contains("Controller.java.ftl")) {
//            return packagePath + "controller" + File.separator + className + "Controller.java";
//        }

        return null;
    }
}
