package com.tasly.core.component.reject;

import com.tasly.core.component.storage.StateStorageOperations;
import com.tasly.core.exception.DataAccessRejectException;
import com.tasly.core.exception.RejectNotSupportedException;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
public abstract class IdempotencyRejectExcutionStrategy {

    public static IdempotencyRejectExcutionStrategy RESULT_SAME = new RESULT_SAME();
    public static IdempotencyRejectExcutionStrategy RESUTL_NUL = new RESUTL_NUL();
    public static IdempotencyRejectExcutionStrategy THROW_EXCEPTION = new THROW_EXCEPTION();

    /**
     * 返回相同结果
     */
    private static class RESULT_SAME extends IdempotencyRejectExcutionStrategy{
        StateStorageOperations stateStorageOperations;
        @Override
        public Object rejectActionIt(String key) {
            return stateStorageOperations.getKey(key);
        }

        @Override
        public void setRejectException(RuntimeException rejectException) {
            throw new RejectNotSupportedException("RESUTL_NUL 不支持设置异常策略!");
        }

        @Override
        public void setStateStorageOperations(StateStorageOperations stateStorageOperations) {
            this.stateStorageOperations = stateStorageOperations;
        }
    }

    /**
     * 返回null
     */
    private static class RESUTL_NUL extends IdempotencyRejectExcutionStrategy{
        @Override
        public Object rejectActionIt(String key) {
            return null;
        }

        @Override
        public void setRejectException(RuntimeException rejectException) {
            throw new RejectNotSupportedException("RESUTL_NUL 不支持设置异常策略!");
        }

        @Override
        public void setStateStorageOperations(StateStorageOperations stateStorageOperations) {
            throw new RejectNotSupportedException("RESUTL_NUL 不支持设置存储策略!");
        }
    }

    /**
     * 抛出异常  此异常设计为可以指定的业务异常
     */
    private static class THROW_EXCEPTION extends IdempotencyRejectExcutionStrategy{
        RuntimeException rejectException = new DataAccessRejectException();

        @Override
        public Object rejectActionIt(String key) {
            throw rejectException;
        }

        @Override
        public void setRejectException(RuntimeException rejectException) {
            this.rejectException = rejectException;
        }

        @Override
        public void setStateStorageOperations(StateStorageOperations stateStorageOperations) {
            throw new RejectNotSupportedException("THROW_EXCEPTION 不支持设置存储策略!");
        }
    }

    public abstract Object rejectActionIt(String key);

    /**
     * 设置异常策略
     * @param rejectException
     */
    public abstract void setRejectException(RuntimeException rejectException);

    /**
     * 设置存储策略
     * @param stateStorageOperations
     */
    public abstract void setStateStorageOperations(StateStorageOperations stateStorageOperations);

}
