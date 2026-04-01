package com.stu.demo3.service.impl;
import org.springframework.stereotype.Service;

import com.stu.demo3.common.Result;
import com.stu.demo3.common.ResultCode;
import com.stu.demo3.dto.UserDTO;
import com.stu.demo3.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private static final Map<String, String> userDb = new HashMap<>();

    @Override
    public Result<String> register(UserDTO userDTO) {
        if (userDb.containsKey(userDTO.getUsername())) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }
        userDb.put(userDTO.getUsername(), userDTO.getPassword());
        return Result.success("注册成功");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        if (!userDb.containsKey(userDTO.getUsername())) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        String dbPwd = userDb.get(userDTO.getUsername());
        if (!dbPwd.equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }
        String token = "Bearer " + System.currentTimeMillis() + "_" + userDTO.getUsername();
        return Result.success(token);
    }
}