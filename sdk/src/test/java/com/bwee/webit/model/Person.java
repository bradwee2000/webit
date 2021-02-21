package com.bwee.webit.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Data
@Accessors(chain = true)
public class Person {
    private String id;
    private String name;
    private List<String> tags = Collections.emptyList();
}
