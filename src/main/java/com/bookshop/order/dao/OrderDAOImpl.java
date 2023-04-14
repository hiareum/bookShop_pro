package com.bookshop.order.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop.order.dao.OrderDAO;
import com.bookshop.order.vo.OrderVO;


@Repository("orderDAO")
public class OrderDAOImpl implements OrderDAO {
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public void insertNewOrder(List<OrderVO> myOrderList) throws DataAccessException {
		//각 orderVO에 설정할 주문번호를 가져옵니다
		int order_id=selectOrderID();
		//주문목록에서 차례대로 orderVO를 가져와 주문번호를 설정합니다
		for(int i=0; i<myOrderList.size();i++){
			OrderVO orderVO =(OrderVO)myOrderList.get(i);
			orderVO.setOrder_id(order_id);
			sqlSession.insert("mapper.order.insertNewOrder",orderVO);
			}
		
	}

	//장바구니에서 주문한 경우 해당 상품을 장바구니에서 삭제
	@Override
	public void removeGoodsFromCart(List<OrderVO> myOrderList) throws DataAccessException {
		for(int i=0; i<myOrderList.size();i++){
			OrderVO orderVO =(OrderVO)myOrderList.get(i);
			sqlSession.delete("mapper.order.deleteGoodsFromCart",orderVO);		
		}
	}
	
	//테이블에서 저장할 주문번호를 가져옴
	private int selectOrderID() throws DataAccessException{
		return sqlSession.selectOne("mapper.order.selectOrderID");
		
	}

}
