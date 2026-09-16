package com.example.lantu_web_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lantu_web_java.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
