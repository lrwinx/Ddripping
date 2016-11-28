Ddripping 是一个简单的, 高效的, 提供多策略的java语言相关的解决方案.

它大概包括以下功能:

* **幂等支持SPEL表达式**: 幂等解析器使用SPEL表达式作为解析器
* **幂等存储策略**: 存储策略支持:内存式存储,mysql存储
* **幂等拒绝策略**: 拒绝策略:多次返回相同值,返回null,抛出异常

## 快速开始


在HTTP中添加注解:

```
@RestIdempotency(value = "1#size(),2",time = 5000)
```

例子如下:

```
@Controller
@RequestMapping("/api/order")
public class OrderApi {

    @RequestMapping(value = "{uid}",method = RequestMethod.POST)
    @ResponseBody
    @RestIdempotency(value = "1#size(),2",time = 5000)
    public Map<String,?> generaterOrder(HttpServletRequest request,
                                        @RequestBody List<Product> productList,
                                        @PathVariable("uid") Integer uid){
        System.out.println("用户id: " + uid);
        System.out.println("购买产品: " + productList);
        Map result = new HashMap(){
            {
                put("orderId",UUID.randomUUID().toString());
            }
        };

        return result;
    }
}
```

## 项目结构


0. com.tasly.core: 此包下的所有类为核心类

1. com.tasly.core: 此包下的所有类为测试类


