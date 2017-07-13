package self.spring.cloud.sample.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import self.spring.cloud.sample.dao.client.EmployeeMapper;
import self.spring.cloud.sample.dao.model.Employee;

@RestController
public class ComputeController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;
    @Autowired
    private EmployeeMapper employeeMapper;

    @RequestMapping(value = "/add" ,method = RequestMethod.GET)
    public String add(@RequestParam Integer a, @RequestParam Integer b) {
        ServiceInstance instance = client.getLocalServiceInstance();
        Integer r = a + b;
        logger.info("/add, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + r);
        return "From Service-A, Result is " + r;
    }
    @RequestMapping(value = "/queryEmployee" ,method = RequestMethod.GET)
    public Employee queryEmployee(){
    	Employee employee = employeeMapper.selectByPrimaryKey(1);
    	
    	System.out.println("employee:"+employee);
    	return employee;
    }

    @RequestMapping(value = "/queryEmployeeAll" ,method = RequestMethod.GET)
    public List<Employee> queryEmployeeAll(){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
//    	paramMap.put("offset", 1);
//    	paramMap.put("limit ", 2);
    	paramMap.put("pageNum", 1);
    	paramMap.put("pageSize ", 2);
    	
    	List<Employee> employees = employeeMapper.selectAll(paramMap);
    	
    	System.out.println("employee:"+employees);
    	return employees;
    }
}