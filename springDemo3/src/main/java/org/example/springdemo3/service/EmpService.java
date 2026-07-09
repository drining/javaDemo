package org.example.springdemo3.service;

import java.util.List;

import org.example.springdemo3.pojo.Emp;
import org.example.springdemo3.pojo.EmpPageListParams;
import org.example.springdemo3.pojo.EmpPageVO;

public interface EmpService {
    List<EmpPageVO> findEmpsByPageList(EmpPageListParams getEmpPageListParams);

    void addEmp(Emp emp);
}
