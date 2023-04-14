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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.common.base.BaseController;
import com.bookshop.goods.controller.GoodsController;
import com.bookshop.goods.service.GoodsService;
import com.bookshop.goods.vo.GoodsVO;

import net.sf.json.JSONObject;

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
	
	//produces = "application/text; charset=utf8" : json�������� �ѱ����ڵ��� ����
	@RequestMapping(value="/keywordSearch.do",method = RequestMethod.GET,produces = "application/text; charset=utf8")
	//@ResponseBody:json�����͸� �������� ��� / @RequestParam("keyword") String keyword : �˻��� Ű���带 ������
	public @ResponseBody String  keywordSearch(@RequestParam("keyword") String keyword,
			                                  HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//System.out.println(keyword);
		if(keyword == null || keyword.equals(""))
		   return null ;
	//toUpperCase : Ű������ ���� �빮�ڷ� �����Ѵ�
		keyword = keyword.toUpperCase();
		//������ Ű���尡 ���Ե� ��ǰ ������ ��ȸ
	    List<String> keywordList =goodsService.keywordSearch(keyword);
	    
	 // ���� �ϼ��� JSONObject ����(��ü) JSONObject�� json�����͸� �ڹٿ��� ó���ϱ� ���� ���̺귯�� �� �ϳ�
		JSONObject jsonObject = new JSONObject();
		//��ȸ�� �����͸� json�� ����  keyword��� �̸�����  keywordList�� ����  jsonObject�� �߰��Ѵٴ� ��
		jsonObject.put("keyword", keywordList);
		 //json�� ���ڿ��� ��ȯ �� �������� ���		
	    String jsonInfo = jsonObject.toString();
	    System.out.println("jsonInfo�� ���� Ȯ�� : "+jsonInfo);
	    return jsonInfo ;
	}
	
	
	@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	//@RequestParam("searchWord")�� searchWord��� �Ű������� ������
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception{
		//viewName�Ӽ��� �����ͼ� viewName������ �Ҵ�
		String viewName=(String)request.getAttribute("viewName");
		//�˻�â���� ������ �ܾ ���Ե� ��ǰ������ ��ȸ searchGoods()�� ȣ���Ͽ� searchWord�� ���� ������ ����
		//�Է��� searchWord���� ���Ե� ��ǰ������ �˻��Ͽ� ���ň List<GoodsVO>���·� ��ȯ ����goodsList������ �Ҵ�
		List<GoodsVO> goodsList=goodsService.searchGoods(searchWord);
		//�������� �������ϱ⿡  viewName������ ���� ���̸����� �����Ͽ�   ModelAndView��ü ����
		ModelAndView mav = new ModelAndView(viewName);
		//goodsList������ ����� �˻��� ��ǰ����Ʈ�� goodsList��� �̸����� mav(ModelAndView)�� �߰�
		mav.addObject("goodsList", goodsList);
		//����� mav��ü ��ȯ
		return mav;
		
	}
	
	
	
	//�����غ� ������! �Ƹ� IT�߰�
	
	@RequestMapping(value="/goodsList.do" , method=RequestMethod.GET)
	//��ȸ�� ��ǰ��ȣ�� ���޹���
	public ModelAndView goodsList(@RequestParam("goods_sort") String goods_sort, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		HttpSession session=request.getSession();
		//��ǰ������ ��ȸ�� �� MAP���� ��ȯ
		Map goodsMap=goodsService.ItselectGoodsList(goods_sort);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsMap", goodsMap);
		//��ȸ�� ��ǰ�� ������ �����޴��� ǥ���ϱ����� ����
		GoodsVO goodsVO=(GoodsVO)goodsMap.get("goodsVO");
		
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
