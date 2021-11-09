package com.mop.controller;

import com.mop.entity.Json;
import com.mop.entity.Phone;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Controller
public class PhoneController {

    private static final Logger log = Logger.getLogger(PhoneController.class);

    @RequestMapping("/getPhone")
    public String getPhone(Model model) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("adb devices -l");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        ArrayList<Phone> phones = new ArrayList<>();
        Phone phone;
        String str;
        while((str = br.readLine()) != null){
            if (str.contains("device product")) {
                phone = new Phone();
                phone.setSid(str.replaceAll(" +","").split("deviceproduct")[0]);
                log.info(phone.getSid());
                phones.add(phone);
            }
        }
        int deviceCount = phones.size();
        log.info("当前连接设备：" + deviceCount);
        br.close();
        process.waitFor();
        model.addAttribute("phones", phones);
        model.addAttribute("deviceCount", deviceCount);
        return "phoneIndex";
    }

    @ResponseBody
    @RequestMapping(value = "/closeScreen",produces = "application/json; charset=UTF-8")
    public Json closeScreen(@RequestParam(name = "sid")String sid) throws IOException {
        Json j = new Json();
        Process process = Runtime.getRuntime().exec("adb -s " + sid + " shell input keyevent 26");
        return j;
    }

}
