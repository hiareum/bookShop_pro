package com.bookshop.goods.service;

import java.util.List;
import java.util.Map;

import com.bookshop.goods.vo.GoodsVO;

public interface GoodsService {

	public Map<String,List<GoodsVO>> listGoods() throws Exception;
	public Map goodsDetail(String _goods_id) throws Exception;
	public List<String> keywordSearch(String keyword) throws Exception;
	public List<GoodsVO> searchGoods(String searchWord) throws Exception;
	//아름 추가
	public Map<String, List<GoodsVO>> ItselectGoodsList(String goods_sort) throws Exception;
}
