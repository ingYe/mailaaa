package com.example.mail.mapper;

import com.example.mail.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User login(@Param("username") String username, @Param("password") String password);
}
