package com.baichen.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Program: Sms
 * @Author: baichen
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "templateId")//这里的templateId对应的就是sms.properties里的属性前缀
@PropertySource("classpath:sms.properties")  //这是属性文件路径
public class Sms {
}
