package org.example.springdemo3.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计结果 VO
 * 用于返回报表数据，如 [{name: "男", value: 10}, {name: "女", value: 8}]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportVO {
    private String name;
    private Integer value;
}
