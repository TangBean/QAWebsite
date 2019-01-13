package com.demo.dao;

import com.demo.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginTicketDao {
    String TABLE_NAME = "login_ticket";

    String INSERT_FIELD = "user_id,ticket,expired";

    String SELECT_FIELD = "id,user_id,ticket,expired,status";

    /**
     * 添加ticket
     * @param ticket
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELD, ")" +
            "values(#{userId},#{ticket},#{expired})"})
    int addTicket(LoginTicket ticket);

    /**
     * 通过ticket查找
     * @param ticket
     * @return
     */
    @Select({"select ", SELECT_FIELD, "from ", TABLE_NAME, "where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    /**
     * 通过ticket更新ticket的status
     * @param ticket
     * @param newStatus
     */
    @Update({"update ", TABLE_NAME, "set status=#{newStatus} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("newStatus") int newStatus);
}
