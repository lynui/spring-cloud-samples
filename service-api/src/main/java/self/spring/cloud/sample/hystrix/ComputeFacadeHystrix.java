package self.spring.cloud.sample.hystrix;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import self.spring.cloud.sample.dto.Employee;
import self.spring.cloud.sample.facade.ComputeFacade;

@Component
public class ComputeFacadeHystrix implements ComputeFacade {

    public String add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b) {
        return "-9999";
    }

	public Employee queryEmployee() {
		return null;
	}

}