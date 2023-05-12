package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpl.volunteer.dao.DictDao;
import com.wpl.volunteer.dto.DictTypeDTO;
import com.wpl.volunteer.entity.Dict;
import com.wpl.volunteer.exception.GlobalException;
import com.wpl.volunteer.service.DictService;
import com.wpl.volunteer.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictDao, Dict> implements DictService {

    @Autowired
    private DictDao dictDao;

    @Override
    public List<Dict> listDictByType(String type) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",type);

        return dictDao.selectList(queryWrapper);
    }

    @Override
    public Dict getDictByTypeAndCode(String type, String code) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",type).eq("code",code);
        return dictDao.selectOne(queryWrapper);
    }

    @Transactional
    @Override
    public boolean updateDictType(DictTypeDTO dictTypeDTO) {
        if (StringUtils.isEmpty(dictTypeDTO.getType())){
            throw new GlobalException("字典类型为空");
        }
        for (Integer id:dictTypeDTO.getIds()){
            Dict dict = dictDao.selectById(id);
            dict.setType(dictTypeDTO.getType());
            dict.setTypeName(dictTypeDTO.getTypeName());
            int result = dictDao.updateById(dict);
            if (result != 1){
                return false;
            }
        }
        return true;
    }
}
