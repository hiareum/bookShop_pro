package com.bookshop.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.common.base.BaseController;
import com.bookshop.goods.controller.GoodsController;
import com.bookshop.goods.service.GoodsService;
import com.bookshop.goods.vo.GoodsVO;

@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsControllerImpl extends BaseController   implements GoodsController {
	@Autowired
	private GoodsService goodsService;
	
	@Override
	@RequestMapping(value="/goodsDetail.do" ,method = RequestMethod.GET)
	//조회할 상품번호를 전달받음
	public ModelAndView goodsDetail(@RequestParam("goods_id") String goods_id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		HttpSession session=request.getSession();
		//상품정보를 조회한 후 MAP으로 반환
		Map goodsMap=goodsService.goodsDetail(goods_id);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsMap", goodsMap);
		//조회한 상품의 정보를 빠른메뉴에 표시하기위해 전달
		GoodsVO goodsVO=(GoodsVO)goodsMap.get("goodsVO");
		addGoodsInQuick(goods_id,goodsVO,session);
		return mav;
	}

	private void addGoodsInQuick(String goods_id, GoodsVO goodsVO, HttpSession session) {
		boolean already_existed=false;
		List<GoodsVO> quickGoodsList; //최근 본 상품 저장 ArrayList
		quickGoodsList=(ArrayList<GoodsVO>)session.getAttribute("quickGoodsList");
		
		//최근본 상품이 있는 경우
		//상품목록을 자겨와 이미 존재하는 상품인지 비교함 이미 존재하는 경우 already_existed=true로 설정
		if(quickGoodsList!=null){
			if(quickGoodsList.size() < 4){ //미리본 상품 리스트에 상품개수가 네개 이하인 경우
				for(int i=0; i<quickGoodsList.size();i++){
					GoodsVO _goodsBean=(GoodsVO)quickGoodsList.get(i);
					if(goods_id.equals(_goodsBean.getGoods_id())){
						already_existed=true;
						break;
					}
				}
				//already_existed==false이면 상품 정보를 목록에 저장
				if(already_existed==false){
					quickGoodsList.add(goodsVO);
				}
			}
			//최근 본 상품 목록이 없으면 생성하여 상품 정보를 저장
		}else{
			quickGoodsList =new ArrayList<GoodsVO>();
			quickGoodsList.add(goodsVO);
			
		}
		//최근 본 상품 목록을 세션에 저장
		session.setAttribute("quickGoodsList",quickGoodsList);
		//최근 본 상품 목록에 저장된 상품 개수를 세션에 저장
		session.setAttribute("quickGoodsListNum", quickGoodsList.size());
	}

}
