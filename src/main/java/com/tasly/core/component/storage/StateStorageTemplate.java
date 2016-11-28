package com.tasly.core.component.storage;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
public class StateStorageTemplate extends StateStorageAccessor implements StateStorageOperations{

    public StateStorageTemplate() {
    }

    public <T> T execute(StateStorageCallback<T> action) {
        return action.doInStateStorage(super.getStateStorageOperations());
    }

    @Override
    public boolean setKeyIfAbsence(String key, long timeout) {
        return execute(new StateStorageCallback<Boolean>() {
            @Override
            public Boolean doInStateStorage(StateStorageOperations stateStorageOperations) {
                return stateStorageOperations.setKeyIfAbsence(key,timeout);
            }
        });
    }

    @Override
    public void resetValue(String key, Object value) {
        execute(new StateStorageCallback<Boolean>() {
            @Override
            public Boolean doInStateStorage(StateStorageOperations stateStorageOperations) {
                stateStorageOperations.resetValue(key,value);
                return null;
            }
        });
    }

    @Override
    public void removeKey(String key) {
        execute(new StateStorageCallback<Boolean>() {
            @Override
            public Boolean doInStateStorage(StateStorageOperations stateStorageOperations) {
                stateStorageOperations.removeKey(key);
                return null;
            }
        });
    }

    @Override
    public Object getKey(String key) {
        return execute(new StateStorageCallback<Object>() {
            @Override
            public Object doInStateStorage(StateStorageOperations stateStorageOperations) {
                return stateStorageOperations.getKey(key);
            }
        });
    }
}
