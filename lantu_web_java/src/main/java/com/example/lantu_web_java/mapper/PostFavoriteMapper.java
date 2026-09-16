package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.PostFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostFavoriteMapper {

    int insert(PostFavorite postFavorite);

    /** 删除收藏记录（取消收藏） */
    int deleteByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    /** 是否存在收藏记录 */
    int countByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);
}
