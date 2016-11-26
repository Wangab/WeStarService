package com.wangab.dao;

import com.wangab.utils.StringUtils;
import com.wangab.entity.vo.OtherRegistVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserDAO
 *
 * @author Anbang Wang
 * @date 2016/11/23
 */
@Repository("userDAO")
public class UserDAO {
    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

    private static final String UPDATE_USER_SQL = "UPDATE t_user SET passwd = ? WHERE uid =? and passwd = ?";
    private static final String QUERY_USER_SQL = "SELECT * FROM t_user WHERE uid=?";
    private static final String QUERY_USER_BY_PHONE_SQL = "SELECT * FROM t_user WHERE phone=?";
    private static final String INSERT_USER_SQL = "INSERT INTO t_user(name,nick,passwd,sex,signature,haunted,registration_time,phone,location_longitude,location_latitude) VALUES (?,?,?,?,?,?,?,?,3395400,11695400)";
    private static final String INSERT_USER_STATUS_SQL = "INSERT INTO t_user_status (uid,numstatus,warnflag) VALUES (?,?,?)";
    private static final String INSERT_USER_AUTH_SQL = "INSERT INTO t_auth (uid,publictype,openid) VALUES (?,?,?)";
    private static final String QUERY_AUTHTYPE_SQL = "SELECT * FROM t_auth WHERE openid=?";
    private static final String QUERY_LOGIN_ID_SQL = "SELECT inde FROM t_index WHERE status=0 ORDER BY id ASC limit 1";
    private static final String UPDATE_LOGIN_STAUS_SQL = "UPDATE t_index SET status = 1 WHERE inde =?";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Map getUserMap(String uid) {
        return jdbcTemplate.queryForMap(QUERY_USER_SQL, uid);
    }

    public int updateUserPassword(String md5pwdnew, String uid, String md5pwdold) {
        int num = jdbcTemplate.update(UPDATE_USER_SQL, md5pwdnew, uid, md5pwdold);
        return num;
    }

    /**
     * 检查手机是否存在
     *
     * @param phone
     * @return
     */
    public Boolean checkTempPhoneExisting(String phone) {
        return jdbcTemplate.query(QUERY_USER_BY_PHONE_SQL, new ResultSetExtractor<Boolean>() {
            @Override
            public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next()) {
                    return true;
                }
                return false;
            }
        }, phone);
    }

    private String getTempString() {
        String tempPhoneStr = StringUtils.createId();
        if (checkTempPhoneExisting(tempPhoneStr)) {
            getTempString();
        }
        return tempPhoneStr;
    }

    public Long insertOtherUser(OtherRegistVO regs) {
        String tempPhoneStr = getTempString();
        KeyHolder userKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, regs.getLoginName());
                preparedStatement.setString(2, regs.getNick());
                preparedStatement.setString(3, regs.getOpenid());
                preparedStatement.setString(4, regs.getSex());
                preparedStatement.setString(5, regs.getSignature() == null ? "-" : regs.getSignature());
                preparedStatement.setString(6, regs.getAddr() == null ? "北京" : regs.getAddr());
                preparedStatement.setDate(7, new Date(System.currentTimeMillis()));
                preparedStatement.setString(8, tempPhoneStr);
                return preparedStatement;
            }
        }, userKeyHolder);
        long uid = userKeyHolder.getKey().longValue();

        return uid;
    }

    /**
     * 插入用户状态
     *
     * @param uid
     * @param statusNum
     * @param warnNum
     * @return
     */
    public int insertOtherUserStatus(Long uid, int statusNum, int warnNum) {
        KeyHolder userStatusKeyHolder = new GeneratedKeyHolder();

        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_STATUS_SQL);
                preparedStatement.setLong(1, uid);
                preparedStatement.setInt(2, statusNum);
                preparedStatement.setInt(3, warnNum);
                return preparedStatement;
            }
        });
    }

    /**
     * 插入用户auth方式
     *
     * @param uid
     * @param type
     * @param openid
     * @return
     */
    public int insertOtherUserAuth(Long uid, String type, String openid) {
        KeyHolder userAuthTypeKeyHolder = new GeneratedKeyHolder();
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_AUTH_SQL);
                preparedStatement.setLong(1, uid);
                preparedStatement.setString(2, type);
                preparedStatement.setString(3, openid);
                return preparedStatement;
            }
        });
    }

    public Boolean checkOpenidExisting(String openid) {
        if (getAcountLinking(openid) != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取第三方用户账户链接
     *
     * @param openid
     * @return
     */
    public Map<String, Object> getAcountLinking(String openid) {
        return jdbcTemplate.query(QUERY_AUTHTYPE_SQL, new ResultSetExtractor<Map<String, Object>>() {
            @Override
            public Map<String, Object> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next()) {
                    Long uid = resultSet.getLong("uid");
                    int authType = resultSet.getInt("publictype");
                    String openid = resultSet.getString("openid");
                    Map<String, Object> rst = new HashMap<String, Object>();
                    rst.put("uid", uid);
                    rst.put("authType", authType);
                    rst.put("openid", openid);
                    return rst;
                }
                return null;
            }
        }, openid);
    }

    public Integer getLoginID() {
        return jdbcTemplate.query(QUERY_LOGIN_ID_SQL, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getInt("inde");
                }
                throw new SQLException("Numbers are used up");
            }
        });
    }

    public Integer updateLoginIDStatus(Integer id) {
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LOGIN_STAUS_SQL);
                preparedStatement.setInt(1, id);
                return preparedStatement;
            }
        });
    }

    public List<Map<String, String>> getUserIcon(String uid) {
        List<Map<String, String>> list = new ArrayList<>();
        Map iconMap = mongoTemplate.findOne(Query.query(Criteria.where("uid").is(uid)), Map.class, "ws_user_icon");
        String url = iconMap.get("url").toString();
        Map<String, String> result = new HashMap<>();
        result.put("source", url);
        result.put("small", url);
        result.put("large", url);
        return list;
    }
}
