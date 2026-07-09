package org.example.springdemo3.service.impl;

import org.example.springdemo3.mapper.DeptMapper;
import org.example.springdemo3.pojo.Dept;
import org.example.springdemo3.pojo.DeptPageListParams;
import org.example.springdemo3.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    public void deleteByID(Integer deptId) {
        deptMapper.delete(deptId);
    }

    public Dept findById(Integer id) {
        return deptMapper.findById(id);
    }

    public void addDept(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.addDept(dept);
    }

    public void updateDept(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.updateDept(dept);
    }

    public Integer getDeptCount(DeptPageListParams params) {
        return deptMapper.getDeptCount(params);
    }

    public List<Dept> getDeptListByPage(DeptPageListParams params) {
        return deptMapper.getDeptListByPage(params);
    }
}
