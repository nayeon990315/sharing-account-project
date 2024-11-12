package com.example.backend.user.vo;

import lombok.Data;

@Data
public class UserVO {
    private String userId;
    private String pwd;
    private String name;
    private String gender;
    private Integer age;

    public UserVO(Integer age, String gender, String name, String pwd, String userId) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.pwd = pwd;
        this.userId = userId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
