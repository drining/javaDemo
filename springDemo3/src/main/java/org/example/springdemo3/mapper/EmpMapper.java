package org.example.springdemo3.mapper;

import org.apache.ibatis.annotations.*;
import org.example.springdemo3.pojo.Emp;
import org.example.springdemo3.pojo.EmpPageListParams;
import org.example.springdemo3.pojo.EmpPageVO;

import java.util.List;

@Mapper
public interface EmpMapper {

    // ========== 分页查询 ==========

    Integer getCount(EmpPageListParams params);

    List<EmpPageVO> getEmpListByPage(EmpPageListParams params);

    // ========== 新增 ==========

    @Insert("insert into emp(username, password, name, gender, phone, job, salary, image, entry_date, create_time, update_time, dept_id) " +
            "values(#{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{createTime}, #{updateTime}, #{deptId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addEmp(Emp emp);

    @Insert("insert into emp_expr(emp_id, begin_date, end_date, company, job) " +
            "values(#{empId}, #{beginDate}, #{endDate}, #{company}, #{job})")
    void addEmpExpr(Emp.EmpExpr expr);

    // ========== 详情 ==========

    @Select("select * from emp where id = #{id}")
    @Results(id = "empMap", value = {
            @Result(column = "entry_date", property = "entryDate"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "dept_id", property = "deptId")
    })
    Emp findById(Integer id);

    @Select("select * from emp_expr where emp_id = #{empId}")
    @Results({
            @Result(column = "begin_date", property = "beginDate"),
            @Result(column = "end_date", property = "endDate"),
            @Result(column = "emp_id", property = "empId")
    })
    List<Emp.EmpExpr> findExprByEmpId(Integer empId);

    // ========== 修改 ==========

    @Update("update emp set username=#{username}, name=#{name}, gender=#{gender}, phone=#{phone}, " +
            "job=#{job}, salary=#{salary}, image=#{image}, entry_date=#{entryDate}, " +
            "dept_id=#{deptId}, update_time=#{updateTime} where id=#{id}")
    void updateEmp(Emp emp);

    @Delete("delete from emp_expr where emp_id = #{empId}")
    void deleteExprByEmpId(Integer empId);

    // ========== 删除 ==========

    @Delete("delete from emp where id = #{id}")
    void deleteEmp(Integer id);
}
