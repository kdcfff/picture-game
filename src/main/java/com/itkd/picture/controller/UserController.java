package com.itkd.picture.controller;

import com.itkd.picture.common.BaseResponse;
import com.itkd.picture.common.ResultUtils;
import com.itkd.picture.exception.ErrorCode;
import com.itkd.picture.exception.ThrowUtils;
import com.itkd.picture.model.dto.UserRegisterRequest;
import com.itkd.picture.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> register
            (@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

}
