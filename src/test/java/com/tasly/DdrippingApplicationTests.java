package com.tasly;

import com.tasly.api.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DdrippingApplicationTests {

	@Test
	public void contextLoads() {

		List<Product> productList = new ArrayList<>();
		for(int i = 0 ; i < 10 ; i ++){
			Product product = new Product();
			product.setDesc("描述" + i);
			product.setName("名称" + i);
			product.setPrice(new BigDecimal(20.2 + i));
			product.setSku("sku" + i);
			productList.add(product);
		}

		String spELValue  = "[0]";

		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(spELValue);
		Object message =  exp.getValue(productList);
		System.out.println("=====: " + message);

	}

}
