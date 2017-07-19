package self.spring.cloud.sample.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class HystrixController {

    private final Logger logger = Logger.getLogger(getClass());

    @RequestMapping(value = "/test/{fallback}" ,method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod="hystrixFallbackMethod", threadPoolProperties = {  
            @HystrixProperty(name = "coreSize", value = "30"), @HystrixProperty(name = "maxQueueSize", value = "100"),  
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "20") }, commandProperties = {  
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100"),  
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "50")  
  
    })/*调用方式失败后调用hystrixFallbackMethod*/
    public String hystrix(@PathVariable("fallback") String fallback) {
    	if("1".equals(fallback)){
            throw new RuntimeException("...");
        }
        return "hello";
    }

    public String hystrixFallbackMethod(String fallback){
        return "fallback 参数值为:"+fallback;
    }

}