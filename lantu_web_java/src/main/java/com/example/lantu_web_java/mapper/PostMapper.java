package com.example.lantu_web_java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.lantu_web_java.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /** 查询帖子详情（含作者昵称/头像），等价于旧 selectById（跨库 JOIN，BaseMapper.selectById 无法表达） */
    Post findPostById(@Param("id") Long id);

    /** 分页查询帖子列表（含作者昵称/头像），按创建时间倒序 */
    List<Post> selectPage(@Param("category") String category,
                          @Param("keyword") String keyword,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    /** 帖子总数（按分类/关键词过滤） */
    long countPosts(@Param("category") String category,
                    @Param("keyword") String keyword);

    /** 查询用户发布的帖子列表 */
    List<Post> selectByUserId(@Param("userId") Long userId);

    /** 查询用户收藏的帖子列表 */
    List<Post> selectFavoriteByUserId(@Param("userId") Long userId);

    /** 热门帖子 TopN（按浏览+点赞+收藏综合热度） */
    List<Post> selectHotPosts(@Param("limit") int limit);
}
