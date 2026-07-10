package org.example.springdemo3.service;

import org.example.springdemo3.pojo.Emp;
import org.example.springdemo3.pojo.EmpPageListParams;
import org.example.springdemo3.pojo.EmpPageVO;

import java.util.List;

public interface EmpService {
    /** 分页查询 */
    List<EmpPageVO> findEmpsByPageList(EmpPageListParams params);

    /** 总数 */
    Integer getCount(EmpPageListParams params);

    /** 新增 */
    void addEmp(Emp emp);

    /** 查询员工详情（含工作经历） */
    Emp findById(Integer id);

    /** 修改 */
    void updateEmp(Emp emp);

    /** 删除 */
    void deleteEmp(Integer id);
}
