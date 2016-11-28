package com.tasly.core.component.storage;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:转态保存的连接器  用于配置使用那种存储方式  默认是内存式存储
 */
public class StateStorageAccessor {

    private final static MemoryStateStorageOperations DEFAULT_STORAGEOPERATIONS = new MemoryStateStorageOperations();

    private StateStorageOperations stateStorageOperations = DEFAULT_STORAGEOPERATIONS;

    public StateStorageOperations getStateStorageOperations() {
        return stateStorageOperations;
    }

    public void setStateStorageOperations(StateStorageOperations stateStorageOperations) {
        this.stateStorageOperations = stateStorageOperations;
    }
}
