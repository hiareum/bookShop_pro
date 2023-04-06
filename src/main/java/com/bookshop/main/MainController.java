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

//�̸��� �������� �ʾƵ� ������ �̸����� ���� ����� �� �ֱ⶧����
//������ ���� �� ���ϰ� ��밡���ϹǷ� �̸��� �����ָ� ����
@Controller("mainController")
//AOP���� AspectJ�� ���� ���� ������ AOP ����� ��밡��
@EnableAspectJAutoProxy
public class MainController {
	
	//��ü�� �ڵ����� ����
	@Autowired
	private GoodsService goodsService;
	
	///main/main.do ������ POST�� GET��û ó��
	@RequestMapping(value = "/main/main.do", method={RequestMethod.POST,RequestMethod.GET})
	//�޼��� �̸�:main / HttpServletRequest request, HttpServletResponse response) throws Exception�� 
	// Servlet API�� ����Ͽ� Ŭ���̾�Ʈ�κ��� ��û(request)�� �޾� ó���ϰ� ����(response)�� ������ �޼ҵ忡�� �Ű������� ���
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//HttpSession ��ü�� ���� ������� ���¸� �����ϱ����� ������ �����
		HttpSession session;
		//ModelAndView ��ü�� ����
		ModelAndView mav=new ModelAndView();
		//Ŭ���̾�Ʈ�� ��û(request) ��ü���� "viewName"�̶�� �Ӽ��� �����ͼ� String Ÿ���� ���� viewName�� �����մϴ�.
		//�Ƹ�:����ڰ� �̵��ϴ� ȭ�鸶�� ������� ��� �ٸ��� ��û������ �޶����� ��� ������� ó���Ѵٴ� ��!
		String viewName=(String)request.getAttribute("viewName");
		//ModelAndView ��ü�� ��(view) �̸��� �����մϴ�. viewName�� �տ��� ������ Ŭ���̾�Ʈ�� ��û(request)���� "viewName" �Ӽ��� ���Դϴ�.
		//�Ƹ� : ȭ�鿡 ��û���� ������� ��ƾ�Ѵٴ� ��
		mav.setViewName(viewName);
		//HttpSession ��ü�� �����մϴ�
		//����ڷκ��� ���ο� �並 ��û�޾ұ⶧���� ���ǿ� ������ �׷������� ��ü�� �����ϴ°�
		session=request.getSession();
		//side_menu"��� �̸����� "user" ���� HttpSession ��ü�� �����մϴ�.
		session.setAttribute("side_menu", "user");
		
		//GoodsVO�� ����Ʈ�� ������ Map ��ü�� goodsService���� �����ɴϴ�.
		//�Ƹ�:��û�� �޾� ����� ȭ�鿡 ��ǰ����Ʈ�� ������� �Ѵ� ������ list��������!
		Map<String,List<GoodsVO>> goodsMap=goodsService.listGoods();
		//ModelAndView ��ü�� "goodsMap"�̶�� �̸����� goodsMap ��ü�� �߰�
		//�Ƹ�:�׸��� ȭ�鿡 �����ֱ�
		mav.addObject("goodsMap", goodsMap);
		//ModelAndView ��ü�� ��ȯ�մϴ�. �� ��ü�� Controller���� ó���� ����� View�� ����
		return mav;
	}
	
}
