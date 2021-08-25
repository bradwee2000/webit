package com.bwee.webit.model;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@Accessors(chain = true)
public class Person {
    private String id;
    private String name;
    private List<String> tags = Collections.emptyList();
    private LocalDateTime createTime;
    private Gender gender;

    @Getter
    public static enum Gender {
        Male("M"), Female("F");

        String code;

        Gender(String code) {
            this.code = code;
        }
    }
}
