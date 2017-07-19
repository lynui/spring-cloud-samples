package self.spring.cloud.sample.facade;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "compute-service")
public interface HystrixFacade {

    @RequestMapping(value = "/test/{fallback}" ,method = RequestMethod.GET)
    public String hystrix(@PathVariable("fallback") String fallback);

}