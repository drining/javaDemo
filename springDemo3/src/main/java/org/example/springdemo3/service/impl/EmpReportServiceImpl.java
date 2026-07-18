package org.example.springdemo3.service.impl;

import org.example.springdemo3.mapper.EmpMapper;
import org.example.springdemo3.pojo.ReportVO;
import org.example.springdemo3.service.EmpReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpReportServiceImpl implements EmpReportService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public List<ReportVO> getGenderReport() {
        return empMapper.countByGender();
    }

    @Override
    public List<ReportVO> getJobReport() {
        return empMapper.countByJob();
    }
}
