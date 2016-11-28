package com.tasly.core.component.reject;

import com.tasly.core.component.storage.StateStorageOperations;
import com.tasly.core.exception.DataAccessRejectException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
public abstract class IdempotencyRejectExcutionStrategy {


    /**
     * 返回相同结果
     */
    public static class RESULT_SAME extends IdempotencyRejectExcutionStrategy{
        StateStorageOperations stateStorageOperations;
        public RESULT_SAME(StateStorageOperations stateStorageOperations){
            this.stateStorageOperations = stateStorageOperations;
        }
        @Override
        public Object rejectActionIt(String key) {
            return stateStorageOperations.getKey(key);
        }
    }

    /**
     * 返回null
     */
    public static class RESUTL_NUL extends IdempotencyRejectExcutionStrategy{
        @Override
        public Object rejectActionIt(String key) {
            return null;
        }
    }

    /**
     * 抛出异常  此异常设计为可以指定的业务异常
     */
    public static class THROW_EXCEPTION extends IdempotencyRejectExcutionStrategy{
        RuntimeException t = new DataAccessRejectException();
        public THROW_EXCEPTION(RuntimeException t){
            this.t = checkNotNull(t);
        }
        @Override
        public Object rejectActionIt(String key) {
            throw t;
        }
    }

    public abstract Object rejectActionIt(String key);

}
