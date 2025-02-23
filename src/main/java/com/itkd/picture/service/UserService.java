package com.itkd.picture.service;

import com.itkd.picture.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itkd.picture.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author kdc
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-02-22 23:31:44
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */

    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    String getEncryptPassword(String userPassword);

    LoginUserVO getLoginUserVO(User user);
}
