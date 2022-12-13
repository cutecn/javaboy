package org.javaboy.provider.controller;

import org.bouncycastle.asn1.esf.SPuri;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//注意，DiscoveryClient查询到的服务列表是一个集合，
// 因为服务在部署的过程中，可能是集群化部署，集合中的每一项就是一个实例
@RestController
public class HelloController {
    @Value("${server.port}")
    Integer port;

    @GetMapping("/hello")
    public String hello() {
        return "hello provider" + ": " + port;
    }

    @GetMapping("/hello5")
    public String hello5(String name) {
        return "hello" + name;
    }

//    @GetMapping("/hello")
//    public String hello() {
//        return "hello java";
//    }
}
