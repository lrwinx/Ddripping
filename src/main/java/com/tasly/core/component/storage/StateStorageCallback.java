package com.tasly.core.component.storage;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
public interface StateStorageCallback<T> {

    T doInStateStorage(StateStorageOperations stateStorageOperations);
}
