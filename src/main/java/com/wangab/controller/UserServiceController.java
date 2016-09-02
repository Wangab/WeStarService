package com.wangab.controller;

import com.wangab.entity.ActivityVO;
import com.wangab.entity.ChgPWDVO;
import com.wangab.service.MySqlService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanganbang on 8/31/16.
 */
@RestController
@RequestMapping("/users")
public class UserServiceController {
    @Resource
    MySqlService mySqlService;

    @RequestMapping(value = "/changepwd", method = RequestMethod.POST)
    @ApiImplicitParam(name = "accessToken", value = "API访问token", required = true, dataType = "string", paramType = "header")
    @ApiOperation(value = "更改密码接口", notes = "输入老密码、新密码，总体是一个json格式", produces = "application/json")
    public ResponseEntity<Map<String,Object>> changePWD(@RequestBody ChgPWDVO prams){
        Map<String,Object> result = new HashMap<String,Object>();
        boolean isOK = mySqlService.updatePWD(prams.getUserid(), prams.getOldpwd(), prams.getNewpwd());
        if (isOK) {
            result.put("retcode",1);
            result.put("retmsg", "success");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
        } else {
            result.put("retcode",-45);
            result.put("retmsg", "old password is error");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delact", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除活动接口", notes = "输入活动相关数据，只需要活动id即可，其他的不用输入，请求体是一个json格式", produces = "application/json")
    @ApiImplicitParam(name = "accessToken", value = "API访问token", required = true, dataType = "string", paramType = "header")
    public ResponseEntity<Map<String, Object>> deleteActivity(@RequestBody ActivityVO activity) {
        Map<String,Object> result = new HashMap<String,Object>();
        long actid = activity.getId();
        boolean isOK = mySqlService.deleteActivity(actid);
        if (isOK) {
            result.put("retcode",1);
            result.put("retmsg", "success");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
        } else {
            result.put("retcode",-45);
            result.put("retmsg", "old password is error");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateact", method = RequestMethod.PUT)
    @ApiOperation(value = "删除活动接口", notes = "输入活动相关数据，活动id必须有，其他的可以选择输入最低要有一项，请求体是一个json格式", produces = "application/json")
    @ApiImplicitParam(name = "accessToken", value = "API访问token", required = true, dataType = "string", paramType = "header")
    public ResponseEntity<Map<String, Object>> updateActivity(@RequestBody ActivityVO activity) {
        Map<String,Object> result = new HashMap<String,Object>();
        long actid = activity.getId();
        boolean isOK = mySqlService.updateActivity(activity);
        if (isOK) {
            result.put("retcode",1);
            result.put("retmsg", "success");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
        } else {
            result.put("retcode",-45);
            result.put("retmsg", "old password is error");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
        }
    }
}
