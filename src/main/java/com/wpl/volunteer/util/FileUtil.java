package com.wpl.volunteer.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Upload;
import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.exception.GlobalException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class FileUtil {

    /**
     * 设置固定常量
     */
//    public static final int APP_ID = **********;
    public static final String secretId = "AKIDgDVWGqDKAj23oIVR6UaQzHI2VhcHgndC";
    public static final String secretKey = "KN9espUs1sSmoZ3GDvLUMyK7gz2oKZXD";
    public static final String bucketName = "volunteer-1311810225";
    public static final String URL = "存储桶地址";

    // 创建 COSClient 实例，这个实例用来后续调用请求
    public static COSClient createCOSClient() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-chengdu");
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);

        // 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }

    // 上传到服务器
    public static String uploadFile(MultipartFile file){
        if (file.isEmpty()) {
            throw new GlobalException(CodeMsgConstant.NO_UPLOAD);
        }

        // 生成 cos 客户端。
        COSClient cosClient = createCOSClient();

        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUID.randomUUID() + suffixName;
        String key = "images/" + fileName;

        ObjectMetadata objectMetadata = new ObjectMetadata();

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(),objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
//            System.out.println(putObjectResult.getRequestId());
//            System.out.println(putObjectResult.getETag());
        } catch (CosClientException | IOException e) {
            e.printStackTrace();
        }

        // 确认本进程不再使用 cosClient 实例之后，关闭之
        cosClient.shutdown();

        String cosUrl = "https://volunteer-1311810225.cos.ap-chengdu.myqcloud.com/";

        return cosUrl + key;
    }

    // 从服务器上删除
    public static void deleteFile(String key){
        COSClient cosClient = createCOSClient();

        try {
            cosClient.deleteObject(bucketName, key);
        } catch (CosClientException e) {
            e.printStackTrace();
        }

        cosClient.shutdown();
    }

    // 上传到本地
    public static String uploadPicture(MultipartFile file){
        if (file.isEmpty()) {
            throw new GlobalException(CodeMsgConstant.NO_UPLOAD);
        }
        //文件全名称
        String fileName = file.getOriginalFilename();
        //后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //文件名
//        String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
        //上传后的路径
        String filePath = "E:\\毕业设计\\GraduationDesign\\uploads\\images\\";

//        Date d = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String date = sdf.format(d);

        // 新文件名 防止同一张图片重复提交名称冲突
//        fileName =prefixName + date + suffixName;
        fileName = UUID.randomUUID() + suffixName;

        //绝对路径--文件的存储路径
        String absolutepath=filePath + fileName;
        //读取路径 http://192.168.xx.xxx:8080/img/这个路径在浏览器中可以访问到，这样重新在数据库中读出来可以直接显示
        String readfilePath = "http://localhost:8081/images/";
        String readUrl = readfilePath + fileName;
        //将图片准备写入本地或服务器
        File dest = new File(absolutepath);
        //查看是否有父目录没有则创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            //上传图片
            file.transferTo(dest);
            return readUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new GlobalException(CodeMsgConstant.UPLOAD_FAILED);
    }
}
