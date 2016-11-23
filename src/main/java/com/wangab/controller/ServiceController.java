package com.wangab.controller;

import com.wangab.entity.vo.ActivityVO;
import com.wangab.entity.vo.ChgPWDVO;
import com.wangab.entity.vo.OtherRegistVO;
import com.wangab.service.DataSourceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanganbang on 8/31/16.
 */
@RestController
@RequestMapping("/service")
public class ServiceController {
    @Resource
    DataSourceService dataSourceService;

    @RequestMapping(value = "/changepwd", method = RequestMethod.POST)
    @ApiImplicitParam(name = "accessToken", value = "API访问token", required = true, dataType = "string", paramType = "header")
    @ApiOperation(value = "更改密码接口", notes = "输入老密码、新密码，总体是一个json格式", produces = "application/json")
    public ResponseEntity<Map<String,Object>> changePWD(@RequestBody ChgPWDVO prams){
        Map<String,Object> result = new HashMap<String,Object>();
        boolean isOK = dataSourceService.updatePWD(prams.getUserid(), prams.getOldpwd(), prams.getNewpwd());
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

    @RequestMapping(value = "/{uid}/{actid}/delact", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除活动接口", notes = "输入活动相关数据，只需要活动id")
    @ApiImplicitParam(name = "accessToken", value = "API访问token", required = true, dataType = "string", paramType = "header")
    public ResponseEntity<Map<String, Object>> deleteActivity(@PathVariable("actid") String actid, @PathVariable("uid") String uid) {
        Map<String,Object> result = new HashMap<String,Object>();
        long activityId = Long.valueOf(actid);
        long userid = Long.valueOf(uid);
        boolean isOK = dataSourceService.deleteActivity(activityId, userid);
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
    @ApiOperation(value = "修改活动接口", notes = "输入活动相关数据，活动id必须有，其他的可以选择输入最低要有一项，请求体是一个json格式", produces = "application/json")
    @ApiImplicitParam(name = "accessToken", value = "API访问token", required = true, dataType = "string", paramType = "header")
    public ResponseEntity<Map<String, Object>> updateActivity(@RequestBody ActivityVO activity) {
        Map<String,Object> result = new HashMap<String,Object>();
        long actid = activity.getId();
        boolean isOK = dataSourceService.updateActivity(activity);
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

    public ResponseEntity<Map<String, Object>> otherRegist(@Validated OtherRegistVO regs) {
        Map<String,Object> result = new HashMap<String,Object>();
        String openid = regs.getOpenid();
        //逻辑代码
        Map<String,Object> acountLinking = dataSourceService.getOtherAcountLinking(openid);
        if (acountLinking != null) {
            result.put("retcode",-45);
            result.put("retmsg", "Account already exists");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CONFLICT);
        }
        //插入
        try {
            dataSourceService.addUser(regs);
            result.put("retcode",1);
            result.put("retmsg", "success");
        } catch (Exception e){
            result.put("retcode",-45);
            result.put("retmsg", "inner error");
            return new ResponseEntity<Map<String, Object>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> otherAuth() {
        Map<String,Object> result = new HashMap<String,Object>();

        //逻辑代码

        return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
    }
}
