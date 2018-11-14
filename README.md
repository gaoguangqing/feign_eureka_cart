# feign_eureka_cart

SpringCloud-使用Feign，Eureka注册中心，重构购物车

启动完所有的项目之后，在浏览器输入，多刷新几次就可以看到数据了

http://localhost:9001/cartlist.html

使用下面的链接，更新购物车，需要指定用户id为7哦

http://localhost:9001/cartmodify.html

使用下面的链接，添加商品到购物车，可以尝试重复添加

http://localhost:9001/cartadd.html

使用下面的链接，删除购物车，也需要指定用户的id为7哦

http://localhost:9001/cartdelete.html

本项目使用MyBatisPlus，使用方法如下
```
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
```
注意生产者pojo与消费者pojo的不同,BasePojo是公共的
BasePojo
```
package com.spoon.pojo;

import java.io.Serializable;
import java.util.Date;

public class BasePojo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date created;
	private Date updated;
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
}
```
生产者pojo
```
package com.spoon.pojo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("tb_cart")
public class Cart extends BasePojo{
	private static final long serialVersionUID = 1L;
	@TableId(type=IdType.AUTO)
	private Long id;
	private Long userId;
	private Long itemId;
	private String itemTitle;
	private String itemImage;
	private Long itemPrice;
	public Long getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Long itemPrice) {
		this.itemPrice = itemPrice;
	}
	private Integer num;
	
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getItemImage() {
		return itemImage;
	}
	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}
	
}
```
消费者的pojo
```
package com.spoon.pojo;


public class Cart extends BasePojo{
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long userId;
	private Long itemId;
	private String itemTitle;
	private String itemImage;
	private Long itemPrice;
	public Long getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Long itemPrice) {
		this.itemPrice = itemPrice;
	}
	private Integer num;
	
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getItemImage() {
		return itemImage;
	}
	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}
	
}
```
同时注意消费者Feign接口与生产者的Controller对应,同时注意使用Feign接收对象参数，Feign接口与消费者Controller的对象参数前加@RequestBody
消费者Feign接口
```
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
```
生产者的Controller
```
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

```
