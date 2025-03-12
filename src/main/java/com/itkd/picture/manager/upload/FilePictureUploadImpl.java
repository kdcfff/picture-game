package com.itkd.picture.manager.upload;

import cn.hutool.core.io.FileUtil;
import com.itkd.picture.exception.BusinessException;
import com.itkd.picture.exception.ErrorCode;
import com.itkd.picture.exception.ThrowUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class FilePictureUploadImpl extends PictureUploadTemplate {
    @Override
    protected File processFile(Object source, File file) {
        MultipartFile multipartFile = (MultipartFile) source;
        try {
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件上传失败");
        }
    }

    @Override
    protected void validPicture(Object source) {
        MultipartFile multipartFile = (MultipartFile) source;
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR, "文件不能为空");
        // 1. 校验文件大小
        long fileSize = multipartFile.getSize();
        final long ONE_M = 1024 * 1024L;
        ThrowUtils.throwIf(fileSize > 2 * ONE_M, ErrorCode.PARAMS_ERROR, "文件大小不能超过 2M");
        // 2. 校验文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        // 允许上传的文件后缀
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("jpeg", "jpg", "png", "webp");
        ThrowUtils.throwIf(!ALLOW_FORMAT_LIST.contains(fileSuffix), ErrorCode.PARAMS_ERROR, "文件类型错误");
    }

    @Override
    protected String getOriginFilename(Object source) {
        final MultipartFile multipartFile = (MultipartFile) source;
        String originalFilename = multipartFile.getOriginalFilename();
        return originalFilename;
    }
}
