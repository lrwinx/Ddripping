package com.tasly.core.anno.impl;

import com.tasly.core.anno.RestIdempotency;
import com.tasly.core.component.analysis.ArgsAnalysisTemplate;
import com.tasly.core.component.reject.IdempotencyRejectExcutionStrategy;
import com.tasly.core.component.storage.StateStorageTemplate;
import com.tasly.core.exception.NeedRefreshException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class RestIdempotencyContract extends RestIdempotencyConfigAccessor implements InitializingBean{


    ArgsAnalysisTemplate argsAnalysisTemplate;
    StateStorageTemplate stateStorageTemplate;
    IdempotencyRejectExcutionStrategy idempotencyRejectExcutionStrategy;

    @Override
    public void afterPropertiesSet() throws Exception {
        argsAnalysisTemplate = getDdrippingConfig().getArgsAnalysisTemplate();
        stateStorageTemplate = getDdrippingConfig().getStateStorageTemplate();
        idempotencyRejectExcutionStrategy = getDdrippingConfig().getIdempotencyRejectExcutionStrategy();
    }

    @Around("within(@org.springframework.stereotype.Controller *) && @annotation(restIdempotency)")
    public Object around(ProceedingJoinPoint pjp,RestIdempotency restIdempotency){
        List<Object> argList = getArgList(pjp);
        String expressionData = restIdempotency.value();
        String key = argsAnalysisTemplate.generatorKey(expressionData,argList);

        Object result = null;
        while (true){
            if(stateStorageTemplate.setKeyIfAbsence(key,restIdempotency.time())){//设置成功
                try {
                    result = pjp.proceed();
                    stateStorageTemplate.resetValue(key, result);
                }catch (Throwable throwable) {
                    stateStorageTemplate.removeKey(key);
                    throwable.printStackTrace();
                }
                break;
            }else{//设置不成功  按照策略进行返回
                try {
                    return idempotencyRejectExcutionStrategy.rejectActionIt(key);
                }catch (NeedRefreshException e){//重新获取数据
                    continue;
                }
            }
        }
        return result;
    }






    private HttpServletRequest getHttpServletRequest(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                request = (HttpServletRequest) args[i];
                break;
            }
        }
        if (request == null) {
            throw new RuntimeException("方法中缺失HttpServletRequest参数");
        }
        return request;
    }

    private List<Object> getArgList(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            list.add(args[i]);
        }
        return list;
    }





}