package org.example.springdemo3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.springdemo3.pojo.ReportVO;
import org.example.springdemo3.pojo.Result;
import org.example.springdemo3.service.EmpReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/report")
@Tag(name = "数据统计", description = "员工数据统计接口")
public class ReportController {

    @Autowired
    private EmpReportService empReportService;

    @GetMapping("/gender")
    @Operation(summary = "性别分布统计")
    public Result<List<ReportVO>> genderReport() {
        List<ReportVO> data = empReportService.getGenderReport();
        log.info("性别分布统计: {}", data);
        return Result.success(data);
    }

    @GetMapping("/job")
    @Operation(summary = "职位分布统计")
    public Result<List<ReportVO>> jobReport() {
        List<ReportVO> data = empReportService.getJobReport();
        log.info("职位分布统计: {}", data);
        return Result.success(data);
    }
}
