package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.Preregistration;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PreregistrationMapper {

    int insert(Preregistration preregistration);
}
