package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    int insert(Comment comment);

    /** 查询某帖子的评论列表（含作者昵称/头像），按时间正序 */
    List<Comment> selectByPostId(@Param("postId") Long postId);

    /** 评论点赞数 +delta */
    int changeLikeCount(@Param("id") Long id, @Param("delta") int delta);

    /** 查询评论是否已删除/存在 */
    Comment selectById(@Param("id") Long id);
}
