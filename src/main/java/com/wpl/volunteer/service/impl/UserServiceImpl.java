package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpl.volunteer.constant.RedisKey;
import com.wpl.volunteer.dao.UserDao;
import com.wpl.volunteer.dao.VolunteerDao;
import com.wpl.volunteer.dto.LoginDTO;
import com.wpl.volunteer.entity.SecurityUser;
import com.wpl.volunteer.entity.User;
import com.wpl.volunteer.entity.Volunteer;
import com.wpl.volunteer.dto.Registrants;
import com.wpl.volunteer.exception.GlobalException;
import com.wpl.volunteer.service.UserService;
import com.wpl.volunteer.util.IPUtil;
import com.wpl.volunteer.util.JwtUtils;
import com.wpl.volunteer.util.RedisUtil;
import com.wpl.volunteer.util.VerifyCodeUtil;
import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.vo.LoginVo;
import com.wpl.volunteer.vo.ResultVo;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService , UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private VolunteerDao volunteerDao;

    @Autowired
    private VerifyCodeUtil verifyCodeUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public User getUserById(Integer id) {
        return userDao.selectById(id);
    }

    @Override
    public User getUserByUserName(String username) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return userDao.selectOne(queryWrapper);
    }

    @Override
    public User getOneByUnique(String unique) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",unique).or().eq("username",unique).or().eq("phone",unique);
        return userDao.selectOne(queryWrapper);
    }

    @Override
    public List<User> listUsers() {
        return userDao.selectList(null);
    }

    @Override
    public int saveUser(User user) {
        return userDao.insert(user);
    }

    @Override
    public int removeUser(Integer id) {
        return userDao.deleteById(id);
    }

    @Override
    public int updateUser(User user) {
        return userDao.updateById(user);
    }

    @Override
    public ResultVo login(HttpServletRequest request, HttpServletResponse response, LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        String verifyCode = loginDTO.getVerifyCode();
        //        System.out.println(IPUtil.getIp(request));

        verifyCodeUtil.checkVerifyCode(request,response,verifyCode);

        try {
            //认证
            SecurityUser  userDetails = (SecurityUser) loadUserByUsername(username);
            //md5(salt+password)
            String dbPwd = userDetails.getPassword();
            String tem = userDetails.getSalt() + password;
            String md5 = DigestUtils.md5DigestAsHex(tem.getBytes());
            System.out.println(md5);
            if (!dbPwd.equals(md5)){
                return ResultVo.error("密码错误");
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            if (redisUtil.get(RedisKey.USER + loginDTO.getUsername()) != null) {
                redisUtil.delete(RedisKey.USER + loginDTO.getUsername());
            }
            // 加入缓存
            redisUtil.set(RedisKey.ADMIN + loginDTO.getUsername(), userDetails);

            LoginVo loginVo = new LoginVo();
            BeanUtils.copyProperties(userDetails,loginVo);
//            loginVo.setAvatar(user.getAvatar());
            String token = JwtUtils.createToken(userDetails.getId(),userDetails.getUsername(),"Volunteer");
            loginVo.setToken(token);

            // 更新登录时间和登录IP
            User user = new User();
            BeanUtils.copyProperties(userDetails,user);
            user.setLoginTime(LocalDateTime.now());
            user.setLoginIp(IPUtil.getIp(request));
            updateUser(user);

            return ResultVo.success(loginVo);
        }catch (Exception e){
            logger.error("login error is {}", e.getMessage());
            throw new GlobalException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResultVo register(HttpServletRequest request,
                             HttpServletResponse response,
                             Registrants registrants) {
        // 验证码是否正确
        verifyCodeUtil.checkVerifyCode(request,response,registrants.getVerifyCode());

        //用户名是否存在
        if (getOneByUnique(registrants.getUsername()) != null
                || getOneByUnique(registrants.getPhone()) != null) {
            throw new GlobalException("用户已存在");
        }

        User newUser = new User();
        newUser.setUsername(registrants.getUsername());
        newUser.setPhone(registrants.getPhone());
        newUser.setRegisterTime(LocalDateTime.now());
        newUser.setAvatar("");

        //随机生成盐值
        String salt = UUID.randomUUID().toString();
        String tem = salt + registrants.getPassword();
        String md5 = DigestUtils.md5DigestAsHex(tem.getBytes());
        newUser.setSalt(salt);
        newUser.setPassword(md5);

        int result = saveUser(newUser);
        if (result !=1 ) {
            throw new GlobalException("注册失败");
        } else {
            Volunteer volunteer=new Volunteer();
            User user = getOneByUnique(registrants.getUsername());
            BeanUtils.copyProperties(registrants,volunteer);
            volunteer.setUid(user.getId());

            int res=volunteerDao.insert(volunteer);
            if (res != 1) {
                return ResultVo.error("注册失败！");
            }
            return ResultVo.success("注册成功！");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名错误");
        }
        SecurityUser securityUser = new SecurityUser();
        BeanUtils.copyProperties(user,securityUser);
        securityUser.setUsername(username);
        securityUser.setPassword(user.getPassword());
        securityUser.setRole(null);
        return securityUser;
    }
}
