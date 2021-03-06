package com.wch.springcloud.controller;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.wch.springcloud.entities.CommonResult;
import com.wch.springcloud.entities.Payment;
import com.wch.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    @Resource
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String port;

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("*********插入的结果：" + result);
        if(result>0){
            return new CommonResult(200,"插入成功！Port:" + port,result);
        }else{
            return new CommonResult(444,"插入失败！", null);
        }

    }
    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("**********查询结果："+ payment);
        if(payment!=null){
            return new CommonResult(200, "查询成功！Port:" + port, payment);
        }else{
            return new CommonResult(444,"没有对应记录，查询ID:"+ id, null);
        }
    }

    @GetMapping("/payment/discovery")
    public Object discovery(){
        List<String> services = discoveryClient.getServices();
        for(String service : services){
            log.info("***********element" + service);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for(ServiceInstance instance : instances){
            log.info(instance.getHost()+ "\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return port;
    }

    @GetMapping("/payment/feign/timeout")
    public String paymentFeignTimeout(){
        try{
            TimeUnit.SECONDS.sleep(3);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return port;
        }
    }

    @GetMapping(value="/payment/zipkin")
    public String paymentZipkin() {
        return "hello,i am paymentZipkin server fallback,O(∩_∩)O哈哈~";
    }


}
