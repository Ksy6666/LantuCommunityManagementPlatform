package com.example.lantu_web_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lantu_web_java.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /** 查询某帖子的评论列表（含作者昵称/头像），按时间正序 */
    List<Comment> selectByPostId(@Param("postId") Long postId);

    /** 查询评论是否已删除/存在（status=1 过滤），等价于旧 selectById */
    Comment findCommentById(@Param("id") Long id);
}
