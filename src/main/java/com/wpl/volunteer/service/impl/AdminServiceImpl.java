package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.constant.RedisKey;
import com.wpl.volunteer.dao.AdminDao;
import com.wpl.volunteer.dto.AdminProfile;
import com.wpl.volunteer.dto.LoginDTO;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.Admin;
import com.wpl.volunteer.entity.Role;
import com.wpl.volunteer.entity.SecurityUser;
import com.wpl.volunteer.exception.GlobalException;
import com.wpl.volunteer.service.AdminService;
import com.wpl.volunteer.service.RoleService;
import com.wpl.volunteer.util.JwtUtils;
import com.wpl.volunteer.util.RedisUtil;
import com.wpl.volunteer.util.VerifyCodeUtil;
import com.wpl.volunteer.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService , UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private VerifyCodeUtil verifyCodeUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public AdminInfoVo getAdminInfoById(Integer id) {
        Admin admin = adminDao.selectById(id);
        AdminInfoVo adminInfo = new AdminInfoVo();
//        Role role = roleService.getRoleById(admin.getRoleId());

        adminInfo.setId(admin.getId());
//        adminInfo.setUsername(admin.getUsername());
        adminInfo.setName(admin.getName());
        adminInfo.setPhone(admin.getPhone());
        adminInfo.setEmail(admin.getEmail());
        adminInfo.setIntroduction(admin.getIntroduction());
        adminInfo.setIsDelete(admin.getIsDelete());
//        adminInfo.setRoleId(admin.getRoleId());
        return adminInfo;
    }

    @Override
    public Admin getAdminById(Integer id) {
        return adminDao.selectById(id);
    }

    @Override
    public Admin getAdminByUserName(String username){
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return adminDao.selectOne(queryWrapper);
    }

    @Override
    public ResultCountVo listAdmins(QueryDTO queryDTO) {
        IPage<Admin> iPage = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        adminDao.selectPage(iPage, null);

        List<Admin> admins = iPage.getRecords();
        List<AdminInfoVo> adminInfoVos = new ArrayList<>();
        for (Admin admin: admins){
            AdminInfoVo adminInfo = new AdminInfoVo();

            BeanUtils.copyProperties(admin,adminInfo);
//            adminInfo.setId(admin.getId());
//            adminInfo.setUsername(admin.getUsername());
//            adminInfo.setPassword(admin.getPassword());
//            adminInfo.setName(admin.getName());
//            adminInfo.setPhone(admin.getPhone());
//            adminInfo.setEmail(admin.getEmail());
//            adminInfo.setIdNumber(admin.getIdNumber());
//            adminInfo.setIsDelete(admin.getIsDelete());
//            adminInfo.setRoleId(admin.getRoleId());
            adminInfoVos.add(adminInfo);
        }
        ResultVo resultVo = ResultVo.success("获取成功",adminInfoVos);
        return new ResultCountVo(resultVo,iPage.getTotal());
    }

    @Override
    public int saveAdmin(Admin admin) {
        //随机生成盐值
        String create_salt = UUID.randomUUID().toString();
        String new_md5 = DigestUtils.md5DigestAsHex((create_salt + admin.getPassword()).getBytes());
        admin.setSalt(create_salt);
        admin.setPassword(new_md5);
        return adminDao.insert(admin);
    }

    @Override
    public int removeAdmin(Integer id) {
        return adminDao.deleteById(id);
    }

    @Override
    public int updateAdmin(Admin admin) {
        return adminDao.updateById(admin);
    }

    @Override
    public int selectCount() {
        return adminDao.selectCount(null);
    }

    @Override
    public AdminProfile getAdminProfile(Integer id) {
        AdminProfile adminProfile = new AdminProfile();
        Admin admin = adminDao.selectById(id);
        adminProfile.setId(admin.getId());
        adminProfile.setName(admin.getName());
        adminProfile.setUsername(admin.getUsername());
        adminProfile.setPhone(admin.getPhone());
        adminProfile.setEmail(admin.getEmail());
        adminProfile.setIntroduction(admin.getIntroduction());
        return adminProfile;
    }

    @Override
    public ResultVo login(HttpServletRequest request,
                          HttpServletResponse response,
                          LoginDTO loginDTO) {
        String verifyCode = loginDTO.getVerifyCode();
        verifyCodeUtil.checkVerifyCode(request,response,verifyCode);

        try {

            //认证
            SecurityUser  userDetails = (SecurityUser) loadUserByUsername(loginDTO.getUsername());
            String dbPwd = userDetails.getPassword();
            String tem = userDetails.getSalt() + loginDTO.getPassword();
            String md5 = DigestUtils.md5DigestAsHex(tem.getBytes());
//            System.out.println(DigestUtils.md5DigestAsHex((userDetails.getSalt() + loginDTO.getPassword()).getBytes()));
//            System.out.println(DigestUtils.md5DigestAsHex((userDetails.getSalt() + "123456").getBytes()));
            if (!dbPwd.equals(md5)){
                return ResultVo.error("密码错误");
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            if (redisUtil.get(RedisKey.ADMIN + loginDTO.getUsername()) != null) {
                redisUtil.delete(RedisKey.ADMIN + loginDTO.getUsername());
            }
            // 加入缓存
            redisUtil.set(RedisKey.ADMIN + loginDTO.getUsername(), userDetails);

            LoginVo loginVo = new LoginVo();
            BeanUtils.copyProperties(userDetails,loginVo);
            String token = JwtUtils.createToken(userDetails.getId(),userDetails.getUsername(),"Administrator");
            loginVo.setToken(token);
            loginVo.setRoleId(userDetails.getRole().getId());
            return ResultVo.success(loginVo);
        }catch (Exception e){
            logger.error("login error is {}", e.getMessage());
            throw new GlobalException(e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = getAdminByUserName(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名错误");
        }
        Role role = roleService.getRoleById(admin.getRoleId());
        SecurityUser securityUser = new SecurityUser();
        BeanUtils.copyProperties(admin,securityUser);
        if (role == null){
            throw new GlobalException("角色不存在，请联系超级管理员");
        }
        securityUser.setRole(role);
        return securityUser;
    }
}
