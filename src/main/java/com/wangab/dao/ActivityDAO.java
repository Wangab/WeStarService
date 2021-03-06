package com.wangab.dao;

import com.wangab.entity.vo.ActivityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * ActivityDAO
 *
 * @author Anbang Wang
 * @date 2016/11/23
 */
@Repository("activityDAO")
public class ActivityDAO {
    private static final Logger log = LoggerFactory.getLogger(ActivityDAO.class);

    private static final String UPDATE_ACTIVITY_SQL = "UPDATE t_activity SET commission = ?,contact = ?,content = ?,end_time = ?,location = ?,number_people = ?,party_localtion = ?,pay_type = ?,phone_number = ?,start_time = ?,title = ? WHERE id = ? AND push_uid=?";
    private static final String SELECT_ACTIVITY_SQL = "SELECT id,commission,contact,content,end_time,location,number_people,party_localtion,pay_type,phone_number,start_time,title FROM t_activity WHERE id = ?";
    private static final String DELETE_ACTIVITY_SQL = "DELETE FROM t_activity WHERE id = ? AND push_uid=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean deleteActvity(long actid, long uid) {
        return jdbcTemplate.execute(DELETE_ACTIVITY_SQL, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setLong(1, actid);
                preparedStatement.setLong(2, uid);
                try {
                    preparedStatement.execute();
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    public boolean updateActvity(ActivityVO activity) {
        long id = activity.getId();
        ActivityVO result = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement pst = connection.prepareStatement(SELECT_ACTIVITY_SQL);
                pst.setLong(1, id);
                return pst;
            }
        }, new ResultSetExtractor<ActivityVO>() {
            @Override
            public ActivityVO extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String title = resultSet.getString("title");
                    String contents = resultSet.getString("content");
                    Date startTime = resultSet.getDate("start_time");
                    Date endTime = resultSet.getDate("end_time");
                    Double commission = resultSet.getDouble("commission");
                    Integer number = resultSet.getInt("number_people");
                    String location = resultSet.getString("location");
                    String partyLocation = resultSet.getString("party_localtion");
                    String payType = resultSet.getString("pay_type");
                    String contact = resultSet.getString("contact");
                    String phone = resultSet.getString("phone_number");
                    return new ActivityVO(id, title, contents, startTime, endTime, commission, number, location, partyLocation, payType, contact, phone);
                }
                return null;
            }
        });
        log.info("查询到活动 -- " + result);
        Double commission = activity.getCommission();
        log.info("activity commission -- " + commission);
        String contact = activity.getContact();
        log.info("activity contact -- " + contact);
        String contents = activity.getContents();
        log.info("activity contents -- " + contents);
        Date endTime = activity.getEndTime();
        log.info("activity endTime -- " + endTime);
        String location = activity.getLocation();
        log.info("activity location -- " + location);
        Integer num = activity.getNumber();
        log.info("activity num -- " + num);
        String partyLocation = activity.getPartyLocation();
        log.info("activity partyLocation -- " + partyLocation);
        String payType = activity.getPayType();
        log.info("activity payType -- " + payType);
        String phone = activity.getPhone();
        log.info("activity phone -- " + phone);
        Date startTime = activity.getStartTime();
        log.info("activity startTime -- " + startTime);
        String title = activity.getTitle();
        log.info("activity title -- " + title);

        if (result != null) {
            jdbcTemplate.update(UPDATE_ACTIVITY_SQL, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setLong(12, id);
                    preparedStatement.setLong(13, Long.valueOf(activity.getUserid()));
                    if (commission != null && commission != 0) {
                        preparedStatement.setDouble(1, commission);
                    } else {
                        preparedStatement.setDouble(1, result.getCommission() == null ? 0:result.getCommission());
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(contact)){
                        preparedStatement.setString(2, contact);
                    } else {
                        preparedStatement.setString(2, result.getContact() == null ? "空":result.getContact());
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(contents)){
                        preparedStatement.setString(3, contents);
                    } else {
                        preparedStatement.setString(3, result.getContents() == null ? "空":result.getContents());
                    }
                    if (endTime != null){
                        preparedStatement.setDate(4, endTime);
                    } else {
                        preparedStatement.setDate(4, result.getEndTime() == null ? new Date(System.currentTimeMillis()):result.getEndTime());
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(location)){
                        preparedStatement.setString(5, location);
                    } else {
                        preparedStatement.setString(5, result.getLocation() == null ? "空":result.getLocation());
                    }
                    if (num != null && num != 0){
                        preparedStatement.setInt(6, num);
                    } else {
                        preparedStatement.setInt(6, result.getNumber());
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(partyLocation)){
                        preparedStatement.setString(7, partyLocation);
                    } else {
                        preparedStatement.setString(7, result.getPartyLocation() == null ? "空":result.getPartyLocation());
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(payType)){
                        preparedStatement.setString(8, payType);
                    } else {
                        preparedStatement.setString(8, result.getPayType() == null ? "空":result.getPayType());
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(phone)){
                        preparedStatement.setString(9, phone);
                    } else {
                        preparedStatement.setString(9, result.getPhone() == null ? "空":result.getPhone());
                    }
                    if (startTime != null){
                        preparedStatement.setDate(10, startTime);
                    } else {
                        preparedStatement.setDate(10, result.getStartTime() == null ? new Date(System.currentTimeMillis()):result.getStartTime());
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(title)){
                        preparedStatement.setString(11, title);
                    } else {
                        preparedStatement.setString(11, result.getTitle() == null ? "空":result.getTitle());
                    }
                }
            });
            return true;
        } else {
            return false;
        }
    }
}
