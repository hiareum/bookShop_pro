package com.bookshop.order.service;

import java.util.List;

import com.bookshop.order.vo.OrderVO;

public interface OrderService {
	public void addNewOrder(List<OrderVO> myOrderList) throws Exception;
}
