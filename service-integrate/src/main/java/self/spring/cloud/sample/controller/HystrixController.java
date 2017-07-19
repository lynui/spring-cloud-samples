package self.spring.cloud.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import self.spring.cloud.sample.facade.HystrixFacade;

@RestController
public class HystrixController {

    @Autowired
    HystrixFacade hystrixFacade;

    @RequestMapping("/test/{fallback}")
    public String hello(@PathVariable("fallback") String fallback){
        String res=hystrixFacade.hystrix(fallback);
        return "调用服务结果为"+res;
    }
}