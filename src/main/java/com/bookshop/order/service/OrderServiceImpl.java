package com.bookshop.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bookshop.order.service.OrderService;
import com.bookshop.order.vo.OrderVO;
import com.bookshop.order.dao.OrderDAO;

@Service("orderService")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderDAO orderDAO;

	@Override
	public void addNewOrder(List<OrderVO> myOrderList) throws Exception {
		//주문 상품 목록을 테이블에 추가합니다
		orderDAO.insertNewOrder(myOrderList);
		//주문 후 카트에서 주문 상품 제거한다.
		orderDAO.removeGoodsFromCart(myOrderList);
		
	}

}
