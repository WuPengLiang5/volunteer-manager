package com.wpl.volunteer.service;

import com.wpl.volunteer.dto.LoginDTO;
import com.wpl.volunteer.entity.User;
import com.wpl.volunteer.dto.Registrants;
import com.wpl.volunteer.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    User getUserById(Integer id);
    User getUserByUserName(String username);
    User getOneByUnique(String unique);
    List<User> listUsers();
    int saveUser(User user);
    int removeUser(Integer id);
    int updateUser(User user);

    ResultVo login(HttpServletRequest request,
                   HttpServletResponse response,
                   LoginDTO loginDTO);

    ResultVo register(HttpServletRequest request,
                      HttpServletResponse response,
                      Registrants registrants);
}
