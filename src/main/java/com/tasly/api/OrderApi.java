package com.tasly.api;

import com.tasly.core.anno.RestIdempotency;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by dulei on 11/27/16.
 * Email:dreamfly@126.com
 * Site:http://lrwinx.github.io/
 * Github:https://github.com/lrwinx
 * Author:Lrwin
 * Description:
 */
@Controller
@RequestMapping("/api/order")
public class OrderApi implements Serializable{

    private static final long serialVersionUID = -453412739627691693L;

    @RequestMapping(value = "{uid}",method = RequestMethod.POST)
    @ResponseBody
    @RestIdempotency(value = "1#size(),2",time = 5000)
    public HashMap<String,?> generaterOrder(HttpServletRequest request,
                                        @RequestBody List<Product> productList,
                                        @PathVariable("uid") Integer uid){
        System.out.println("用户id: " + uid);
        System.out.println("购买产品: " + productList);
        HashMap result = new HashMap(){
            {
                put("orderId",UUID.randomUUID().toString());
            }
        };

        return result;
    }
}
