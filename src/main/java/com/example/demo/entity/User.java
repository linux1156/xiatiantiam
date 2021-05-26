package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Table(catalog = "test_user")
@Data
public class User implements Serializable {
    @Column(name="username")
    private String userName;

    @Column(name="id")
    private int id;

    @Column(name="age")
    private int age;

    @Column(name="nickname")
    private String nickName;

    @Column(name="female")
    private int female;

    @Column(name="adult")
    private int adult;
}
