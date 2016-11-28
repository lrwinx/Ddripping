package com.tasly.core.component.storage;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:幂等操作的转态存储
 */
public interface StateStorageOperations {

    boolean setKeyIfAbsence(String key,long timeout);

    void resetValue(String key,Object value);

    void removeKey(String key);

    Object getKey(String key);

}
