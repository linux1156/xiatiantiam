package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@Slf4j
class UserController {

    private User user;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public JSONObject sayHello() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "ok");
        String s = "ok";
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public User allUser() {
        return  userService.allUser();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public JSONObject userByUserId(@PathVariable("id") int id){
        return userService.userById(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public  JSONObject insertUser(@RequestParam("username") String username, @RequestParam("age") int age,
                                  @RequestParam("nickname") String nickname, @RequestParam("female") int female,
                                  @RequestParam("adult") int adult){
        User user = new User();
        user.setId(0);
        user.setUserName(username);
        user.setAge(age);
        user.setNickName(nickname);
        user.setFemale(female);
        user.setAdult(adult);
        return userService.insertUser(user);
    }




}
