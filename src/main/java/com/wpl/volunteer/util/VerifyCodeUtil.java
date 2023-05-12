package com.wpl.volunteer.util;

import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.constant.VerifyCode;
import com.wpl.volunteer.exception.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 验证码生成工具。
 * 原文网址 http://codexe.net/j2ee/java-create-verification-code.html
 */
@Component
public class VerifyCodeUtil {

    private static final Random random = new Random();

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCodeUtil.class);

    public String getVerifyCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= VerifyCode.CHARS_NUMBER; i++)
            sb.append(VerifyCode.CHARS[random.nextInt(VerifyCode.CHARS.length)]);
        return sb.toString();
    }

    public BufferedImage getVerifyCodeImg(String verifyCode) {
        BufferedImage image = new BufferedImage(VerifyCode.IMAGE_WIDTH, VerifyCode.IMAGE_HEIGHT,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        // 背景颜色
        g.setColor(getRandColor(200,250));
        // 自定义长方形
        g.fillRect(0, 0, VerifyCode.IMAGE_WIDTH, VerifyCode.IMAGE_HEIGHT);
        // 字体颜色
        g.setColor(getRandColor(160,200));
        // 设置字体
        g.setFont(new Font("Arial", Font.PLAIN,
                VerifyCode.CHAR_HEIGHT + VerifyCode.CHAR_MARGIN));
        g.setColor(Color.BLACK);
        String c;
        for (int i = 0; i <= VerifyCode.CHARS_NUMBER - 1; i++) {
            c = String.valueOf(verifyCode.charAt(i));
            // 最左位置基线，左下
            g.drawString(c, i * (VerifyCode.CHAR_WIDTH + VerifyCode.CHAR_MARGIN) +
                            VerifyCode.CHAR_MARGIN / 2 + VerifyCode.IMAGE_PADDING,
                    VerifyCode.IMAGE_PADDING / 2 + VerifyCode.CHAR_HEIGHT);
        }
        int x, x1, y , y1;
        for (int i = 0; i < 10; i++) {
            x = random.nextInt(VerifyCode.IMAGE_WIDTH);
            y = random.nextInt(VerifyCode.IMAGE_HEIGHT);
            x1 = random.nextInt(VerifyCode.IMAGE_WIDTH);
            y1 = random.nextInt(VerifyCode.IMAGE_HEIGHT);
            g.drawLine(x, y, x + x1, y + y1);
        }
        // 处理图形并释放使用的资源
        g.dispose();
        return image;
    }

    public Color getRandColor(int fc,int bc){//给定范围获得随机颜色
        Random random = new Random();
        if(fc>255)
            fc=255;
        if(bc>255)
            bc=255;
        int r=fc+random.nextInt(bc-fc);
        int g=fc+random.nextInt(bc-fc);
        int b=fc+random.nextInt(bc-fc);

        return new Color(r,g,b);
    }

    /**
     *  生成验证码，并写入返回流
     */
    public void verifyCodeResponse(HttpServletRequest request,HttpServletResponse response) throws IOException {

        response.setContentType("image/jpeg");
        response.setCharacterEncoding("utf-8");

        String verifyCode = getVerifyCode();
        BufferedImage image = getVerifyCodeImg(verifyCode);

        HttpSession session = request.getSession();
        session.setAttribute("verifyCode",verifyCode);
//        session.setMaxInactiveInterval(60);

        // write 不会关闭提供的流
        ImageIO.write(image, "jpg", response.getOutputStream());
        // 关闭流
        response.getOutputStream().close();
        // 销毁验证码
        removeVerifyCode(request.getSession());
        LOGGER.info("verify code: " + verifyCode);
    }

    /**
     * 验证验证码
     * @param request
     * @param response
     * @param checkedCode
     */
    public void checkVerifyCode(HttpServletRequest request,
                                HttpServletResponse response,
                               String checkedCode) {

        HttpSession session = request.getSession();
        String verifyCode = (String) session.getAttribute("verifyCode");
        if (verifyCode == null)
            throw new GlobalException("验证码过期！");
        if (!verifyCode.equalsIgnoreCase(checkedCode))
            throw new GlobalException(CodeMsgConstant.WRONG_VERIFY_CODE);

        session.removeAttribute("verifyCode");
    }

    /**
     * 销毁验证码
     * @param session
     */
    private void removeVerifyCode(HttpSession session) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 删除session中存的验证码
                session.removeAttribute("verifyCode");
                timer.cancel();
            }
        }, 60 * 1000);

    }

}
