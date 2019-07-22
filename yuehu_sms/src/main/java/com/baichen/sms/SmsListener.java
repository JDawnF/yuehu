package com.baichen.sms;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Program: SmsListener
 * @Author: baichen
 * @Description: 短信监听类
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    /**
     * 发送短信
     *
     * @param map
     */
    @RabbitHandler
    public void sendSms(Map<String, String> map) {
        System.out.println("手机号:" + map.get("mobile"));
        System.out.println("验证码:" + map.get("code"));
        String result = null;
        try {
            result = smsUtil.SendSms(
                    map.get("templateId"),
                    map.get("code"),
                    map.get("mobile"),
                    map.get("uid")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}
