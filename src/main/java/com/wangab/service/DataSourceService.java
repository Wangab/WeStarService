package com.wangab.service;

import com.wangab.BlowfishUtil;
import com.wangab.StringUtils;
import com.wangab.dao.ActivityDAO;
import com.wangab.dao.UserDAO;
import com.wangab.entity.vo.ActivityVO;
import com.wangab.entity.vo.OtherRegistVO;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.Map;

/**
 * Created by wanganbang on 8/31/16.
 */
@Service
public class DataSourceService {
    private static final Logger log = LoggerFactory.getLogger(DataSourceService.class);
    private MD5Digest md5Digest = new MD5Digest();

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ActivityDAO activityDAO;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BlowfishUtil blowfishUtil;

    /**
     * 根据用户ID查询用户信息
     *
     * @param uid 用户ID
     * @return
     */
    public Map getUser(String uid) {
        return userDAO.getUserMap(uid);
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
            int num = userDAO.updateUserPassword(md5pwdnew, uid, md5pwdold);
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
    public boolean deleteActivity(long actid, long uid) {
        return activityDAO.deleteActvity(actid, uid);
    }

    /**
     * 跟新活动
     *
     * @param activity 保存用户信息的参数对象
     * @return
     */
    public boolean updateActivity(ActivityVO activity) {
        return activityDAO.updateActvity(activity);
    }

    public Map<String,Object> getOtherAcountLinking(String openid) {

        return null;
    }

    @Transactional(value = "jdbcTXManager")
    public Boolean addUser(OtherRegistVO regs) {
        Boolean isExist = userDAO.checkOpenid(regs.getOpenid());
        if (isExist) {
            return false;
        }

        String sex = regs.getSex();
        if (sex != null) {
            if ("男".equals(sex)){
                sex = "M";
            } else if("女".equals(sex)) {
                sex = "F";
            } else if ("F".equals(sex.trim().toUpperCase())){
                sex = "F";
            } else if ("M".equals(sex.trim().toUpperCase())){
                sex = "M";
            }
        } else {
            sex = "F";
        }
        regs.setSex(sex);
        Long uid = userDAO.insertOtherUser(regs);
        int usnum = userDAO.insertOtherUserStatus(uid, 0, 0);
        int uanum = userDAO.insertOtherUserAuth(uid,0,regs.getOpenid());
        log.debug("userDAO.insertOtherUser resultuid:{} userDAO.insertOtherUserStatus resultcount:{} userDAO.insertOtherUserAuth resultcount:{}",uid,usnum,uanum);
        return true;
    }
}
