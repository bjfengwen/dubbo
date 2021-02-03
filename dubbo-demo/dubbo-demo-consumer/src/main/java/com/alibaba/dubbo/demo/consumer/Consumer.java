/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.demo.consumer;

import com.alibaba.dubbo.demo.BarService;
import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Consumer {
    static Logger logger = LoggerFactory.getLogger(Consumer.class);
    public static void main(String[] args) {

       // while (true) {
            try {
//                try {
//                    demoService.sayHello(gen(1024000));
//                } catch (Exception e) {
//                }

//                String hello = demoService.sayHello("world"); // call remote method
//                System.out.println(hello); // get result


//                demoService.say01(null);
//                demoService.say01("TestException");
//                demoService.hello("01");
//                ((EchoService) demoService).$echo("test4u");
//                ((EchoService) demoService).$echo("test4u");

               //
//                ProtocolConfig.destroyAll();
                //RpcContext.getContext().getFuture();

//                demoService.say02();
//                demoService.say03();
//                demoService.say04();

                // 参数回调
                // https://dubbo.gitbooks.io/dubbo-user-book/demos/callback-parameter.html
//                demoService.callbackParam("shuaiqi", new ParamCallback() {
//                    @Override
//                    public void doSome(Cat msg) {
//                        System.out.println("回调biubiu：" + msg);
//                    }
//                });
                 getDemoService().sayHello("world");
//                demoService.bye(new Cat().setName("小猫"));
//                demoService.bye(new Dog().setAge(10));
                // asyncSayHello();
                // asyncSayHello2();
                // async();
                ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
                System.out.println(map.put("1", "1"));
                System.out.println(map.put("1", "2"));
                System.out.println(map.get("1"));
                Thread.sleep(10000);
            } catch (Exception e) {

                logger.error("",e);
            }


       // }

    }

    public static DemoService getDemoService() {
        //Prevent to get IPV6 address,this way only work in debug mode
        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-demo-consumer.xml"});
        context.start();
        DemoService demoService = (DemoService) context.getBean("demoService"); // get remote service proxy
        return demoService;
    }
    public static BarService getBarService() {
        //Prevent to get IPV6 address,this way only work in debug mode
        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-demo-consumer.xml"});
        context.start();
        return (BarService) context.getBean("barService"); // get remote service proxy

    }
    //https://www.jianshu.com/p/3a706e544874
    public static void asyncSayHello() throws ExecutionException, InterruptedException {

        String r = getDemoService().asyncSayHello("fw");
        System.out.println("等待结果之前:"+r);
        Future<String> future = RpcContext.getContext().getFuture();
        String result = future.get();
        System.out.println("等待结果:"+result);
    }

    public static void asyncSayHello2() throws ExecutionException, InterruptedException {

        String r = getDemoService().asyncSayHello("fw");
        System.out.println("等待结果之前2:"+r);
        FutureAdapter adapter = (FutureAdapter)RpcContext.getContext().getFuture();
        //4.回调函数
        adapter.getFuture().setCallback(new ResponseCallback() {
            @Override
            public void done(Object response) {
                System.out.println("回调函数:"+response);
            }

            @Override
            public void caught(Throwable exception) {

            }
        });
        System.out.println("main 结束");
    }

    //基于 JDK 1.6 的 Future，并不是真正意义上的异步，本质上还是阻塞的
    //2.7.0 为了支持真正的异步，用到了 JDK 1.8 的 CompletableFuture，也用到了 1.8 的 Supplier、Consumer 等操作符
    public static void async() throws ExecutionException, InterruptedException {
        long start=System.currentTimeMillis();
        BarService barService = getBarService();
        barService.add(1, 2);
        Future<Integer> sumRes = RpcContext.getContext().getFuture();
        barService.multi(12, 13);
        Future<Integer> multiRes = RpcContext.getContext().getFuture();
        //调用future.get()方法会使线程一直阻塞。
        System.out.println("sum:"+sumRes.get()+",multi:"+multiRes.get());
        long end=System.currentTimeMillis();
        System.out.println("总共耗时："+(end-start)/1000+"秒");
    }
    private static String gen(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append("s");
        }
        return sb.toString();
    }
}
