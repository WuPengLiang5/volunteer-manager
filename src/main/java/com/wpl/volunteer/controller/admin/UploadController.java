package com.wpl.volunteer.controller.admin;

import com.wpl.volunteer.util.FileUtil;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/uploadImage")
    public ResultVo uploadImage(MultipartFile file) {
        return ResultVo.success("上传成功", FileUtil.uploadPicture(file));
    }

    @PostMapping(value = "/uploadFile")
    public ResultVo uploadFile(MultipartFile file) {
        return ResultVo.success("上传成功", FileUtil.uploadFile(file));
    }
}
