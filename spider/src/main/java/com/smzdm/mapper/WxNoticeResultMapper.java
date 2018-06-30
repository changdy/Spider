package com.smzdm.mapper;

import com.smzdm.pojo.WxNoticeResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Changdy on 2018/6/30.
 */
public interface WxNoticeResultMapper {

    @ResultMap("BaseResultMap")
    @Select("SELECT id,send_msg,event_time,response,success FROM wx_notice_result")
    List<WxNoticeResult> selectAll();

    @Insert("insert into wx_notice_result (send_msg,event_time,response,success)  values (#{sendMsg,jdbcType=VARCHAR},#{eventTime,jdbcType=DATE},#{response,jdbcType=VARCHAR},#{success,jdbcType=BIT})")
    void insert(WxNoticeResult wxNoticeResult);
}
