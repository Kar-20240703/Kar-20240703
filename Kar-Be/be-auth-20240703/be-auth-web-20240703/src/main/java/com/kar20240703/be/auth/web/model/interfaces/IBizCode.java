package com.kar20240703.be.auth.web.model.interfaces;

/**
 * 业务消息枚举类的父类
 */
public interface IBizCode {

    int getCode(); // 建议从：300011开始

    String getMsg();

}
