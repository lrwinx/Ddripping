package com.tasly.core.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:数据表达式解析器
 *
 * 目前只支持SPEL表达式,使用方法:
 *      DataResolverUtils.withSpEL().resolveData(#param1,#param2);
 */
public abstract class DataResolverUtils {

    public static DataResolverUtils withSpEL(){
        return SpELDataResolver;
    }

    static final DataResolverUtils SpELDataResolver = new DataResolverUtils() {
        @Override
        public Object resolveData(String expression,Object context) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(expression);
            Object retObj =  exp.getValue(context);
            return retObj;
        }
    } ;

    public abstract Object resolveData(String expression,Object context);

}
