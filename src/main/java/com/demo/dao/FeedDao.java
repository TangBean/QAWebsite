package com.demo.dao;

import com.demo.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FeedDao {
    static final String INSERT_FIELD =
            "type,user_id,create_date,data";

    static final String SELECT_FIELD =
            "id,type,user_id,create_date,data";

    static final String TABLE_NAME = "feed";

    @Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELD, ") " +
            "VALUES(#{type},#{userId},#{createDate},#{data})"})
    int addFeed(Feed feed);

    @Select({"SELECT ", SELECT_FIELD, "FROM ", TABLE_NAME, "WHERE id=#{id}"})
    Feed getById(int id);

    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);

}
