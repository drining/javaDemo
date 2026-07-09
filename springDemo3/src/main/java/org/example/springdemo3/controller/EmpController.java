package org.example.springdemo3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.example.springdemo3.pojo.Emp;
import org.example.springdemo3.pojo.PageResult;
import org.example.springdemo3.pojo.EmpPageListParams;
import org.example.springdemo3.pojo.EmpPageVO;
import org.example.springdemo3.pojo.Result;
import org.example.springdemo3.service.impl.EmpServiceImpl;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Result<PageResult<EmpPageVO>> getEmpPageList(@Valid @ParameterObject EmpPageListParams params) {
        Integer count = empService.getCount(params);
        List<EmpPageVO> emplist = empService.findEmpsByPageList(params);
        return Result.success(new PageResult<>(count, emplist));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询员工详情（含工作经历）")
    public Result<Emp> getEmpDetail(@Parameter(description = "员工ID") @PathVariable Integer id) {
        log.info("查询员工详情: id={}", id);
        Emp emp = empService.findById(id);
        if (emp == null) {
            return Result.error("员工不存在");
        }
        return Result.success(emp);
    }

    @PostMapping("/add")
    @Operation(summary = "新增员工")
    public Result<Void> addEmp(@Valid @RequestBody Emp emp) {
        empService.addEmp(emp);
        log.info("新增员工成功: id={}", emp.getId());
        return Result.success();
    }

    @PostMapping("/update")
    @Operation(summary = "修改员工")
    public Result<Void> updateEmp(@RequestBody Emp emp) {
        log.info("修改员工: id={}", emp.getId());
        empService.updateEmp(emp);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "删除员工")
    public Result<Void> deleteEmp(@Parameter(description = "员工ID") @RequestParam("id") Integer id) {
        log.info("删除员工: id={}", id);
        empService.deleteEmp(id);
        return Result.success();
    }
}
