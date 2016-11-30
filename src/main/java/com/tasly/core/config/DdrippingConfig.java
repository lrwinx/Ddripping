package com.tasly.core.config;

import com.tasly.core.component.analysis.ArgsAnalysisTemplate;
import com.tasly.core.component.analysis.SpELArgsAnalysis;
import com.tasly.core.component.reject.IdempotencyRejectExcutionStrategy;
import com.tasly.core.component.storage.MySQLStateStorageOperations;
import com.tasly.core.component.storage.StateStorageTemplate;
import com.tasly.core.exception.DataAccessRejectException;

import javax.sql.DataSource;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:核心配置 用户需要关注这个
 */
public class DdrippingConfig {


    private DdrippingConfig(){}


    ArgsAnalysisTemplate argsAnalysisTemplate = new SpELArgsAnalysis();
    StateStorageTemplate stateStorageTemplate = new StateStorageTemplate();
    IdempotencyRejectExcutionStrategy idempotencyRejectExcutionStrategy;

    public ArgsAnalysisTemplate getArgsAnalysisTemplate() {
        return argsAnalysisTemplate;
    }

    public void setArgsAnalysisTemplate(ArgsAnalysisTemplate argsAnalysisTemplate) {
        this.argsAnalysisTemplate = argsAnalysisTemplate;
    }

    public StateStorageTemplate getStateStorageTemplate() {
        return stateStorageTemplate;
    }

    public void setStateStorageTemplate(StateStorageTemplate stateStorageTemplate) {
        this.stateStorageTemplate = stateStorageTemplate;
    }

    public IdempotencyRejectExcutionStrategy getIdempotencyRejectExcutionStrategy() {
        return idempotencyRejectExcutionStrategy;
    }

    public void setIdempotencyRejectExcutionStrategy(IdempotencyRejectExcutionStrategy idempotencyRejectExcutionStrategy) {
        this.idempotencyRejectExcutionStrategy = idempotencyRejectExcutionStrategy;
    }


    public static class Builder{
        private ArgsAnalysisTemplate analysis;//分析器
        private StateStorageTemplate stateStorage;//存储策略
        private IdempotencyRejectExcutionStrategy reject;//拒绝策略


        private RuntimeException t;
        private DataSource mysqlDataSource;

        public Builder mysqlDataSource(DataSource mysqlDataSource){
            this.mysqlDataSource = mysqlDataSource;
            return this;
        }
        public Builder reject(IdempotencyRejectExcutionStrategy reject){
            this.reject = reject;
            return this;
        }
        public Builder businessException(RuntimeException t){
            this.t = t;
            return this;
        }

        public DdrippingConfig build(){
            DdrippingConfig ddrippingConfig = new DdrippingConfig();
            if(null == reject){//默认拒绝策略
                reject = IdempotencyRejectExcutionStrategy.DEFAULT_REJECT_EXCUTION_STRATEGY;
            }
            if(null == stateStorage){
                stateStorage = new StateStorageTemplate();
            }
            if(null == analysis){
                this.analysis = new SpELArgsAnalysis();
            }

            switch (reject.toString()){//策略默认值
                case "RESULT_SAME":
                    reject.setStateStorageOperations(stateStorage);
                    break;
                case "THROW_EXCEPTION":
                    if(null != t){
                        reject.setRejectException(t);
                    }else {
                        reject.setRejectException(new DataAccessRejectException());
                    }
                    break;
            }

            if(null != mysqlDataSource){
                stateStorage.setStateStorageOperations(new MySQLStateStorageOperations(mysqlDataSource));
            }

            ddrippingConfig.setStateStorageTemplate(stateStorage);
            ddrippingConfig.setArgsAnalysisTemplate(analysis);
            ddrippingConfig.setIdempotencyRejectExcutionStrategy(reject);

            return ddrippingConfig;
        }
    }
}
