package com.bookshop.goods.service;

import java.util.List;
import java.util.Map;

import com.bookshop.goods.vo.GoodsVO;

public interface GoodsService {

	public Map<String,List<GoodsVO>> listGoods() throws Exception;
	public Map goodsDetail(String _goods_id) throws Exception;
}
