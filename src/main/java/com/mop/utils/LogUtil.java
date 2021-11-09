package com.mop.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    private static final Logger log = Logger.getLogger(LogUtil.class);
    private static final boolean DOMAIN = true;

    /**
     * 生成日志
     * @param ip ip（windows执行机的ip）
     * @param deviceName 设备名称
     * @param content 保存的内容
     */
    public static void writeLog(String ip, String deviceName, String content){
        Date date = new Date();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String pathname;
        if (DOMAIN) {
            pathname = "/data/EMULATOR_LOG/" + today + "/" + ip + "/";
        } else {
            pathname = "E:\\EMULATOR_LOG\\" + today + "\\" + ip + "\\";
        }
        File dir = new File(pathname);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                log.info("目录创建成功");
            } else {
                log.info("目录创建失败");
            }
        }
        try {
            String finalPath = pathname + deviceName + ".txt";
            File file = new File(finalPath);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    log.info("文件创建成功");
                } else {
                    log.info("文件创建失败");
                }
            }
            FileWriter writer = new FileWriter(finalPath, true);
            writer.write(content + "\r\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        writeLog("172.31.66.133","emulator-5554","heihei");
    }

}
