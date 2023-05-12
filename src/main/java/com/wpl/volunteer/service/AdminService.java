package com.wpl.volunteer.service;

import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.Admin;
import com.wpl.volunteer.vo.AdminInfoVo;
import com.wpl.volunteer.dto.LoginDTO;
import com.wpl.volunteer.dto.AdminProfile;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AdminService {
    AdminInfoVo getAdminInfoById(Integer id);
    Admin getAdminById(Integer id);
    Admin getAdminByUserName(String username);
    ResultCountVo listAdmins(QueryDTO queryDTO);
    int saveAdmin(Admin admin);
    int removeAdmin(Integer id);
    int updateAdmin(Admin admin);
    int selectCount();
    AdminProfile getAdminProfile(Integer id);

    ResultVo login(HttpServletRequest request,
                   HttpServletResponse response,
                   LoginDTO loginDTO);
}
