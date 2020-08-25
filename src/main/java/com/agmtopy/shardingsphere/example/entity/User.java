package com.agmtopy.shardingsphere.example.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class User {

    private Integer id;

    private String vender_id;

    private String user_name;

    private String pass_word;
}
