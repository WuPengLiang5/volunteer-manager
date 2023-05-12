package com.wpl.volunteer.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wpl.volunteer.constant.CodeMsg;
import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.exception.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

public class JwtUtils {

    public final static Logger logger = LoggerFactory.getLogger(JwtUtils.class);;

    public final static String TOKEN_HEADER = "Authorization";

    public final static String TOKEN_PREFIX = "Bearer";

    public static final String SECRET = "volunteer-manager";

    /** web site user token 过期时间: 10天 */
    public static final int user_calendarField = Calendar.DATE;
    public static final int user_calendarInterval = 10;

    /** admin token 过期时间: 3小时 60 * 3 */
    public static final int admin_calendarField = Calendar.MINUTE;
    public static final int admin_calendarInterval = 60 * 3;

//    public static final int admin_calendarField = Calendar.SECOND;
//    public static final int admin_calendarInterval = 10;

    /**
     * 生成Token
     * @param id
     * @param username
     * @param userType
     * @return
     */
    public static String createToken(Integer id,String username,String userType) {

        Calendar nowTime = Calendar.getInstance();
        if (!userType.equals("Volunteer")){
            nowTime.add(admin_calendarField,admin_calendarInterval);
        }else{
            nowTime.add(user_calendarField,user_calendarInterval);
        }

        Date expiresDate = nowTime.getTime();
//        System.out.println(expiresDate);

        return JWT.create().withAudience(String.valueOf(id))   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("username", username)
                .withClaim("userType",userType)
                .sign(Algorithm.HMAC256(username + userType + SECRET));   //加密
    }

    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     * @param token
     * @throws Exception
     */
    public static boolean verifyToken(String token, String secret){
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret + SECRET)).build();
            jwt = verifier.verify(token);
//            System.out.println(jwt.getExpiresAt());
            return true;
        } catch (TokenExpiredException e) {
            logger.error(e.getMessage());
//            throw new GlobalException(new CodeMsg(401001, "token过期"));
            return false;
        }catch (JWTVerificationException e){
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) throws Exception {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
            throw new Exception();
        }
        return audience;
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }
}
