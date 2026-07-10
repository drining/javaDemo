package org.example.springdemo3.service.impl;

import org.example.springdemo3.mapper.EmpMapper;
import org.example.springdemo3.pojo.Emp;
import org.example.springdemo3.pojo.EmpPageListParams;
import org.example.springdemo3.pojo.EmpPageVO;
import org.example.springdemo3.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public List<EmpPageVO> findEmpsByPageList(EmpPageListParams params) {
        return empMapper.getEmpListByPage(params);
    }

    @Override
    public Integer getCount(EmpPageListParams params) {
        return empMapper.getCount(params);
    }

    @Override
    public void addEmp(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.addEmp(emp);

        List<Emp.EmpExpr> exprList = emp.getExprList();
        if (exprList != null && !exprList.isEmpty()) {
            exprList.forEach(expr -> expr.setEmpId(emp.getId()));
            empMapper.addEmpExprBatch(exprList);
        }
    }

    @Override
    public Emp findById(Integer id) {
        Emp emp = empMapper.findById(id);
        if (emp != null) {
            List<Emp.EmpExpr> exprList = empMapper.findExprByEmpId(id);
            emp.setExprList(exprList);
        }
        return emp;
    }

    @Override
    public void updateEmp(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        // 更新员工基本信息
        empMapper.updateEmp(emp);

        // 先删除旧的工作经历，再插入新的
        empMapper.deleteExprByEmpId(emp.getId());
        List<Emp.EmpExpr> exprList = emp.getExprList();
        if (exprList != null && !exprList.isEmpty()) {
            for (Emp.EmpExpr expr : exprList) {
                expr.setEmpId(emp.getId());
                empMapper.addEmpExpr(expr);
            }
        }
    }

    @Override
    public void deleteEmp(Integer id) {
        // emp_expr 有 ON DELETE CASCADE，但显式删除更安全
        empMapper.deleteExprByEmpId(id);
        empMapper.deleteEmp(id);
    }
}
