package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.UserAvatar;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserAvatarMapper {

    int insert(UserAvatar avatar);

    UserAvatar selectByUserId(@Param("userId") Long userId);

    int deleteByUserId(@Param("userId") Long userId);

    /** 批量查询用户头像 */
    List<UserAvatar> selectByUserIds(@Param("userIds") List<Long> userIds);
}
