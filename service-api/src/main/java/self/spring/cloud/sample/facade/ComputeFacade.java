package self.spring.cloud.sample.facade;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import self.spring.cloud.sample.hystrix.ComputeFacadeHystrix;

@FeignClient(value = "compute-service", fallback = ComputeFacadeHystrix.class)
public interface ComputeFacade {

    @RequestMapping(method = RequestMethod.GET, value = "/add")
    Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);

}