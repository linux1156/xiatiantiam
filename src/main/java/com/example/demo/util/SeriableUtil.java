package com.example.demo.util;

import com.example.demo.entity.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SeriableUtil {

    /**
     * 序列化对象
     * @param obj
     * @return
     * 对象需要实现Serialiable接口
     */
    public static byte[] ObjTOSerialize(Object obj){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream byteOut = null;
        try{
            byteOut = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(byteOut);
            oos.writeObject(obj);
            byte[] bytes = byteOut.toByteArray();
            System.out.println(bytes.toString());
            return bytes;
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 序列化对象
     * @param bytes
     * @return
     * 对象需要实现Serialiable接口
     */
    public static Object unserialize(byte[] bytes){
        ByteArrayInputStream bais = null;
        try{
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            System.out.println(ois.readObject().toString());
            return ois.readObject();
        }catch (Exception e){

        }
        return null;
    }

    public static void main(String[] args){
        byte[] bytes;
        User user = new User();
        user.setId(11);
        user.setUserName("xiaoxia");
        user.setNickName("jiayi");
        user.setAge(18);
        user.setFemale(1);
        user.setAdult(1);
        bytes = ObjTOSerialize(user);
        unserialize(bytes);
    }
}
