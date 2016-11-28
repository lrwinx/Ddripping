package com.tasly.core.config;

import com.tasly.core.component.analysis.ArgsAnalysisTemplate;
import com.tasly.core.component.analysis.SpELArgsAnalysis;
import com.tasly.core.component.reject.IdempotencyRejectExcutionStrategy;
import com.tasly.core.component.storage.StateStorageTemplate;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:核心配置 用户需要关注这个
 */
public class DdrippingConfig {

    ArgsAnalysisTemplate argsAnalysisTemplate = new SpELArgsAnalysis();
    StateStorageTemplate stateStorageTemplate = new StateStorageTemplate();
    IdempotencyRejectExcutionStrategy idempotencyRejectExcutionStrategy = new IdempotencyRejectExcutionStrategy.THROW_EXCEPTION(new RuntimeException("拒绝策略为异常"));

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
}
