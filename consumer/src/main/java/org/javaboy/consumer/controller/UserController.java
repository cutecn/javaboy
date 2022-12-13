package org.javaboy.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {
    @Autowired
    @Qualifier(value = "restTemplate")  //指定注入的文件名
    RestTemplate restTemplate;
    @Autowired
    DiscoveryClient discoveryClient;
    //    此时provider在eureka上有两个，采用线性负载均衡的操作方法

//    验证RestTemplate中GET方法getForObject()返回值 getForEntity()返回值+响应头 重载
    @GetMapping("hello4")
    public void hello4() {
        String s1 = restTemplate.getForObject("http://provider/hello5?name={1}", String.class, "javaboy");
        System.out.println(s1);
//        获取响应体
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://provider/hello5", String.class, "javaboy");
        String body = responseEntity.getBody();
        System.out.println("body:" + body);
//        获取响应码
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println("HttpStatus:" + statusCode);
//        获取状态码值
        int statusCodeValue = responseEntity.getStatusCodeValue();
        System.out.println("statusCodeValue" + statusCodeValue);
//        获取响应头信息
        HttpHeaders headers = responseEntity.getHeaders();
        Set<String> keySet = headers.keySet();
        System.out.println("------------header------------");
        for (String s : keySet) {
            System.out.println(s + ":" + headers.get(s));
        }
    }

    int count = 0;
    @GetMapping("/hello3")
    public String hello3() {
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
//      从list列表中随机取出一个 provider 采用取余操作，结果为0 1 0 1 0 1
        ServiceInstance instance = list.get((count++) % list.size());
        String host = instance.getHost();
        int port = instance.getPort();
        StringBuffer sb = new StringBuffer();
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
//        使用RestTemplate来替代http请求，实现代码简化
//        HttpURLConnection con = null;
//        try {
//            URL url = new URL(sb.toString());
//            con = ((HttpURLConnection) url.openConnection());
//            if (con.getResponseCode() == 200) {
//                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String s = bf.readLine();
//                bf.close();
//                return s;
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        用RestTemplate，一行代码实现了http调用
        String s = restTemplate.getForObject(sb.toString(), String.class);
        return s;
    }

//    此时provider在eureka上只有一个
//    @GetMapping("/hello2")
//    public String hello2() {
//      List<ServiceInstance> list =  discoveryClient.getInstances("provider");
//        ServiceInstance instance = list.get(0);
//        String host = instance.getHost();
//        int port = instance.getPort();
//        StringBuffer sb = new StringBuffer();
//        sb.append("http://")
//                .append(host)
//                .append(":")
//                .append(port)
//                .append("/hello");
//        HttpURLConnection con = null;
//        try {
//            URL url = new URL(sb.toString());
//            con = ((HttpURLConnection) url.openConnection());
//            if (con.getResponseCode() == 200) {
//                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String s = bf.readLine();
//                bf.close();
//                return s;
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return "error";
//    }
}

//固定写死的访问方式，从consumer消费provider的提供方
//@RestController
//public class UserController {
//    @GetMapping("/hello1")
//    public String hello1() {
////        使用HttpURLConnection连接方式
//        HttpURLConnection con = null;
//        try {
////            固定访问地址
//            URL url = new URL("http://localhost:1113/hello");
////            打开访问连接
//            con = (HttpURLConnection) url.openConnection();
////            判断返回响应码
//            if (con.getResponseCode() == 200) {
////                从Buffer缓存流中读取输入的Stream流中的连接数据
//                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
////                将缓存流中数据写成字符串
//                String s = br.readLine();
////                关闭字符流
//                br.close();
//                return s;
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return "error";
//    }
//}
