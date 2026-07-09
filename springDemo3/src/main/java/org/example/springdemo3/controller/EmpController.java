package org.example.springdemo3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.example.springdemo3.pojo.Emp;
import org.example.springdemo3.pojo.PageResult;
import org.example.springdemo3.pojo.EmpPageListParams;
import org.example.springdemo3.pojo.EmpPageVO;
import org.example.springdemo3.service.impl.EmpServiceImpl;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/emps")
@Tag(name = "员工管理", description = "员工相关接口")
public class EmpController {

    @Autowired
    private EmpServiceImpl empService;

    @GetMapping("/pageList")
    @Operation(summary = "分页查询员工")
    public PageResult<EmpPageVO> getEmpPageList(@Valid @ParameterObject EmpPageListParams getEmpPageListParams) {

        Integer count = empService.getCount(getEmpPageListParams);

        List<EmpPageVO> emplist = empService.findEmpsByPageList(getEmpPageListParams);
        return new PageResult<EmpPageVO>(count,emplist);
    }

    @PostMapping("/add")
    @Operation(summary = "新增员工")
    public void addEmp(@Valid @RequestBody Emp emp) {
        empService.addEmp(emp);
        // log.info("Request success: add employee, id={}", emp.getId());
    }
}
