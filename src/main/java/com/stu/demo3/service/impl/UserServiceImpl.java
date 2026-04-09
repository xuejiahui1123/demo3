package com.stu.demo3.service.impl;

import com.stu.demo3.common.Result;
import com.stu.demo3.common.ResultCode;
import com.stu.demo3.dto.UserDTO;
import com.stu.demo3.entity.User;
import com.stu.demo3.mapper.UserMapper;
import com.stu.demo3.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> register(UserDTO userDTO) {
        //1.查询该用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User exist = userMapper.selectOne(wrapper);

        if (exist != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }

        //2.组装实体对象
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        //3.插入数据库
        userMapper.insert(user);

        return Result.success("注册成功！");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        //1.根据用户名查询数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        User user = userMapper.selectOne(wrapper);

        //2.校验用户是否存在
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        if (!user.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }

        //return Result.success("登录成功！");
        String token = "Bearer " + System.currentTimeMillis() + "_" + userDTO.getUsername();
        return Result.success(token);//我的token:Bearer 1743564729381_testuser
    }

    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success("查询成功：" + user.getUsername());
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        // 1. 创建分页对象
        Page<User> page = new Page<>(pageNum, pageSize);

        // 2. 执行分页查询（null = 查全部）
        Page<User> resultPage = userMapper.selectPage(page, null);

        // 3. 返回分页数据
        return Result.success(resultPage);
    }
}