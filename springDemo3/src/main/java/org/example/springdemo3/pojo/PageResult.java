package org.example.springdemo3.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Integer total;
    // private Integer current;
    // private Integer size;
    private List<T> rows;
}
