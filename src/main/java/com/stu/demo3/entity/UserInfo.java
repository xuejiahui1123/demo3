package com.stu.demo3.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_info")
public class UserInfo {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;      // 关联用户ID
    private String realName; // 真实姓名
    private String phone;    // 电话
    private String address;  // 地址
}