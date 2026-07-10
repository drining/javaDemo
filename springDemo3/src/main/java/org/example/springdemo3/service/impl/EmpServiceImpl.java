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

    @Transactional(readOnly = true)
    public List<EmpPageVO> findEmpsByPageList(EmpPageListParams getEmpPageListParams) {

        List<EmpPageVO> empList = empMapper.getEmpListByPage(getEmpPageListParams);
        return empList;
    }

    @Transactional(readOnly = true)
    public Integer getCount(EmpPageListParams getEmpPageListParams) {
        return empMapper.getCount(getEmpPageListParams);
    }

    @Transactional
    public void addEmp(Emp emp) {
        // 设置创建/更新时间
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        // 插入员工主表（自增 ID 回写到 emp.id）
        empMapper.addEmp(emp);
        // 批量插入工作经历
        List<Emp.EmpExpr> exprList = emp.getExprList();
        if (exprList != null && !exprList.isEmpty()) {
            exprList.forEach(expr -> expr.setEmpId(emp.getId()));
            empMapper.addEmpExprBatch(exprList);
        }
    }
}
