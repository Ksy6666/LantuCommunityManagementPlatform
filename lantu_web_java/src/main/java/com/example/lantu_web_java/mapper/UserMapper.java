package com.example.lantu_web_java.mapper;

import com.example.lantu_web_java.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    User selectById(Long id);

    User selectByAccount(String account);

    User selectByPhone(String phone);

    User selectByEmail(String email);

    int countByAccount(String account);

    int countByPhone(String phone);

    int countByEmail(String email);

    int countByNickname(String nickname);

    /**
     * 登录用：优先按 account 匹配，其次按 phone 匹配
     */
    User selectByPhoneOrAccount(@Param("loginId") String loginId);

    int updateBirthday(@Param("id") Long id, @Param("birthday") LocalDate birthday);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateProfile(User user);

    /** 批量查询用户 */
    List<User> selectByIds(@Param("ids") List<Long> ids);

    // ----- 管理后台 -----

    /** 分页查询用户（支持 keyword 搜索账号/昵称/姓名/手机号） */
    List<User> selectAllUsers(@Param("keyword") String keyword,
                              @Param("offset") int offset,
                              @Param("size") int size);

    /** 统计用户总数（支持 keyword 搜索） */
    int countAllUsers(@Param("keyword") String keyword);

    /** 统计用户总数（不搜索） */
    int countTotalUsers();

    /** 更新用户角色 */
    int updateRole(@Param("id") Long id, @Param("roleId") Long roleId);

    /** 删除用户 */
    int deleteById(@Param("id") Long id);
}
