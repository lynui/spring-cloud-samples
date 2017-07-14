package self.spring.cloud.sample.dao.client;

import java.util.List;
import java.util.Map;

import self.spring.cloud.sample.dao.model.Employee;

public interface EmployeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Employee record);

    int insertSelective(Employee record);

    Employee selectByPrimaryKey(Integer id);
    
    List<Employee> selectAllByMap(Map<String,Object> paramMap);

    int updateByPrimaryKeySelective(Employee record);

    int updateByPrimaryKey(Employee record);
}