package com.itkd.picture.controller;

import com.itkd.picture.common.BaseResponse;
import com.itkd.picture.common.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    @RequestMapping("/health")
    public BaseResponse<String> health(){
        return ResultUtils.success("ok");
    }

}
