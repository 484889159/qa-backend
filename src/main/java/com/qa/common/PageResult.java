package com.qa.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private Long total;
    private List<T> records;
    private Integer current;
    private Integer size;
    private Long pages;
}