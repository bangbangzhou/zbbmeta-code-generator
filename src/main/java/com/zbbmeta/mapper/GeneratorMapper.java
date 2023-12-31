package com.zbbmeta.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author springboot葵花宝典
 * @description: TODO
 */
public interface GeneratorMapper {


    /**
     *模糊匹配查询表列表
     * @param map
     * @return
     */
    List<Map<String, Object>> queryTableList(String map);


    /**
     * 精确匹配查询表
     * @param tableName
     * @return
     */
    Map<String, String> queryTable(String tableName);

    /**
     * 根据表名查找列信息
     * @param tableName
     * @return
     */
    List<Map<String, String>> queryColumns(String tableName);
}
