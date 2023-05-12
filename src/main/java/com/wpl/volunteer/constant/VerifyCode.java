package com.wpl.volunteer.constant;

public interface VerifyCode {
    //选择的字符
    char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ".toCharArray();
    //验证码个数
    int CHARS_NUMBER = 4;

    //生成图片的各个参数
    int CHAR_WIDTH = 15;
    int CHAR_HEIGHT = 30;
    int CHAR_MARGIN = 5;
    int IMAGE_PADDING = 10;

    //图片宽度
    int IMAGE_WIDTH = (CHAR_WIDTH + CHAR_MARGIN) * CHARS_NUMBER + IMAGE_PADDING * 2;
    //图片高度
    int IMAGE_HEIGHT = (CHAR_HEIGHT + CHAR_MARGIN) + IMAGE_PADDING;
}
