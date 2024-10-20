package oit.is.z2395.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

  @Select("SELECT * FROM users;")
  ArrayList<User> selectAllUser();

  @Select("SELECT * FROM users WHERE id = #{id};")
  User selectById(int id);

  @Select("SELECT * FROM users WHERE name = #{name};")
  User selectByName(String name);
}
