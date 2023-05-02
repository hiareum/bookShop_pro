package com.bookshop.order.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.order.service.ApiService01;
import com.bookshop.order.vo.OrderVO;

//������ kakaocontroller�� �ִµ� �� �� ����� kakaocontroller�� @restController�̱⶧���� view�� ����� ����
@Controller
public class NaverController2 {

	@Autowired
	private ApiService01 apiService01;
	


	@RequestMapping(value = "/test/naverPay.do", method = RequestMethod.POST)
	public ModelAndView naverPay(@RequestParam Map<String, String> map) throws Exception {

		ModelAndView mav = new ModelAndView();
		System.out.println(map.toString());
		// īī�����̷� ���� �������� �� map�� ����� ���������Ͱ� �� ����
		// ordr_idxx=20230502101446KK0633, good_name=Try! helloworld ????????,
		// good_mny=100, buyr_name=??????, site_cd=A8QOB, req_tx=pay,
		// pay_method=100000000000, currency=410, kakaopay_direct=Y, module_type=01,
		// ordr_chk=20230502101446KK0633|100, param_opt_1=, param_opt_2=, param_opt_3=,
		// res_cd=0000, res_msg=????,
		// enc_info=2nSyaAt9kyDnseCuNnp-f2RRd0ReWaeYKjPN6J7H5kUJLTZYPASjB4NEpV2y3fQMAcb-DUUVhciH0iCEv.m3i7vNVDos4KukeF9c0JmO3xeuw6nNTVAkexSVk59Fi-ii3TI8mRfMnHf4vrU9651h9CDgBpK8jrAXzcSjLIGCq73yBzNsY79q6Zt23kRVeo.z9O.0IJYkJ6j__,
		// enc_data=4YY2aQsN1A1lYMG-mNlxKrDJ8Gs80goXnXtcwpYbyvc8ciAOCk1ignqZ0AOWf4qGi-dEbogelXPFzrqWDg9qTtLTIT.ptOH5BiyVFG1YPmwe9.50zmNT1mSWAUQWpqWnJhFRf1.BXWHBhURYZx4gXdTyILBPjY-5adxymgGVUOip1LUpsGo1X5PkGXFQ7BelHJjeW22CpzCdKVoROa4ro2mgu94FzmC9XevGlZglVNkoolIquUX407wyQK2WhCQ9mimIAqJjR6KVNIxX-NSnL.-3LXgYq-MAz8W78N0-Q9w3AcKVuWdav8umz0DrFalK1PqMrPRLbCl45JOq3TmwY.UlhDsl65RsXijytjUjCR6i4SO1lDXyX4j2FDWywq9jevCkH26y.18GdwqK8ZNJ83h7hvpf5zYl1INTQYtwW2nE8w9PSLt1583nUhfPDIcC5Yr3UfQq8oNqW9pxFBqtgb4n0R3fedsQ.IoFnetEP2Uu2phemg9dhoEZZK5zICxufXaEvhSZW5cw6Is1-ZgRKVE82wri2LVGtcA4OeWQ1XMueSsq0N3aS4Y0IfomtxAdBrN9Q3v.Hh4TySoKPzlpzDUh9GpNcI1aRvmobcx1h0Vj3i8-IW.ue0vIDMFXK1MT7BQ0FYAYPjcZWFoTzNep2ZwMGDc5KwdK5gAlr1Cq6l6gb0AMioDiu8Zb9shLxPI6Q,
		// ret_pay_method=CARD, tran_cd=00100000, use_pay_method=100000000000,
		// card_pay_method=KAKAO_MONEY}

		// 4 ���������ͷ� ���� ��û�ϱ� (rest api���)
		// ��û(���� api��Ž� �������ϴ� �����͸� ����)
		String res_cd = map.get("res_cd");
		String enc_info = map.get("enc_info");
		String enc_data = map.get("enc_data");
		String tran_cd = map.get("tran_cd");
		String ordr_idxx = map.get("ordr_idxx");

		String merchantId = "himedia";

		String url = "https://api.testpayup.co.kr/ep/api/naver/" + merchantId + "/pay";

		Map<String,String> map01 = new HashMap<String,String>();
		Map<String, Object> returnMap = new HashMap<String, Object>();

		map01.put("res_cd", res_cd);
		map01.put("enc_info", enc_info);
		map01.put("enc_data", enc_data);
		map01.put("tran_cd", tran_cd);
		map01.put("ordr_idxx", ordr_idxx);

		// apiService01.restApi(���⿡ ��(��û���� ����־����), url(Ǯ URL));
		//���� �����ϰ������ �ּ�Ǯ��
	//	returnMap = apiService01.restApi(map, url);
		
		//�׽�Ʈ �����Ϳ�(���Ƿ� ����)  �����������ϰ������ �ּ��ޱ�
				returnMap.put("responseCode", "0000");
				returnMap.put("responseMsg", "����");
				returnMap.put("type", "NAVER_MONEY"); 
				returnMap.put("amount", "100"); 
	

		// returnMap�� ���䰪�� ������� ����
		System.out.println("���̹� ���� ������" + returnMap.toString());

		// �̰� ����Ǹ� ������ ���� view������ �ȵǾ� �ֱ� ������
		// view����
		// ���� ���� or ����
		
	
		String responseCode = (String) returnMap.get("responseCode");

		if ("0000".equals(responseCode)) {

			// mav.setViewName("/order/orderResultOk");
			mav.setViewName("/naver/naverResult");

			//�ϳ��� ȭ������ ���� ��
		//	mav.addObject("authDateTime", returnMap.get("authDateTime"));
//			mav.addObject("type", returnMap.get("type"));
		
			//��°�� ȭ������ ���� ��
			mav.addObject("returnMap", returnMap);
			

			// ����
			System.out.println(responseCode + "��������");
		} else {

			mav.setViewName("/naver/naverResultfail");

			mav.addObject("returnMap", returnMap);
			System.out.println(responseCode + "��������");
			// ������ ����
		}

		return mav;

	}

}
