package ${package}.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ${package}.entity.${className}Entity;
import ${package}.service.${className}Service;
import ${package}.entity.R;




/**
 * ${comments}
 *
 * @author ${author}
 *
 *
 */
@RestController
@RequestMapping("${pathName}")
public class ${className}Controller {
    @Autowired
    private ${className}Service ${classname}Service;



    /**
     * 信息
     */
    @GetMapping("/info/{${pk.lowerFirstAttrName}}")
    public R info(@PathVariable("${pk.lowerFirstAttrName}") ${pk.attrType} ${pk.lowerFirstAttrName}){
		${className}Entity ${classname} = ${classname}Service.getById(${pk.lowerFirstAttrName});

        return R.ok().put("${classname}", ${classname});
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody ${className}Entity ${classname}){
		${classname}Service.save(${classname});

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public R update(@RequestBody ${className}Entity ${classname}){
		${classname}Service.updateById(${classname});

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody ${pk.attrType}[] ${pk.lowerFirstAttrName}s){
		${classname}Service.removeByIds(Arrays.asList(${pk.lowerFirstAttrName}s));

        return R.ok();
    }

}
