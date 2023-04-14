package com.bookshop.goods.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.bookshop.goods.vo.GoodsVO;
import com.bookshop.goods.vo.ImageFileVO;

public interface GoodsDAO {
	public List<GoodsVO> selectGoodsList(String goodsStatus ) throws DataAccessException;
	public GoodsVO selectGoodsDetail(String goods_id) throws DataAccessException;
	public List<ImageFileVO> selectGoodsDetailImage(String goods_id) throws DataAccessException;
	public List<String> selectKeywordSearch(String keyword) throws DataAccessException;
	public ArrayList selectGoodsBySearchWord(String searchWord) throws DataAccessException;
	//아름 IT추가
	public List<GoodsVO> ItselectGoodsList(String goods_sort) throws DataAccessException;
}
