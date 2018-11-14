package com.spoon.service;

import java.util.List;

import com.spoon.comm.vo.SysResult;
import com.spoon.pojo.Cart;

public interface CartService {
	List<Cart> query(Long userId);

	void delete(Long userId, Long itemId);

	SysResult save(Cart cart);

	void updateNum(Cart cart);
}
