package com.wangab.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.wangab.dao.ActivityDAO;
import com.wangab.dao.EasemobDAO;
import com.wangab.dao.UserDAO;
import com.wangab.entity.enums.OtherAcountType;
import com.wangab.entity.po.EasemobPO;
import com.wangab.entity.vo.ActivityVO;
import com.wangab.entity.vo.OtherRegistVO;
import com.wangab.utils.BlowfishUtil;
import com.wangab.utils.MD5Utils;
import com.wangab.utils.StringUtils;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
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
    private EasemobDAO easemobDAO;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    public Map<String, Object> getOtherAcountLinking(String openid) {
        Map<String, Object> result = userDAO.getAcountLinking(openid);
        return result;
    }

    @Transactional(value = "jdbcTXManager")
    public Boolean addUser(OtherRegistVO regs) {
        Boolean isExist = userDAO.checkOpenidExisting(regs.getOpenid());
        if (isExist) {
            return false;
        }

        String sex = regs.getSex();
        if (sex != null) {
            if ("男".equals(sex.trim())) {
                sex = "M";
            } else if ("女".equals(sex.trim())) {
                sex = "F";
            } else if ("F".equals(sex.trim().toUpperCase())) {
                sex = "F";
            } else if ("M".equals(sex.trim().toUpperCase())) {
                sex = "M";
            } else if ("1".equals(sex.trim())) {
                sex = "M";
            } else if ("0".equals(sex.trim())) {
                sex = "F";
            }
        } else {
            sex = "F";
        }
        Integer loginid = userDAO.getLoginID();
        regs.setSex(sex);
        regs.setLoginName(loginid.toString());
        Long uid = userDAO.insertOtherUser(regs);
        int usnum = userDAO.insertOtherUserStatus(uid, 0, 0);
        int uanum = userDAO.insertOtherUserAuth(uid, OtherAcountType.valueOf(regs.getAccountType()).getIndex().toString(), regs.getOpenid());
        userDAO.updateLoginIDStatus(loginid);

        DBObject temp = new BasicDBObject();
        temp.put("uid", String.valueOf(uid));
        temp.put("group", "");
        temp.put("path", "");
        temp.put("url", regs.getIconUrl());
        temp.put("time", new java.util.Date());
        mongoTemplate.save(temp, "ws_user_icon");

        EasemobPO easemobPO = new EasemobPO();
        easemobPO.setNickname(regs.getNick());
        easemobPO.setPassword(MD5Utils.getMD5Code(regs.getOpenid()));
        easemobPO.setUsername(loginid.toString());
        String result = null;
        try {
            result = easemobDAO.addUser(easemobPO);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        log.debug("环信创建用户返回结果{}", result);
        log.debug("userDAO.insertOtherUser resultuid:{} userDAO.insertOtherUserStatus resultcount:{} userDAO.insertOtherUserAuth resultcount:{} loginid {} ", uid, usnum, uanum, loginid);
        return true;
    }

    public Map<String, Object> getUserSimpleInfomationByOpenid(String openid) {
        Map<String, Object> simplMap = new HashMap<>();
        Map<String, Object> accountLink = userDAO.getAcountLinking(openid);
        if (accountLink != null) {
            String uid = accountLink.get("uid").toString();
            List<Map<String, String>> icons = userDAO.getUserIcon(uid);
            Map user = userDAO.getUserMap(uid);
            simplMap.put("birthday", user.get("birthday"));
            simplMap.put("userID", user.get("uid"));
            simplMap.put("sex", user.get("sex"));
            simplMap.put("nickName", user.get("nick"));
            simplMap.put("longitude", user.get("location_longitude"));
            simplMap.put("latitude", user.get("location_latitude"));
            simplMap.put("photo", icons);
            simplMap.put("loginName", user.get("name"));
            return simplMap;
        }
        return null;
    }

    public String makeAccessToken(String uid,String openid) {
        String nowTime = System.currentTimeMillis() + "";
        String secstr = blowfishUtil.encryptString(uid + "," + nowTime);
        redisTemplate.opsForHash().put("westar:token:" + uid, "tokenStr", secstr);
        return secstr;
    }
}
