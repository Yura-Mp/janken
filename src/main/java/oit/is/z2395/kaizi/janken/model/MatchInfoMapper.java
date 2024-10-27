package oit.is.z2395.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchInfoMapper {

  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES(#{user1}, #{user2}, #{user1Hand}, TRUE)")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void startMatch(MatchInfo matchinfo);

  @Select("SELECT * FROM matchinfo WHERE isActive = TRUE")
  ArrayList<MatchInfo> selectActiveMatch();

}
