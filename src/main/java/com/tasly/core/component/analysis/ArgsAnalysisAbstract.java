package com.tasly.core.component.analysis;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;

import java.util.Iterator;
import java.util.List;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
public abstract class ArgsAnalysisAbstract implements ArgsAnalysisTemplate{

    public abstract Object analysisAndResolveData(String expression,Object context);

    private final String DEFAULT_GROUP_SEPARATOR = ",";//默认组分隔符 如对于list集合:1#size(),2#size()

    private final String DEFAULT_INNER_SEPARATOR = "#";//默认内部分隔符 如对于list集合:1#size()

    private String groupSeparator = DEFAULT_GROUP_SEPARATOR;
    private String innerSeparator = DEFAULT_INNER_SEPARATOR;

    public void setGroupSeparator(String groupSeparator){
        this.groupSeparator = groupSeparator;
    }

    public void setInnerSeparator(String innerSeparator){
        this.innerSeparator = innerSeparator;
    }

    private final String ARG_INDEX = "index";
    private final String ARG_EXPRESSION = "expression";

    @Override
    public String generatorKey(String expressionData,List<Object> objContextList){
        List<String> parseValueList = analysisArgs(expressionData,objContextList);
        String parseValueString = Joiner.on(",").join(parseValueList);
        //TODO 一种加密算法生成一个唯一字符串
        String uniqueKey = Hashing.md5().newHasher().putString(parseValueString, Charsets.UTF_8).hash().toString();
        return uniqueKey;
    }

    private List<String> analysisArgs(String expressionData,List<Object> objContextList){
        Iterable<String> dataIterable = Splitter.on(groupSeparator).split(expressionData);
        Iterator<String> dataIterator = dataIterable.iterator();

        List<String> parseValueList = Lists.newArrayList();

        while (dataIterator.hasNext()){
            String singleData = dataIterator.next();
            ImmutableMap<String, ?> argIndexData = splitArg(singleData);
            String expression = (String) MoreObjects.firstNonNull(argIndexData.get(ARG_EXPRESSION),"");
            Object context = objContextList.get((Integer) argIndexData.get(ARG_INDEX));
            String parseValue = analysisAndResolveData(expression,context).toString();
            parseValueList.add(parseValue);
        }

        return parseValueList;
    }

    private ImmutableMap<String, ?> splitArg(String arg){
        Iterable<String> argIterable = Splitter.on(innerSeparator).split(arg);
        Iterator<String> argIterator = argIterable.iterator();

        Integer index = null;
        String expression = null;

        if (argIterator.hasNext()){
            index = Integer.valueOf(argIterator.next());
        }

        if (argIterator.hasNext()){
            expression = argIterator.next();
        }

        return ImmutableMap.of(ARG_INDEX,index,ARG_EXPRESSION,MoreObjects.firstNonNull(expression,""));

    }
}
