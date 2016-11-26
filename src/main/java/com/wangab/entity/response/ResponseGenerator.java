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
}
