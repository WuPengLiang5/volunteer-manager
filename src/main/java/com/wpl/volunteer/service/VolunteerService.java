package com.wpl.volunteer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.Volunteer;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.VolunteerInfoVo;

import java.util.List;
import java.util.Map;

public interface VolunteerService extends IService<Volunteer> {
    Volunteer getVolunteerById(Integer id);
    Volunteer getVolunteerByUid(Integer uid);
//    Volunteer getOneByUnique(String unique);
    ResultCountVo listVolunteersByPage(QueryDTO queryDTO);
    int saveVolunteer(Volunteer volunteer);
    int removeVolunteer(Integer id);
    int removeVolunteers(Integer[] ids);
    int updateVolunteer(Volunteer volunteer);
    int selectCount();
    List<Map<String,Object>> listVolunteerMap();
    VolunteerInfoVo getVolunteerInfo(String username);
}
