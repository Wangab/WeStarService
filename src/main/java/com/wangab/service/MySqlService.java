package com.wangab.service;

import com.wangab.BlowfishUtil;
import com.wangab.StringUtils;
import com.wangab.entity.ActivityVO;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Map;

/**
 * Created by wanganbang on 8/31/16.
 */
@Service
public class MySqlService {
    private static final Logger log = LoggerFactory.getLogger(MySqlService.class);

    private static final String DELETE_ACTIVITY_SQL = "DELETE FROM t_activity WHERE id = ?";
    private static final String UPDATE_USER_SQL = "UPDATE t_user SET passwd = ? WHERE uid =? and passwd = ?";
    private static final String QUERY_USER_SQL = "SELECT * FROM t_user WHERE uid=?";
    private static final String UPDATE_ACTIVITY_SQL = "UPDATE t_activity SET commission = ?,contact = ?,content = ?,end_time = ?,location = ?,number_people = ?,party_localtion = ?,pay_type = ?,phone_number = ?,start_time = ?,title = ? WHERE id = ?";
    private static final String SELECT_ACTIVITY_SQL = "SELECT id,commission,contact,content,end_time,location,number_people,party_localtion,pay_type,phone_number,start_time,title FROM t_activity WHERE id = ?";
    private MD5Digest md5Digest = new MD5Digest();

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private BlowfishUtil blowfishUtil;

    /**
     * 根据用户ID查询用户信息
     *
     * @param uid 用户ID
     * @return
     */
    public Map getUser(String uid) {
        return jdbcTemplate.queryForMap(QUERY_USER_SQL, uid);
    }

    /**
     * 更新用户密码
     *
     * @param uid    用户ID
     * @param oldpwd 老密码
     * @param newpwd 原密码
     * @return
     */
    public Boolean updatePWD(String uid, String oldpwd, String newpwd) {
        try {
            byte[] oldpwdbyts = oldpwd.getBytes();
            byte[] newpwdpwdbyts = newpwd.getBytes();

            md5Digest.update(oldpwdbyts, 0, oldpwdbyts.length);
            byte[] result = new byte[md5Digest.getDigestSize()];
            md5Digest.doFinal(result, 0);
            String md5pwdold = StringUtils.toHex(result);

            md5Digest.update(newpwdpwdbyts, 0, newpwdpwdbyts.length);
            byte[] result2 = new byte[md5Digest.getDigestSize()];
            md5Digest.doFinal(result2, 0);
            String md5pwdnew = StringUtils.toHex(result2);

            int num = jdbcTemplate.update(UPDATE_USER_SQL, md5pwdnew, uid, md5pwdold);
            if (num > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查用户Token
     *
     * @param token 用户Token
     * @param uid   用户ID
     * @return
     */
    public boolean checkToken(String token, String uid) {
        Object result = redisTemplate.opsForHash().get("westar:token:" + uid, "tokenStr");
//        long expireTime = redisTemplate.getExpire("westar:token:" + uid);
//        log.info(expireTime+"");
        if (result != null) {
            String[] redisResult = blowfishUtil.decryptString(result.toString()).split(",");
            String[] blowStrs = blowfishUtil.decryptString(token).split(",");
            log.info("从Redis获取token记录 -- " + result.toString() + "\t解密token Size -- " + blowStrs.length);
            if (redisResult.length == blowStrs.length && redisResult.length == 2) {
                if (redisResult[0].equals(blowStrs[0]) && redisResult[1].equals(blowStrs[1])) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 删除活动
     *
     * @param actid 要删除的活动ID
     * @return
     */
    public boolean deleteActivity(long actid) {
        return jdbcTemplate.execute(DELETE_ACTIVITY_SQL, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setLong(1, actid);
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

    /**
     * 跟新活动
     *
     * @param activity 保存用户信息的参数对象
     * @return
     */
    public boolean updateActivity(ActivityVO activity) {
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
