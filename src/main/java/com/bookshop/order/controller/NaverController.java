package com.bookshop.order.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.order.service.ApiService01;

@RestController
public class NaverController {

	@Autowired
	private ApiService01 apiService01;
	
	@RequestMapping(value= "/test/naverOrder.do")
	public Map<String,Object> naverOrder(@RequestParam Map<String, String> dateMap) throws Exception{
		
		
		System.out.println("들어오는 데이터 = " + dateMap.toString());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		//주문 연동 하기
		String id = "himedia"; //발급된 계정
		String base = "https://api.testpayup.co.kr";
		String path = "/ep/api/naver/"+id+"/order";
		
		String url = base+path;
		
		String signature = ""; 
		
		//파라미터로 사용할 맵
		Map<String,String> map = new HashMap<String,String>();
		map.put("orderNumber","TEST_12346"); //주문데이터 생성해야지 생기는 키값 (생성을 안해서 임의값)
//		map.put("amount",dateMap.get("amount")); //화면에서 받아온 값
		map.put("amount","100"); //화면에서 받아온 값 (테스트를 위해서 100원 고정)
		map.put("itemName",dateMap.get("itemName")); //화면에서 받아온 값
//		map.put("userName",dateMap.get("userName")); //화면에서 받아온 값
		map.put("userName","테스트중"); //(테스트를 위해서 고정)
		map.put("returnUrl","naver.com");//PC에서는 안중요함
		map.put("timestamp","1000");
		map.put("userAgent","WP");
		map.put("payType","CARD");
		
		
	
		signature = apiService01.encrypt(id+"|"+map.get("orderNumber")+"|"+map.get("amount")+"|ac805b30517f4fd08e3e80490e559f8e|"+map.get("timestamp"));
		
		map.put("signature",signature);
		resultMap = apiService01.restApi(map, url);
		System.out.println("나가는 데이터 = " + resultMap.toString());
		
		return resultMap;
	}
}