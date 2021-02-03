package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.BarService;

/**
 * @Description
 * @Author fengwen
 * @Date 2021-01-15
 */
public class BarServiceImpl implements BarService {
    @Override
    public String biubiubiu(String msg) {
        return null;
    }

    public int add(Integer x, Integer y) {
        try{
            Thread.sleep(3000);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return x+y;
    }

    public int multi(Integer x, Integer y) {
        try{
            Thread.sleep(5000);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return x*y;
    }
}
