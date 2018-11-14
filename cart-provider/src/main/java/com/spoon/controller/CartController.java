package com.spoon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spoon.comm.vo.SysResult;
import com.spoon.pojo.Cart;
import com.spoon.service.CartService;

@RestController
public class CartController {
	@Autowired
	private CartService cartService;
	
	@RequestMapping("/cart/query/{userId}")
	public SysResult query(@PathVariable("userId") Long userId) {
		try {
			System.out.println("CartController.query()"+userId);
			System.out.println("CartController.query()"+cartService);
			List<Cart> cartList = cartService.query(userId);
			return SysResult.oK(cartList);
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "查询购物车失败");
		}
	}
	@RequestMapping("/cart/delete/{userId}/{itemId}")
	public SysResult delete(@PathVariable("userId")Long userId,@PathVariable("itemId")Long itemId) {
		try {
			cartService.delete(userId, itemId);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "删除商品出错");
		}
		
	}
	@RequestMapping("/cart/update")
	public SysResult updateNum(@RequestBody Cart cart) {
		try {
			cartService.updateNum(cart);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.build(201, "修改商品数量出错");
		}
	}
	@RequestMapping("/cart/save") //以post请求来访问
	public SysResult save(@RequestBody Cart cart) {
		return cartService.save(cart);
	}
}
