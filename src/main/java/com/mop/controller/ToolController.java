package com.mop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mop.entity.Json;
import com.mop.utils.HttpUtil;
import com.mop.utils.LogUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ToolController {

    private static final Logger log = Logger.getLogger(ToolController.class);

    @ResponseBody
    @RequestMapping(value = "/ckDecode",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public String ckDecode(@RequestBody Map<String,Object> map){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("bpStr",map.get("code"));
        String result = HttpUtil.doPost("http://172.31.64.151/ToolWeb/journalDecode",jsonObj.toJSONString());
        jsonObj = JSONObject.parseObject(result);
        result = jsonObj.get("decodeStr").toString();
        JSONObject jsonObject = JSONObject.parseObject(result);
        ArrayList<JSONObject> objects = new ArrayList<>();
        ArrayList<JSONArray> arrays = new ArrayList<>();
        JSONObject resultJson = new JSONObject();
        //最外层json or array
        Map<String,String> jsonType = new HashMap<>();
        for (Map.Entry<String,Object> entry : jsonObject.entrySet())
        {
            try {
                jsonType.put(entry.getKey(),"OBJECT");
                objects.add(JSONObject.parseObject(entry.getValue().toString()));
            }catch (JSONException | ClassCastException e){
                jsonType.put(entry.getKey(),"ARRAY");
                arrays.add(JSONArray.parseArray(entry.getValue().toString()));
            }
        }
        //json
        int jsonObjectIndex = 0,jsonArrayIndex = 0;
        for (Map.Entry<String,String> stringEntry : jsonType.entrySet())
        {
            if (stringEntry.getValue().equals("OBJECT"))
            {
                for (;jsonObjectIndex < objects.size();jsonObjectIndex++)
                {
                    resultJson.put(stringEntry.getKey(),objects.get(jsonObjectIndex));
                }
            }
            else
            {
                JSONArray jsonArray;
                JSONArray tempArray = new JSONArray();
                for (;jsonArrayIndex < arrays.size();jsonArrayIndex++)
                {
                    jsonArray = arrays.get(jsonArrayIndex);
                    for (Object o : jsonArray)
                    {
                        tempArray.add(JSONObject.parseObject(o.toString()));
                    }
                    resultJson.put(stringEntry.getKey(),tempArray);
                }
            }
        }
        return resultJson.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/zyDecode",produces = "application/json; charset=UTF-8",method = RequestMethod.POST)
    public String zyDecode(@RequestBody Map<String,Object> param) throws IOException{
        String decodeType = param.get("decodeType").toString();
        String json = HttpUtil.okHttp(decodeType,param.get("param").toString());
        log.info("zyDecode");
        return json;
    }

    @ResponseBody
    @RequestMapping(value = "/decodeJson",produces = "application/json; charset=UTF-8",method = RequestMethod.POST)
    public String decodeJson(@RequestBody Map<String,Object> param) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bpStr",param.get("code"));
        String result = HttpUtil.doPost("http://172.31.64.151/ToolWeb/journalDecode",jsonObject.toJSONString());
        jsonObject = JSONObject.parseObject(result);
        result = jsonObject.get("decodeStr").toString();
        log.info("decodeJson");
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/recordLog", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    public Json recordLog(@RequestBody Map<String,Object> param) {
        Json json = new Json();
        String ip = param.get("ip").toString();
        String deviceName = param.get("deviceName").toString();
        String content = param.get("content").toString();
        LogUtil.writeLog(ip,deviceName,content);
        json.setMsg("上报成功");
        return json;
    }

}
