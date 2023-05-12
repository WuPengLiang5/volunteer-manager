package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpl.volunteer.dao.VolunteerDao;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.User;
import com.wpl.volunteer.entity.Volunteer;
import com.wpl.volunteer.service.UserService;
import com.wpl.volunteer.service.VolunteerService;
import com.wpl.volunteer.util.GenerateUtil;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import com.wpl.volunteer.vo.VolunteerInfoVo;
import com.wpl.volunteer.vo.VolunteerVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class VolunteerServiceImpl extends ServiceImpl<VolunteerDao,Volunteer> implements VolunteerService {

    @Autowired
    private VolunteerDao volunteerDao;

    @Autowired
    private UserService userService;

    @Override
    public Volunteer getVolunteerById(Integer id) {
        return volunteerDao.selectById(id);
    }

    @Override
    public Volunteer getVolunteerByUid(Integer uid) {
        QueryWrapper<Volunteer> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        return volunteerDao.selectOne(queryWrapper);
    }

    @Override
    public ResultCountVo listVolunteersByPage(QueryDTO queryDTO) {
        IPage<Volunteer> iPage = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        volunteerDao.selectPage(iPage, null);
        List<VolunteerVo> volunteerVos = new ArrayList<>();
        for (Volunteer volunteer:iPage.getRecords()){
            VolunteerVo volunteerVo = new VolunteerVo();
            BeanUtils.copyProperties(volunteer,volunteerVo);
            User user = userService.getUserById(volunteer.getUid());
            BeanUtils.copyProperties(user,volunteerVo,"id");
            volunteerVos.add(volunteerVo);
        }

        return new ResultCountVo(ResultVo.success("获取成功",volunteerVos),iPage.getTotal());
    }

    @Override
    public int saveVolunteer(Volunteer volunteer) {
        volunteer.setVNumber(GenerateUtil.getVolunteerNumber(volunteer.getIdNumber()));
        return volunteerDao.insert(volunteer);
    }

    @Override
    public int removeVolunteer(Integer id) {
        return volunteerDao.deleteById(id);
    }

    @Override
    public int removeVolunteers(Integer[] ids) {
        return volunteerDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public int updateVolunteer(Volunteer volunteer) {
        return volunteerDao.updateById(volunteer);
    }

    @Override
    public int selectCount() {
        return volunteerDao.selectCount(null);
    }

    @Override
    public List<Map<String, Object>> listVolunteerMap() {
        QueryWrapper<Volunteer> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("location name","count(location) value").groupBy("location");
        List<Map<String,Object>> volunteerMaps = volunteerDao.selectMaps(queryWrapper);
        return volunteerMaps;
    }

    @Override
    public VolunteerInfoVo getVolunteerInfo(String username){
        User user = userService.getUserByUserName(username);
        Volunteer volunteer = getVolunteerByUid(user.getId());
        VolunteerInfoVo volunteerInfoVo = new VolunteerInfoVo();
        BeanUtils.copyProperties(volunteer,volunteerInfoVo);
        BeanUtils.copyProperties(user,volunteerInfoVo, "id");
        return volunteerInfoVo;
    }
}
