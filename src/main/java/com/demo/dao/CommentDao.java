package com.demo.dao;

import com.demo.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentDao {
    static final String INSERT_FIELD =
            "user_id,content,entity_id,entity_type,created_date";

    static final String SELECT_FIELD =
            "id,user_id,content,entity_id,entity_type,created_date,status";

    static final String TABLE_NAME = "comment";

    @Select({"select ", SELECT_FIELD, " from ", TABLE_NAME, " where id=#{commentId}"})
    Comment selectCommentById(@Param("commentId") int commentId);

    @Insert({"INSERT INTO ", TABLE_NAME, "(", INSERT_FIELD, ") " +
            "VALUES(#{userId},#{content},#{entityId},#{entityType},#{createdDate})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME, "where " +
            "entity_id=#{entityId} and entity_type=#{entityType}"})
    List<Comment> selectCommentsByEntity(@Param("entityId") int entityId,
                                          @Param("entityType") int entityType);

    @Select({"select count(*) from ", TABLE_NAME, "where " +
            "entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);

    @Update({"UPDATE ", TABLE_NAME, "SET status=#{status} WHERE id=#{commentId}"})
    int updateCommentStatus(@Param("commentId") int commentId,
                            @Param("status") int status);

    @Select({"select count(*) from ", TABLE_NAME, "where user_id = #{userId}"})
    int getUserCommentCount(@Param("userId") int userId);

}
