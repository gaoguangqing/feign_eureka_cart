package com.spoon.feign;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spoon.comm.vo.SysResult;
import com.spoon.pojo.Cart;

@FeignClient("cart-provider")
public interface CartFeign {
	@RequestMapping("/cart/query/{userId}")
	public SysResult query(@PathVariable("userId") Long userId) ;
	@RequestMapping("/cart/delete/{userId}/{itemId}")
	public SysResult delete(@PathVariable("userId")Long userId,@PathVariable("itemId")Long itemId) ;
	@RequestMapping("/cart/update")
	public SysResult updateNum(@RequestBody Cart cart) ;
	@RequestMapping("/cart/save") //以post请求来访问
	public SysResult save(@RequestBody Cart cart);
}
