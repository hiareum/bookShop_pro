package com.bookshop.main;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.goods.service.GoodsService;
import com.bookshop.goods.vo.GoodsVO;

//이름을 정해주지 않아도 되지만 이름으로 빈을 등록할 수 있기때문에
//의존성 주입 등 편하게 사용가능하므로 이름을 정해주면 좋음
@Controller("mainController")
//AOP에서 AspectJ와 같은 보다 강력한 AOP 기능을 사용가능
@EnableAspectJAutoProxy
public class MainController {
	
	//객체를 자동으로 주입
	@Autowired
	private GoodsService goodsService;
	
	///main/main.do 에대한 POST와 GET요청 처리
	@RequestMapping(value = "/main/main.do", method={RequestMethod.POST,RequestMethod.GET})
	//메서드 이름:main / HttpServletRequest request, HttpServletResponse response) throws Exception는 
	// Servlet API를 사용하여 클라이언트로부터 요청(request)을 받아 처리하고 응답(response)을 보내는 메소드에서 매개변수로 사용
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//HttpSession 객체를 선언 사용자의 상태를 유지하기위해 세션을 사용함
		HttpSession session;
		//ModelAndView 객체를 생성
		ModelAndView mav=new ModelAndView();
		//클라이언트의 요청(request) 객체에서 "viewName"이라는 속성을 가져와서 String 타입의 변수 viewName에 저장합니다.
		//아름:사용자가 이동하는 화면마다 뷰네임은 모두 다르다 요청에따라 달라지는 모든 뷰네임을 처리한다는 뜻!
		String viewName=(String)request.getAttribute("viewName");
		//ModelAndView 객체의 뷰(view) 이름을 설정합니다. viewName은 앞에서 가져온 클라이언트의 요청(request)에서 "viewName" 속성의 값입니다.
		//아름 : 화면에 요청받은 뷰네임을 셋틴한다는 뜻
		mav.setViewName(viewName);
		//HttpSession 객체를 생성합니다
		//사용자로부터 새로운 뷰를 요청받았기때문에 세션에 저장함 그러기위해 객체를 생성하는것
		session=request.getSession();
		//side_menu"라는 이름으로 "user" 값을 HttpSession 객체에 저장합니다.
		session.setAttribute("side_menu", "user");
		
		//GoodsVO를 리스트로 가지는 Map 객체를 goodsService에서 가져옵니다.
		//아름:요청을 받아 저장된 화면에 상품리스트를 보여줘야 한다 먼저는 list가져오기!
		Map<String,List<GoodsVO>> goodsMap=goodsService.listGoods();
		//ModelAndView 객체에 "goodsMap"이라는 이름으로 goodsMap 객체를 추가
		//아름:그리고 화면에 보여주기
		mav.addObject("goodsMap", goodsMap);
		//ModelAndView 객체를 반환합니다. 이 객체는 Controller에서 처리한 결과를 View에 전달
		return mav;
	}
	
}
