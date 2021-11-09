package com.mop.controller;

import com.alibaba.fastjson.JSONObject;
import com.mop.utils.HttpUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
public class PageController {

    public static final Logger log = Logger.getLogger(PageController.class);

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/requestAgent", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public JSONObject requestAgent(@RequestBody Map<String,Object> param){
        JSONObject jsonObject = new JSONObject();
        try {
            if (!param.isEmpty()) {
                String agentIp = param.get("agentIp").toString();
                String agentAction = param.get("agentAction").toString();
                String agentParam = param.get("agentParam").toString();
                StringBuilder sb = new StringBuilder();
                sb.append("http://").append(agentIp).append(":5000").append("/").append(agentAction);
                //追加参数
                if (!agentParam.equals("")) {
                    sb.append("/");
                }
                String url = sb.append(agentParam).toString();
                if (url.contains(" ")) {
                    String[] arr = url.split(" ");
                    StringBuilder hasSpaceUrl = new StringBuilder();
                    for (String s : arr) {
                        hasSpaceUrl.append(s).append("%20");
                    }
                    url = hasSpaceUrl.toString();
                }
                log.info(url);
                String result = HttpUtil.easyHttp_get_noHeader(url);
                return JSONObject.parseObject(result);
            } else {
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
