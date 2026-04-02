package com.stu.demo3.service;

import com.stu.demo3.common.Result;
import com.stu.demo3.dto.UserDTO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
    Result<String> getUserById(Long id);
}
