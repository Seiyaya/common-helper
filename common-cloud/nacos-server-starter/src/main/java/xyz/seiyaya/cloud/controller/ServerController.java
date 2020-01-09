package xyz.seiyaya.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xyz.seiyaya.common.bean.ResultBean;

import javax.annotation.Resource;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/1/9 9:54
 */
@RestController
@Slf4j
public class ServerController {

    @Resource
    private LoadBalancerClient loadBalancerClient;

    @GetMapping
    public ResultBean serverIndex(){
        ServiceInstance choose = loadBalancerClient.choose("alibaba-nacos-discovery-client");
        String uri = choose.getUri()+"/index?name=seiyaya";
        RestTemplate restTemplate = new RestTemplate();
        ResultBean resultBean = restTemplate.getForObject(uri, ResultBean.class);
        log.info("invoke client success:{}",resultBean);
        return resultBean;
    }
}
