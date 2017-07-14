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

import com.github.pagehelper.PageInfo;

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
    public PageInfo queryEmployeeAll(){
    	Map<String, Object> paramMap = new HashMap<String, Object>();
//    	paramMap.put("pageNum", 3);
//    	paramMap.put("pageSize", 2);
    	paramMap.put("offset", 3);
    	paramMap.put("limit", 2);
    	List<Employee>  employees = employeeMapper.selectAllByMap(paramMap);
    	PageInfo page = new PageInfo(employees);
    	System.out.println("employee:"+employees);
    	
    	return page;
    }

}