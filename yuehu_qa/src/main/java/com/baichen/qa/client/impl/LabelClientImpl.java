package com.baichen.qa.client.impl;

import com.baichen.entity.Contants;
import com.baichen.entity.Result;
import com.baichen.entity.StatusCode;
import com.baichen.qa.client.LabelClient;
import org.springframework.stereotype.Component;

/**
 * @Program: LabelClientImpl
 * @Author: baichen
 * @Description: 实现熔断器
 */
@Component
public class LabelClientImpl implements LabelClient {

    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.ERROR, Contants.OPEN_HYSTRIX);
    }
}
