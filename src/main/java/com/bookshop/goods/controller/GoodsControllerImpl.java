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
	//��ȸ�� ��ǰ��ȣ�� ���޹���
	public ModelAndView goodsDetail(@RequestParam("goods_id") String goods_id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		HttpSession session=request.getSession();
		//��ǰ������ ��ȸ�� �� MAP���� ��ȯ
		Map goodsMap=goodsService.goodsDetail(goods_id);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsMap", goodsMap);
		//��ȸ�� ��ǰ�� ������ �����޴��� ǥ���ϱ����� ����
		GoodsVO goodsVO=(GoodsVO)goodsMap.get("goodsVO");
		addGoodsInQuick(goods_id,goodsVO,session);
		return mav;
	}

	private void addGoodsInQuick(String goods_id, GoodsVO goodsVO, HttpSession session) {
		boolean already_existed=false;
		List<GoodsVO> quickGoodsList; //�ֱ� �� ��ǰ ���� ArrayList
		quickGoodsList=(ArrayList<GoodsVO>)session.getAttribute("quickGoodsList");
		
		//�ֱٺ� ��ǰ�� �ִ� ���
		//��ǰ����� �ڰܿ� �̹� �����ϴ� ��ǰ���� ���� �̹� �����ϴ� ��� already_existed=true�� ����
		if(quickGoodsList!=null){
			if(quickGoodsList.size() < 4){ //�̸��� ��ǰ ����Ʈ�� ��ǰ������ �װ� ������ ���
				for(int i=0; i<quickGoodsList.size();i++){
					GoodsVO _goodsBean=(GoodsVO)quickGoodsList.get(i);
					if(goods_id.equals(_goodsBean.getGoods_id())){
						already_existed=true;
						break;
					}
				}
				//already_existed==false�̸� ��ǰ ������ ��Ͽ� ����
				if(already_existed==false){
					quickGoodsList.add(goodsVO);
				}
			}
			//�ֱ� �� ��ǰ ����� ������ �����Ͽ� ��ǰ ������ ����
		}else{
			quickGoodsList =new ArrayList<GoodsVO>();
			quickGoodsList.add(goodsVO);
			
		}
		//�ֱ� �� ��ǰ ����� ���ǿ� ����
		session.setAttribute("quickGoodsList",quickGoodsList);
		//�ֱ� �� ��ǰ ��Ͽ� ����� ��ǰ ������ ���ǿ� ����
		session.setAttribute("quickGoodsListNum", quickGoodsList.size());
	}

}
