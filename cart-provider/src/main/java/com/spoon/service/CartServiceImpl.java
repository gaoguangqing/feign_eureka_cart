package com.spoon.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.spoon.comm.vo.SysResult;
import com.spoon.mapper.CartMapper;
import com.spoon.pojo.Cart;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private CartMapper cartMapper;
	
	//我的购物车，查询:where userId=?
	@Override
	public List<Cart> query(Long userId) {
		EntityWrapper<Cart> wrapper=new EntityWrapper<Cart>();
		wrapper.where("user_id={0}", userId);
		return cartMapper.selectList(wrapper);
	}
	//删除用户的某商品
	@Override
	public void delete(Long userId,Long itemId) {
		EntityWrapper<Cart> wrapper=new EntityWrapper<Cart>();
		//where user_id=? and item_id=? 方式QBC面向对象查询方式
		wrapper.where("user_id={0}", userId);
		wrapper.and("item_id={0}",itemId);
		cartMapper.delete(wrapper);
	}
	//新增商品到购物车,按对象方式接收参数
	@Override
	public SysResult save(Cart cart){
		//判断此用户商品是否存在
		Cart param=new Cart();
		param.setUserId(cart.getUserId());
		param.setItemId(cart.getItemId());
		Cart oldCart=cartMapper.selectOne(param); //pojo,属性怎么出现在where条件，属性为null就出现
		if(oldCart==null){
			//如果不存在，新增操作
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
			return SysResult.oK();
		}else{
			//如果存在，修改数量=旧数量+新数量
			oldCart.setNum(oldCart.getNum()+cart.getNum());
			cartMapper.updateById(oldCart);
			return SysResult.build(202, "此用户的此商品已经存在购物车中");
		}
	}
	//更新商品数量+-，页面的值直接替换数据库
	@Override
	public void updateNum(Cart cart) {
		System.out.println("CartServiceImpl.updateNum()"+cart.getUserId()+" "+cart.getNum());
		EntityWrapper<Cart> wrapper=new EntityWrapper<Cart>();
		//where user_id=? and item_id?
		wrapper.where("user_id={0}", cart.getUserId());
		wrapper.and("item_id={0}",cart.getItemId());
		cart.setUpdated(new Date());
		cartMapper.update(cart, wrapper);
	}
}
