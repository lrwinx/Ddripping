package com.tasly.core.anno.impl;

import com.tasly.core.config.DdrippingConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:转态保存的连接器  用于配置使用那种存储方式  默认是内存式存储
 */
public class RestIdempotencyConfigAccessor  implements InitializingBean {
    private DdrippingConfig ddrippingConfig;

    public DdrippingConfig getDdrippingConfig() {
        return ddrippingConfig;
    }

    public void setDdrippingConfig(DdrippingConfig ddrippingConfig) {
        this.ddrippingConfig = ddrippingConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.getDdrippingConfig(), "DdrippingConfig is required");
    }
}
