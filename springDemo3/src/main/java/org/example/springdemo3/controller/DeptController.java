package org.example.springdemo3.controller;

import java.util.List;

import org.example.springdemo3.pojo.Dept;
import org.example.springdemo3.pojo.DeptPageListParams;
import org.example.springdemo3.pojo.PageResult;
import org.example.springdemo3.pojo.Result;
import org.example.springdemo3.service.DeptService;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/depts")
@Tag(name = "部门管理", description = "部门相关接口")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping
    @Operation(summary = "查询全部部门")
    public Result list() {
        log.info("Request start: list departments");
        List<Dept> deptList = deptService.findAll();
        log.info("Request success: list departments, count={}", deptList.size());
        return Result.success(deptList);
    }

    @GetMapping("/pageList")
    @Operation(summary = "分页查询部门")
    public Result pageList(@Valid @ParameterObject DeptPageListParams params) {
        log.info("Request start: page department, page={}, pageSize={}", params.getCurrent(), params.getSize());
        Integer count = deptService.getDeptCount(params);
        List<Dept> deptList = deptService.getDeptListByPage(params);
        PageResult<Dept> pageResult = new PageResult<>(count, deptList);
        log.info("Request success: page department, total={}, rows={}", count, deptList.size());
        return Result.success(pageResult);
    }

    @DeleteMapping
    @Operation(summary = "删除部门")
    public Result delete(@Parameter(description = "部门ID", required = true)
                         @RequestParam("id") Integer deptId) {
        log.info("Request start: delete department, id={}", deptId);
        deptService.deleteByID(deptId);
        log.info("Request success: delete department, id={}", deptId);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询部门详情")
    public Result getDeptList(@Parameter(description = "部门ID", required = true)
                              @PathVariable Integer id) {
        log.info("Request start: get department detail, id={}", id);
        Dept dept = deptService.findById(id);
        log.info("Request success: get department detail, id={}, found={}", id, dept != null);
        return Result.success(dept);
    }

    @PostMapping("/add")
    @Operation(summary = "新增部门")
    public Result addDept(@RequestBody Dept dept) {
        log.info("Request start: add department, dept={}", dept);
        deptService.addDept(dept);
        log.info("Request success: add department, name={}", dept.getName());
        return Result.success();
    }

    @PostMapping("/update")
    @Operation(summary = "更新部门")
    public Result updateDept(@RequestBody Dept dept) {
        log.info("Request start: update department, dept={}", dept);
        deptService.updateDept(dept);
        log.info("Request success: update department, id={}", dept.getId());
        return Result.success();
    }
}
