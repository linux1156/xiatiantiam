package com.example.demo.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.db.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    Logger log = LoggerFactory.getLogger(getClass());

    JSONObject jsonObject = new JSONObject();
    JSONObject data = new JSONObject();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public User allUser() {
        return userMapper.allUser();
    }

    @Override
    public JSONObject userById(int id){
        if(id<0){
            jsonObject.put("code", -100);
            jsonObject.put("data", data);
            jsonObject.put("msg", "userid错误");
            log.info(jsonObject.toString());
            return jsonObject;
        }
        User userById;
        userById =
        userById = userMapper.userById(id);
        jsonObject.put("code", 0);
        data.put("id", userById.getId());
        data.put("username", userById.getUserName());
        data.put("age", userById.getAge());
        data.put("nickname", userById.getNickName());
        data.put("female", userById.getFemale());
        data.put("adult", userById.getAdult());
        jsonObject.put("data", data);
        jsonObject.put("msg", "ok");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject insertUser(User user){
        int ttl = 24*60*60;
        int myId;
        try{
            //TODO(加数据库锁)
            userMapper.insertUser(user.getUserName(),user.getAge(), user.getNickName(), user.getFemale(), user.getAdult());
            myId = userMapper.selectLastId();
            user.setId(myId);
            redisUtil.set("user:info:"+myId, JSON.toJSON(user), ttl);
        }catch (Exception e){
            log.info(e.toString());
            jsonObject.put("code", -500);
            jsonObject.put("data", data);
            jsonObject.put("msg", "内部错误");
            return jsonObject;
        }
        jsonObject.put("code", 0);
        jsonObject.put("data", data);
        jsonObject.put("msg", "ok");
        return jsonObject;
    }
}
