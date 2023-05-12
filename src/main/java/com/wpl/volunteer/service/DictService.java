package com.wpl.volunteer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wpl.volunteer.dto.DictTypeDTO;
import com.wpl.volunteer.entity.Dict;
import com.wpl.volunteer.vo.ResultCountVo;

import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> listDictByType(String type);

    Dict getDictByTypeAndCode(String type,String code);

    boolean updateDictType(DictTypeDTO dictTypeDTO);
}
