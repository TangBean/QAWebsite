package com.demo.dao;

import com.demo.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface QuestionDao {
    String SELECT_FIELD = "id, title, content, created_date, user_id, comment_count";

    String INSERT_FIELD = "title, content, user_id, created_date";

    String TABLE_NAME = "question";

    List<Question> selectByUserIdAndOffset(@Param("userId") int userId,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    @Insert({"INSERT INTO ", TABLE_NAME, " (", INSERT_FIELD, ")" +
            "VALUES(#{title},#{content},#{userId},#{createdDate})"})
    int addQuestion(Question question);

    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME, "where id=#{id}"})
    Question selectById(@Param("id") int id);

    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME,
            "ORDER BY created_date DESC LIMIT #{offset}, #{limit}"})
    List<Question> selectLatestQuestions(@Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update ", TABLE_NAME, "set comment_count=#{count} where id=#{questionId}"})
    int updateCommentCount(@Param("questionId") int questionId, @Param("count") int count);
}
