package com.bookshop.goods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bookshop.goods.service.GoodsService;
import com.bookshop.goods.vo.GoodsVO;
import com.bookshop.goods.vo.ImageFileVO;
import com.bookshop.goods.dao.GoodsDAO;

@Service("goodsService")
//트랜잭션은 데이터베이스 작업을 논리적 단위로 묶어서 일관성을 유지하기 위한 개념
@Transactional(propagation=Propagation.REQUIRED)
public class GoodsServiceImpl implements GoodsService{
	@Autowired
	private GoodsDAO goodsDAO;

	@Override
	public Map<String, List<GoodsVO>> listGoods() throws Exception {
		Map<String,List<GoodsVO>> goodsMap=new HashMap<String,List<GoodsVO>>();
		List<GoodsVO> goodsList=goodsDAO.selectGoodsList("bestseller");
		goodsMap.put("bestseller",goodsList);
		goodsList=goodsDAO.selectGoodsList("newbook");
		goodsMap.put("newbook",goodsList);
		
		goodsList=goodsDAO.selectGoodsList("steadyseller");
		goodsMap.put("steadyseller",goodsList);
		return goodsMap;
	}
	
	@Override
	public Map goodsDetail(String _goods_id) throws Exception {
		Map goodsMap=new HashMap();
		//상품 정보외 이미지정보를 조회한 후 해쉬맵에 저장
		GoodsVO goodsVO = goodsDAO.selectGoodsDetail(_goods_id);
		goodsMap.put("goodsVO", goodsVO);
		List<ImageFileVO> imageList =goodsDAO.selectGoodsDetailImage(_goods_id);
		goodsMap.put("imageList", imageList);
		return goodsMap;
	}

}
