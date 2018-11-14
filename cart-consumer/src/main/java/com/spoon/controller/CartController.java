package com.spoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spoon.comm.vo.SysResult;
import com.spoon.feign.CartFeign;
import com.spoon.pojo.Cart;
@RestController
public class CartController {
	@Autowired
	CartFeign cartFeign;
	@RequestMapping("/cart/query/{userId}")
	public SysResult query(@PathVariable("userId") Long userId){
		return cartFeign.query(userId);
	}
	@RequestMapping("/cart/delete/{userId}/{itemId}")
	public SysResult delete(@PathVariable("userId")Long userId,@PathVariable("itemId")Long itemId)
	{
		return cartFeign.delete(userId, itemId);
	}
	@RequestMapping("/cart/update")
	public SysResult updateNum( Cart cart)
	{
		System.out.println(cart.getNum()+" "+cart.getUserId()+" "+cart.getItemId());
		return cartFeign.updateNum(cart);
	}
	@RequestMapping("/cart/save") //以post请求来访问
	public SysResult save( Cart cart)
	{
		return cartFeign.save(cart);
	}
}
