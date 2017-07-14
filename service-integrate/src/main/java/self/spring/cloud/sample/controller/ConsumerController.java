package self.spring.cloud.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import self.spring.cloud.sample.dto.Employee;
import self.spring.cloud.sample.facade.ComputeFacade;

@RestController
public class ConsumerController {

    @Autowired
    ComputeFacade ComputeFacade;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return ComputeFacade.add(10, 20);
    }
    @RequestMapping(value = "/queryEmployee", method = RequestMethod.GET)
    public Employee queryEmployee() {
        return ComputeFacade.queryEmployee();
    }
}