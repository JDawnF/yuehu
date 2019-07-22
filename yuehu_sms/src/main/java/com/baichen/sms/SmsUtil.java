package com.baichen.sms;

import com.baichen.sms.restDemo.client.AbsRestClient;
import com.baichen.sms.restDemo.client.AbsRestClient;
import com.baichen.sms.restDemo.client.JsonReqClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @program: lehuan_sms_service
 * @description: 短信工具类
 * @author: baichen
 * @create: 2018-12-02 21:13
 **/
@Component
public class SmsUtil {
    @Autowired
    private Environment env;
    static AbsRestClient InstantiationRestAPI() {
        return new JsonReqClient();
    }
    String result = "";


    public String SendSms(String templateid, String param, String mobile, String uid){
        String sid=env.getProperty("sid");
        String token=env.getProperty("token");
        String appid=env.getProperty("appid");
        try {
            result=InstantiationRestAPI().sendSms(sid, token, appid, templateid, param, mobile, uid);
            System.out.println("Response content is: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
