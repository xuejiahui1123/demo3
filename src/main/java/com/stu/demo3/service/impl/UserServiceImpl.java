package com.stu.demo3.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stu.demo3.common.Result;
import com.stu.demo3.common.ResultCode;
import com.stu.demo3.dto.UserDTO;
import com.stu.demo3.entity.User;
import com.stu.demo3.entity.UserInfo;
import com.stu.demo3.mapper.UserInfoMapper;
import com.stu.demo3.mapper.UserMapper;
import com.stu.demo3.service.UserService;
import com.stu.demo3.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    // 缓存前缀
    private static final String CACHE_KEY_PREFIX = "user:detail:";

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

        return Result.success("登录成功！");
        //String token = "Bearer " + System.currentTimeMillis() + "_" + userDTO.getUsername();
        //return Result.success(token);//我的token:Bearer 1743564729381_testuser
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

    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;

        // 1. 先从Redis查缓存
        String json = redisTemplate.opsForValue().get(key);
        if (json != null && !json.isBlank()) {
            try {
                UserDetailVO vo = JSONUtil.toBean(json, UserDetailVO.class);
                return Result.success(vo);
            } catch (Exception e) {
                redisTemplate.delete(key);
            }
        }

        // 2. Redis没有，查数据库（多表联查）
        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        // 3. 存入Redis，有效期10分钟
        redisTemplate.opsForValue().set(
                key,
                JSONUtil.toJsonStr(detail),
                10,
                TimeUnit.MINUTES
        );

        return Result.success(detail);
    }

    @Override
    public Result<String> updateUserInfo(UserInfo userInfo) {
        // 1. 更新数据库
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserId, userInfo.getUserId());
        userInfoMapper.update(userInfo, wrapper);

        // 2. 删除对应缓存，让下次查询重新加载最新数据
        String key = CACHE_KEY_PREFIX + userInfo.getUserId();
        redisTemplate.delete(key);

        return Result.success("更新成功！");
    }

    @Override
    public Result<String> deleteUser(Long userId) {
        // 1. 删除主表用户
        userMapper.deleteById(userId);

        // 2. 删除关联的用户扩展信息
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserId, userId);
        userInfoMapper.delete(wrapper);

        // 3. 删除对应缓存
        String key = CACHE_KEY_PREFIX + userId;
        redisTemplate.delete(key);

        return Result.success("删除成功！");
    }
}