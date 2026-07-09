package org.example.springdemo3.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.example.springdemo3.pojo.Emp;
import org.example.springdemo3.pojo.EmpPageListParams;
import org.example.springdemo3.pojo.EmpPageVO;

import java.util.List;

@Mapper
public interface EmpMapper {

    Integer getCount(EmpPageListParams params);

    List<EmpPageVO> getEmpListByPage(EmpPageListParams params);

    @Insert("insert into emp(username, password, name, gender, phone, job, salary, image, entry_date, create_time, update_time, dept_id) " +
            "values(#{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{createTime}, #{updateTime}, #{deptId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addEmp(Emp emp);

    @Insert("insert into emp_expr(emp_id, begin_date, end_date, company, job) " +
            "values(#{empId}, #{beginDate}, #{endDate}, #{company}, #{job})")
    void addEmpExpr(Emp.EmpExpr expr);
}
