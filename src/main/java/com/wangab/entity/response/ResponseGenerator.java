package com.wangab.entity.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * ResponseUtils
 *
 * @author Anbang Wang
 * @date 2016/11/25
 */
public class ResponseGenerator {
    public static ResponseEntity<Map<String, Object>> customError(Map<String, Object> map, HttpStatus status) {
        return new ResponseEntity<Map<String, Object>>(map, status);
    }
    public static ResponseEntity<Map<String, Object>>  internalServerError(){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("retcode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put("retmsg", "inner server error");
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<Map<String,Object>> userNotFoundError() {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("retcode", HttpStatus.NOT_FOUND.value());
        result.put("retmsg", "User Not found");
        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
//        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<Map<String,Object>> success(Map<String, Object> map) {
        map.put("recode", 1);
        map.put("retmsg", "登录成功");
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
}
