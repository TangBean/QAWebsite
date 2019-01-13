package com.demo.dao;

import com.demo.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.Mapping;

@Repository
@Mapper
public interface UserDao {
    String TABLE_NAME = "user";

    String INSERT_FIELD = "name, password, salt, head_url";

    String SELECT_FIELD = "id, name, password, salt, head_url";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELD, ") " +
            "values(#{name}, #{password}, #{salt}, #{headUrl})"})
    void addUser(User user);

    @Select({"select ", SELECT_FIELD, " from ", TABLE_NAME, " where id = #{id}"})
    User searchById(int id);

    @Select({"select ", SELECT_FIELD, " from ", TABLE_NAME, " where name = #{name}"})
    User searchByName(String name);

    @Update({"update ", TABLE_NAME, " set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    void deleteById(int id);
}
