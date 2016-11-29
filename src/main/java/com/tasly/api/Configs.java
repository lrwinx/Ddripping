package com.tasly.api;

import com.tasly.core.anno.impl.RestIdempotencyContract;
import com.tasly.core.component.reject.IdempotencyRejectExcutionStrategy;
import com.tasly.core.component.storage.MySQLStateStorageOperations;
import com.tasly.core.component.storage.StateStorageTemplate;
import com.tasly.core.config.DdrippingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
@Configuration
public class Configs {


    @Bean
    public RestIdempotencyContract restIdempotencyContract(@Autowired JdbcTemplate jdbcTemplate){
        DdrippingConfig ddrippingConfig = new DdrippingConfig();
        //存储策略
        DataSource dataSource = jdbcTemplate.getDataSource();
        StateStorageTemplate stateStorageTemplate = new StateStorageTemplate();
        stateStorageTemplate.setStateStorageOperations(new MySQLStateStorageOperations(dataSource));
        ddrippingConfig.setStateStorageTemplate(stateStorageTemplate);


        //拒绝策略
        ddrippingConfig.setIdempotencyRejectExcutionStrategy(new IdempotencyRejectExcutionStrategy.RESULT_SAME(ddrippingConfig.getStateStorageTemplate()));


        //配置
        RestIdempotencyContract restIdempotencyContract = new RestIdempotencyContract();
        restIdempotencyContract.setDdrippingConfig(ddrippingConfig);
        return restIdempotencyContract;
    }


}
