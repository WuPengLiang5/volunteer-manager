package com.wpl.volunteer.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.annotation.Log;
import com.wpl.volunteer.dto.DictTypeDTO;
import com.wpl.volunteer.dto.QueryDictDTO;
import com.wpl.volunteer.entity.Activity;
import com.wpl.volunteer.entity.Dict;
import com.wpl.volunteer.enums.BusinessType;
import com.wpl.volunteer.exception.GlobalException;
import com.wpl.volunteer.service.DictService;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @RequestMapping("/listDictByPage")
    public ResultVo listDictByPage(@RequestBody QueryDictDTO queryDictDTO){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        if(queryDictDTO.getType()!=null && !"".equals(queryDictDTO.getType()))
            wrapper.like("type",queryDictDTO.getType());
        if(queryDictDTO.getTypeName()!=null && !"".equals(queryDictDTO.getTypeName()))
            wrapper.like("type_name",queryDictDTO.getTypeName());
        if(queryDictDTO.getCode()!=null && !"".equals(queryDictDTO.getCode()))
            wrapper.like("code",queryDictDTO.getCode());

        IPage<Dict> page = new Page<>(queryDictDTO.getPage(), queryDictDTO.getLimit());
        dictService.page(page, wrapper);

        return new ResultCountVo(ResultVo.success("获取成功",page.getRecords()), page.getTotal());
    }

    @GetMapping(value = "/listDictByType")
    public ResultVo listDictByType(String type){
        return ResultVo.success("获取成功",dictService.listDictByType(type));
    }

    @RequestMapping(value = "/listDictType",method = RequestMethod.GET)
    public ResultVo listDictType(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("group_concat(id) as ids","type","type_name as typeName","group_concat(code) as code").groupBy("type","type_name");
        List<Map<String,Object>> maps = dictService.listMaps(queryWrapper);
        return ResultVo.success("获取成功",maps);
    }

    @Log(module = "字典管理",businessType = BusinessType.INSERT)
    @RequestMapping("/saveDict")
    public ResultVo saveDict(@RequestBody Dict dict){
        List<Dict> dicts = dictService.listDictByType(dict.getType());
        for (Dict temp:dicts){
            if (temp.getCode().equals(dict.getCode())){
                return ResultVo.error("字典编码已存在！");
            }

            if (temp.getSort().equals(dict.getSort())){
                return ResultVo.error("排序值已存在");
            }
        }
        dict.setCreateTime(LocalDateTime.now());
        boolean isSave = dictService.save(dict);
        if (!isSave){
            return ResultVo.error("新增失败");
        }
        return ResultVo.success("新增成功");
    }

    @Log(module = "字典管理",businessType = BusinessType.DELETE)
    @RequestMapping("/delDict")
    public ResultVo removeDict(@RequestParam Integer dictId){
        boolean isRemove = dictService.removeById(dictId);
        if (!isRemove){
            return ResultVo.error("删除失败");
        }
        return ResultVo.success("删除成功");
    }

    @Log(module = "字典管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updateDict")
    public ResultVo updateDict(@RequestBody Dict dict){
        List<Dict> dicts = dictService.listDictByType(dict.getType());
        Dict dbDict = dictService.getDictByTypeAndCode(dict.getType(),dict.getCode());
        for (Dict temp:dicts){
//            if (temp.getCode().equals(dict.getCode())){
//                throw new GlobalException("字典编码已存在！");
//            }

            if (!dbDict.getSort().equals(dict.getSort())
                    && temp.getSort().equals(dict.getSort())){
                throw new GlobalException("排序值已存在");
            }
        }
        dict.setUpdateTime(LocalDateTime.now());
        boolean isUpdate = dictService.updateById(dict);
        if (!isUpdate){
            return ResultVo.error("修改失败");
        }
        return ResultVo.success("修改成功");
    }

    @Log(module = "字典管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updateDictType")
    public ResultVo updateDictType(@RequestBody DictTypeDTO dictTypeDTO){
        boolean isUpdate = dictService.updateDictType(dictTypeDTO);
        if (!isUpdate){
            return ResultVo.error("修改失败");
        }
        return ResultVo.success("修改成功");
    }
}
