package com.example.demo.dao.db;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper{
    @Select("select * from test_user limit 1")
    User allUser();

    @Select("select * from test_user where id = #{id}")
    User userById(int id);

    @Insert("insert into test_user(username, age, nickname, female, adult) values(#{username}, #{age}, #{nickname}, #{female}, #{adult})")
    int insertUser(@Param("username")String userName, @Param("age")int age, @Param("nickname")String nickName, @Param("female")int female, @Param("adult")int adult);

    @Select("select id from test_user order by id desc limit 1")
    int selectLastId();
}
