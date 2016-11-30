package com.tasly.api;

import com.tasly.core.anno.impl.RestIdempotencyContract;
import com.tasly.core.component.reject.IdempotencyRejectExcutionStrategy;
import com.tasly.core.config.DdrippingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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
        DdrippingConfig config = new DdrippingConfig.Builder()
                .mysqlDataSource(jdbcTemplate.getDataSource())
                .reject(IdempotencyRejectExcutionStrategy.RESULT_SAME)
//                .businessException(new RuntimeException("业务异常"))
                .build();

        RestIdempotencyContract restIdempotencyContract = new RestIdempotencyContract();
        restIdempotencyContract.setDdrippingConfig(config);
        return restIdempotencyContract;
    }


}
