package com.arjun.subjective.demo.dao;

import com.arjun.subjective.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Description： user dao
 * Author: zhangm@seaskylight.com
 * Date: 2018/3/7 14:51
 */

@Mapper
public interface UserDao {

    @Select("select *from user where name = #{name}")
    User findByName(@Param("name") String name);

    @Insert("insert into user(id,name,age) values(#{id}, #{name},#{age})")
    int insert(@Param("id") Integer id, @Param("name") String name, @Param("age") Integer age);

    User findUserInfo();

    @Select("select *from user where id = #{id}")
    User getById(@Param("id") Integer id);
}