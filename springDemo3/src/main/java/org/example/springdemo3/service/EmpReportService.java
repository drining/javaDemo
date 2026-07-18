package org.example.springdemo3.service;

import org.example.springdemo3.pojo.ReportVO;

import java.util.List;

public interface EmpReportService {

    /**
     * 获取性别分布统计
     * 返回 [{name: "男", value: 11}, {name: "女", value: 9}]
     */
    List<ReportVO> getGenderReport();

    /**
     * 获取职位分布统计
     * 返回 [{name: "主任", value: 8}, {name: "主管", value: 7}, {name: "员工", value: 5}]
     */
    List<ReportVO> getJobReport();
}
