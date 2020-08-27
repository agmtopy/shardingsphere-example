package com.agmtopy.shardingsphere.example.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "vender_id")
    private String vender_id;

    @Column(name = "user_name")
    private String user_name;

    @Column(name = "pass_word")
    private String pass_word;
}
