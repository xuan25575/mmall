package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.utils.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FIleServiceImpl implements IFileService {
    private static final Logger logger = LoggerFactory.getLogger(FIleServiceImpl.class);

    public String upload(MultipartFile file ,String path){
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            //上传成功
            file.transferTo(targetFile);
            //targetFile上传到文件服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 上传成功后，将upload文件删除。
            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件失败",e);
            return null;
        }
        return targetFile.getName();
    }


}
