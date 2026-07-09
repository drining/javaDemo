package org.example.springdemo3.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    private Integer page = 1;
    private Integer pageSize = 10;

    public Integer getCurrent() {
        return (page == null || page < 1) ? 1 : page;
    }

    public Integer getSize() {
        return (pageSize == null || pageSize < 1) ? 10 : pageSize;
    }

    public Integer getOffset() {
        return (getCurrent() - 1) * getSize();
    }
}
