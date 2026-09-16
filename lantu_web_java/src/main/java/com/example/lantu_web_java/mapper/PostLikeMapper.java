package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeMapper {

    int insert(PostLike postLike);

    /** 删除点赞记录（取消点赞） */
    int deleteByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    /** 是否存在点赞记录 */
    int countByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);
}
