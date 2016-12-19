package com.yhhl.wsts.spring;

import org.springframework.stereotype.Service;


@Service
public class HelloAspect {

	@Wstsable
	public String sayHello(String name){
		System.out.println("方法执行，这里的参数是："+name);
		return "你好 \""+name+"\"。";
	}
}
