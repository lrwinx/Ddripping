package com.tasly.core.component.analysis;

import java.util.List;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
public interface ArgsAnalysisTemplate {

    /**
     * 生成唯一的key
     * @param expressionData
     * @param objContextList
     * @return
     */
    String generatorKey(String expressionData,List<Object> objContextList);
}
