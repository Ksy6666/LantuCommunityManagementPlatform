package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    Role selectById(@Param("id") Long id);

    List<Role> selectAll();
}
