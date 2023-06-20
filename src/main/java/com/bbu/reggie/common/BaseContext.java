package com.bbu.reggie.common;

/**
 * 一个线程内，封装工具类，获取和保存id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 在LoginCheckFilter，也就是拦截器设置值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
