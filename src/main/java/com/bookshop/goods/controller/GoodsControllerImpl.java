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
	
	//produces = "application/text; charset=utf8" : json데이터의 한글인코딩을 지정
	@RequestMapping(value="/keywordSearch.do",method = RequestMethod.GET,produces = "application/text; charset=utf8")
	//@ResponseBody:json데이터를 브라우저로 출력 / @RequestParam("keyword") String keyword : 검색할 키워드를 가져옴
	public @ResponseBody String  keywordSearch(@RequestParam("keyword") String keyword,
			                                  HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//System.out.println(keyword);
		if(keyword == null || keyword.equals(""))
		   return null ;
	//toUpperCase : 키워드의 값을 대문자로 변경한다
		keyword = keyword.toUpperCase();
		//가져온 키워드가 포함된 상품 제목을 조회
	    List<String> keywordList =goodsService.keywordSearch(keyword);
	    
	 // 최종 완성될 JSONObject 선언(전체) JSONObject는 json데이터를 자바에서 처리하기 위한 라이브러리 중 하나
		JSONObject jsonObject = new JSONObject();
		//조회한 데이터를 json에 저장  keyword라는 이름으로  keywordList의 값을  jsonObject에 추가한다는 뜻
		jsonObject.put("keyword", keywordList);
		 //json을 문자열로 변환 후 브라우저로 출력		
	    String jsonInfo = jsonObject.toString();
	    System.out.println("jsonInfo의 내용 확인 : "+jsonInfo);
	    return jsonInfo ;
	}
	
	
	@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	//@RequestParam("searchWord")는 searchWord라는 매개변수를 가져옴
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception{
		//viewName속성을 가져와서 viewName변수에 할당
		String viewName=(String)request.getAttribute("viewName");
		//검색창에서 가져온 단어가 포함된 상품제목을 조회 searchGoods()를 호출하여 searchWord의 값을 변수로 전달
		//입력한 searchWord값이 포함된 상품제목을 검색하여 결과흫 List<GoodsVO>형태로 반환 값을goodsList변수에 할당
		List<GoodsVO> goodsList=goodsService.searchGoods(searchWord);
		//브라우저에 보여야하기에  viewName변수의 값을 뷰이름으로 지정하여   ModelAndView객체 생성
		ModelAndView mav = new ModelAndView(viewName);
		//goodsList변수에 저장된 검색된 상품리스트를 goodsList라는 이름으로 mav(ModelAndView)에 추가
		mav.addObject("goodsList", goodsList);
		//저장된 mav객체 반환
		return mav;
		
	}
	
	
	
	//시험해봄 지워라! 아름 IT추가
	
	@RequestMapping(value="/goodsList.do" , method=RequestMethod.GET)
	//조회할 상품번호를 전달받음
	public ModelAndView goodsList(@RequestParam("goods_sort") String goods_sort, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		HttpSession session=request.getSession();
		//상품정보를 조회한 후 MAP으로 반환
		Map goodsMap=goodsService.ItselectGoodsList(goods_sort);
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("goodsMap", goodsMap);
		//조회한 상품의 정보를 빠른메뉴에 표시하기위해 전달
		GoodsVO goodsVO=(GoodsVO)goodsMap.get("goodsVO");
		
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
