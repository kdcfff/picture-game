package com.itkd.picture.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itkd.picture.mapper.PictureMapper;
import com.itkd.picture.model.entity.Picture;
import com.itkd.picture.service.PictureService;
import org.springframework.stereotype.Service;

/**
* @author kdc
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2025-02-28 19:26:13
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService {

}




