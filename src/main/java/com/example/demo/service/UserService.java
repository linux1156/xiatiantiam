package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.User;

public interface UserService {
    User allUser();

    JSONObject userById(int id);

    JSONObject insertUser(User user);
}
