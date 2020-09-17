package xyz.seiyaya.apollo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjia
 * @version 1.0
 * @date 2020/9/16 16:26
 */
@RestController
@RequestMapping("/apollo")
@Slf4j
public class DemoController {

    @Value( "${server.port}" )
    private String port;

    @GetMapping("say")
    public String say(String name){
        log.debug( "debug log..." );
        log.info( "info log..." );
        log.warn( "warn log..." );

        return "hi " + name + " ,i am from port:" + port;
    }
}
