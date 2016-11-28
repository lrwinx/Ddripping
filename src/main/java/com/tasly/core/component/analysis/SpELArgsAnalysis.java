package com.tasly.core.component.analysis;

import com.tasly.core.utils.DataResolverUtils;
import org.springframework.util.StringUtils;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
public class SpELArgsAnalysis extends ArgsAnalysisAbstract  implements ArgsAnalysisTemplate{
    @Override
    public Object analysisAndResolveData(String expression, Object context) {
        if(!StringUtils.hasText(expression)){
            expression = "toString()";
        }
        return DataResolverUtils.withSpEL().resolveData(expression,context);
    }
}
