package com.demo.dao;

import com.demo.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDao {
    static final String TABLE_NAME = "message";

    static final String INSERT_FIELD = "fromid,toid,content,conversation_id,created_date,is_read";

    static final String SELECT_FIELD = "id,fromid,toid,content,conversation_id,created_date,is_read";

    @Insert({"INSERT INTO ", TABLE_NAME, " (", INSERT_FIELD, ") " +
            "VALUES(#{fromId},#{toId},#{content},#{conversationId},#{createdDate},#{isRead})"})
    int addMessage(Message message);

    @Select({"SELECT ", SELECT_FIELD, "FROM ", TABLE_NAME,
            " WHERE conversation_id=#{conversationId} ORDER BY created_date DESC" +
            " LIMIT #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"SELECT COUNT(id) AS id,", INSERT_FIELD, " FROM " +
            "(SELECT * FROM ", TABLE_NAME, " WHERE fromid=#{localUserId} OR toid=#{localUserId} " +
            "ORDER BY created_date DESC) tt " +
            "GROUP BY conversation_id LIMIT #{offset},#{limit}"})
    List<Message> getConversationList(@Param("localUserId") int localUserId,
                                      @Param("offset") int offset,
                                      @Param("limit") int limit);

    @Select({"SELECT COUNT(id) FROM ", TABLE_NAME,
            " WHERE fromid!=#{userId} AND conversation_id=#{conversationId} AND is_read=0"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);

    @Select({"UPDATE ", TABLE_NAME, " SET is_read=1 WHERE conversation_id=#{conversationId}"})
    void updateUnreadStatus(@Param("conversationId") String conversationId);
}
