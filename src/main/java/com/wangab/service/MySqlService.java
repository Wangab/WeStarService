package com.wangab.service;

import com.wangab.BlowfishUtil;
import com.wangab.StringUtils;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by wanganbang on 8/31/16.
 */
@Service
public class MySqlService {
    private static final Logger log = LoggerFactory.getLogger(MySqlService.class);

    private static final String UPDATE_USER_SQL = "UPDATE t_user SET passwd = ? WHERE uid =? and passwd = ?";
    private static final String QUERY_USER_SQL = "SELECT * FROM t_user WHERE uid=?";
    private MD5Digest md5Digest = new MD5Digest();

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private BlowfishUtil blowfishUtil;

    public Map getUser(String uid){
        return jdbcTemplate.queryForMap(QUERY_USER_SQL, uid);
    }
    public Boolean updatePWD(String uid, String oldpwd, String newpwd){
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
            if(num>0){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean checkToken(String token, String uid) {
        Object result = redisTemplate.opsForHash().get("westar:token:" + uid, "tokenStr");
//        long expireTime = redisTemplate.getExpire("westar:token:" + uid);
//        log.info(expireTime+"");
        if (result != null ) {
            String[] redisResult = blowfishUtil.decryptString(result.toString()).split(",");
            String[] blowStrs = blowfishUtil.decryptString(token).split(",");
            log.info("从Redis获取token记录 -- " + result.toString() + "\t解密token Size -- " + blowStrs.length);
            if(redisResult.length == blowStrs.length && redisResult.length == 2) {
                if (redisResult[0].equals(blowStrs[0]) && redisResult[1].equals(blowStrs[1])) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
