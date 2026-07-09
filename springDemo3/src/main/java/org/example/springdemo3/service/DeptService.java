package org.example.springdemo3.service;


import org.example.springdemo3.pojo.Dept;
import org.example.springdemo3.pojo.DeptPageListParams;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();

    void deleteByID(Integer deptId);

    Dept findById(Integer deptId);

    void addDept(Dept dept);

    void updateDept(Dept dept);

    /** 分页查询：统计总数 */
    Integer getDeptCount(DeptPageListParams params);

    /** 分页查询：获取当前页数据 */
    List<Dept> getDeptListByPage(DeptPageListParams params);
}
