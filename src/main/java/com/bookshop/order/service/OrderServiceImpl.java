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
		//�ֹ� ��ǰ ����� ���̺� �߰��մϴ�
		orderDAO.insertNewOrder(myOrderList);
		//�ֹ� �� īƮ���� �ֹ� ��ǰ �����Ѵ�.
		orderDAO.removeGoodsFromCart(myOrderList);
		
	}

}
