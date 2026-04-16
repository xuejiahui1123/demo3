package com.stu.demo3.service;

import com.stu.demo3.common.Result;
import com.stu.demo3.dto.UserDTO;
import com.stu.demo3.vo.UserDetailVO;
import com.stu.demo3.entity.UserInfo;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
    Result<String> getUserById(Long id);
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);

    Result<UserDetailVO> getUserDetail(Long userId);
    Result<String> updateUserInfo(UserInfo userInfo);
    Result<String> deleteUser(Long userId);
}
