package com.tasly.core.component.storage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:内存式存储  也是默认的方式
 */
public class MemoryStateStorageOperations implements StateStorageOperations{

    ConcurrentHashMap<String,Object> map = new ConcurrentHashMap<String,Object>();

    @Override
    public boolean setKeyIfAbsence(String key, long timeout) {
        Object obj = map.get(key);
        if(null != obj){
            return false;
        }
        if(timeout != STATE_STORAGE_TIMEOUT.NEVER_TIME_OUT){
            map.put(key,"仍在操作");
            Timer timer= new Timer();
            TimerTask task  = new TimerTask(){    //创建一个新的计时器任务。
                @Override
                public void run() {
                    map.remove(key);
                }
            };
            timer.schedule(task, timeout);
        }
        return true;
    }

    @Override
    public void resetValue(String key, Object value) {
        map.put(key,value);
    }

    @Override
    public void removeKey(String key) {
        map.remove(key);
    }

    @Override
    public Object getKey(String key) {
        return map.get(key);
    }
}
