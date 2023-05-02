package com.bookshop.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.common.base.BaseController;
import com.bookshop.order.controller.OrderController;
import com.bookshop.order.vo.OrderVO;
import com.bookshop.goods.vo.GoodsVO;
import com.bookshop.order.service.ApiService01;
import com.bookshop.order.service.OrderService;
import com.bookshop.member.vo.MemberVO;

@Controller("orderController")
@RequestMapping(value="/order")
public class OrderControllerImpl extends BaseController implements OrderController {
	@Autowired
	private OrderVO orderVO;
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ApiService01 apiService01;
	
	@Override
	@RequestMapping(value="/orderEachGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderEachGoods(@ModelAttribute("orderVO") OrderVO _orderVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		HttpSession session=request.getSession();
		session=request.getSession();
		
		Boolean isLogOn=(Boolean)session.getAttribute("isLogOn");
		String action=(String)session.getAttribute("action");
		//로그인 여부 체크
		//이전에 로그인 상태인 경우는 주문과정 진행
		//로그아웃 상태인 경우 로그인 화면으로 이동
		if(isLogOn==null || isLogOn==false){
			session.setAttribute("orderInfo", _orderVO);
			session.setAttribute("action", "/order/orderEachGoods.do");
			return new ModelAndView("redirect:/member/loginForm.do");
		}else{
			 if(action!=null && action.equals("/order/orderEachGoods.do")){
				orderVO=(OrderVO)session.getAttribute("orderInfo");
				session.removeAttribute("action");
			 }else {
				 orderVO=_orderVO;
			 }
		 }
		
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		//주문정보를 저장할 주문 ArrayList를 생성
		List myOrderList=new ArrayList<OrderVO>();
		//브라우저에서 전달할 주문정보를 ArrayList에 저장
		myOrderList.add(orderVO);

		MemberVO memberInfo=(MemberVO)session.getAttribute("memberInfo");
		//주문정보와 주문자 정보를 세션에 바인딩한 후 주문참으로 전달
		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberInfo);
		return mav;
	}

	//@RequestParam Map<String, String> receiverMap : 주문창에서 입력한 상품 수령자 정보와 배송지 정보를 map에 바로 저장
	@Override
	@RequestMapping(value="/payToOrderGoods.do" ,method = RequestMethod.POST)
	public ModelAndView payToOrderGoods(@RequestParam Map<String, String> receiverMap,
			                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		//결제하기 수업 : 데이터가 잘 넘어오는지 콘솔에서 확인 : @RequestParam으로 인해 orderGoodForm.jsp를 통해 입력한 값들이 receiverMap에 저장되고 출력
		System.out.println("확인 : " + receiverMap.toString());
		
		
		//결제하기 수업 : 결제하기 요청 (카드결제_구인증_API_규격서에 있는 필수값들 변수명은 규격서내 이름과 일치하게 작성)
		String merchantId="";
		String orderNumber ="";  //주문번호
		String cardNo ="";  // 카드번호
		String expireMonth ="";  //유효기간
		String expireYear ="";
		String birthday ="";
		String cardPw ="";
		String amount ="";
		String quota ="";
		String itemName ="";
		String userName ="";
		String signature ="";
		String timestamp ="";
		String apiCertKey =""; //api인증키
		
		//위에 변수설정하였고 값을 셋팅
		merchantId="himedia";
		apiCertKey="ac805b30517f4fd08e3e80490e559f8e";
		orderNumber = "TEST_choi1234"; // 주문번호 생성
		cardNo = receiverMap.get("cardNo"); //화면에서 받은 값
		expireMonth = receiverMap.get("expireMonth"); //화면에서 받은 값
		expireYear = receiverMap.get("expireYear"); //화면에서 받은 값
		birthday = receiverMap.get("birthday"); //화면에서 받은 값
		cardPw = receiverMap.get("cardPw"); //화면에서 받은 값
		amount = "1000";
		quota = "0"; //일시불
		itemName = "책";
		userName = receiverMap.get("receiver_name");//화면에서 받은 값
		signature = ""; //서명값
		timestamp ="20230501";
		
		
		signature = apiService01.encrypt(merchantId+"|"+orderNumber+"|"+amount+"|"+apiCertKey+"|"+timestamp);  //서명값
		
		
		//rest api를 라이브러리 써서 사용
		//가장 평범한 통신은 httpURLconnection 으로 하는 통신 (아래에서 사용X)
		String url="https://api.testpayup.co.kr/v2/api/payment/"+merchantId+"/keyin2";
		Map<String,String> map = new HashMap<String,String>();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		//map에다가 요청데이터값들을 넣으면 된다
		map.put("merchantId",merchantId);
		map.put("orderNumber",orderNumber);
		map.put("cardNo",cardNo);
		map.put("expireMonth",expireMonth);
		map.put("expireYear",expireYear);
		map.put("birthday",birthday);
		map.put("cardPw",cardPw);
		map.put("amount",amount);
		map.put("quota",quota);
		map.put("itemName",itemName);
		map.put("userName",userName);
		map.put("signature",signature);
		map.put("timestamp",timestamp);
		
		returnMap=apiService01.restApi(map, url);
		
		System.out.println("카드승인 응답 데이터" + returnMap.toString());
		
		//응답값을 잘 받으면
		//승인 성공 or 실패
		String responseCode= (String) returnMap.get("responseCode");
		
		if("0000".equals(responseCode)) {
			
	//mav.setViewName("/order/orderResultOk");
	mav.setViewName("/order/orderResult");
			
			mav.addObject("responseCode",returnMap.get("responseCode"));
			mav.addObject("responseMsg",returnMap.get("responseMsg"));
			
			//성공
			System.out.println(responseCode+"결제성공");
		}else {
			
			mav.setViewName("/order/orderResultfail");
			
			mav.addObject("responseCode",returnMap.get("responseCode"));
			mav.addObject("responseMsg",returnMap.get("responseMsg"));
			
			System.out.println(responseCode+"결제실패");
			//페이지 설정
		}
		
		
		
		
		HttpSession session=request.getSession();
		MemberVO memberVO=(MemberVO)session.getAttribute("orderer");
		String member_id=memberVO.getMember_id();
		String orderer_name=memberVO.getMember_name();
		String orderer_hp = memberVO.getHp1()+"-"+memberVO.getHp2()+"-"+memberVO.getHp3();
		List<OrderVO> myOrderList=(List<OrderVO>)session.getAttribute("myOrderList");
		
		//주문창에서 입력한 수령자 정보와 배송지 정보를 주문 상품 정보목록과 합칩니다
		for(int i=0; i<myOrderList.size();i++){
			OrderVO orderVO=(OrderVO)myOrderList.get(i);
			//각 orderVO에 수령자 정보를 설정한 후 다시 
			orderVO.setMember_id(member_id);
			orderVO.setOrderer_name(orderer_name);
			orderVO.setReceiver_name(receiverMap.get("receiver_name"));
			
			orderVO.setReceiver_hp1(receiverMap.get("receiver_hp1"));
			orderVO.setReceiver_hp2(receiverMap.get("receiver_hp2"));
			orderVO.setReceiver_hp3(receiverMap.get("receiver_hp3"));
			orderVO.setReceiver_tel1(receiverMap.get("receiver_tel1"));
			orderVO.setReceiver_tel2(receiverMap.get("receiver_tel2"));
			orderVO.setReceiver_tel3(receiverMap.get("receiver_tel3"));
			
			orderVO.setDelivery_address(receiverMap.get("delivery_address"));
			orderVO.setDelivery_message(receiverMap.get("delivery_message"));
			orderVO.setDelivery_method(receiverMap.get("delivery_method"));
			orderVO.setGift_wrapping(receiverMap.get("gift_wrapping"));
			orderVO.setPay_method(receiverMap.get("pay_method"));
			orderVO.setCard_com_name(receiverMap.get("card_com_name"));
			orderVO.setCard_pay_month(receiverMap.get("card_pay_month"));
			orderVO.setPay_orderer_hp_num(receiverMap.get("pay_orderer_hp_num"));	
			orderVO.setOrderer_hp(orderer_hp);	
			myOrderList.set(i, orderVO); //각 orderVO에 주문자 정보를 세팅한 후 다시 myOrderList에 저장한다.
		}//end for
		
		//주문정보를 sql문으로 정달
	    orderService.addNewOrder(myOrderList);
		mav.addObject("myOrderInfo",receiverMap);//OrderVO로 주문결과 페이지에  주문자 정보를 표시한다.
		mav.addObject("myOrderList", myOrderList);//주문완료 결과창에 주문상품 목록을 표시하도록 전달
		return mav;
	}

	//@RequestParam("cart_goods_qty") String[] cart_goods_qty로 선택한 상품수량을 배열로 받음
	@Override
	@RequestMapping(value="/orderAllCartGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderAllCartGoods(@RequestParam("cart_goods_qty") String[] cart_goods_qty, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session=request.getSession();
		//미리 세션에 저장한 장바구니 상품 목록을 가져옵니다
		Map cartMap=(Map)session.getAttribute("cartMap");
		List myOrderList=new ArrayList<OrderVO>();
		List<GoodsVO> myGoodsList=(List<GoodsVO>)cartMap.get("myGoodsList");
		
		MemberVO memberVO=(MemberVO)session.getAttribute("memberInfo");
		
		//장바구니 상품 개수만큼 반복
		for(int i=0; i<cart_goods_qty.length;i++){
			//문자열로 결합되어 전송된 상품번호와 주문수량을 split(":")메서드로 분리
			String[] cart_goods=cart_goods_qty[i].split(":");
			
			for(int j = 0; j< myGoodsList.size();j++) {
				//장바구니 목록에서 차례로 goodsVO를 가져옴
				GoodsVO goodsVO = myGoodsList.get(j);
				//goodsVO의 상품 번호를 가져옴
				int goods_id = goodsVO.getGoods_id();
				
				//전송된 상품 번호와 goodsVO의 상품번호가 같으면 주문하는 상품이므로 orderVO객체를
				//생성한 후 상품 정보를 orderVO에 설정합니다 그리고 다시 myOrderList에 저장합니다
				if(goods_id==Integer.parseInt(cart_goods[0])) {
					OrderVO _orderVO=new OrderVO();
					String goods_title=goodsVO.getGoods_title();
					//int goods_sales_price=goodsVO.getGoods_sales_price();
					int goods_sales_price=goodsVO.getGoods_sales_price();
					String goods_fileName=goodsVO.getGoods_fileName();
					_orderVO.setGoods_id(goods_id);
					_orderVO.setGoods_title(goods_title);
					_orderVO.setGoods_sales_price(goods_sales_price);
					_orderVO.setGoods_fileName(goods_fileName);
					_orderVO.setOrder_goods_qty(Integer.parseInt(cart_goods[1]));
					myOrderList.add(_orderVO);
					break;
				}
			}
		}
		//장바구니 목록에서 주문하기 위해 선택한 상품만 myOrderList에 저장한 후 세션에 바인딩
		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberVO);
		return mav;
	}

}
