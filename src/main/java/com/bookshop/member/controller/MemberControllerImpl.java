package com.bookshop.member.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.common.base.BaseController;
import com.bookshop.member.controller.MemberController;
import com.bookshop.member.service.MemberService;
import com.bookshop.member.vo.MemberVO;

@Controller("memberController")
@RequestMapping(value="/member")
public class MemberControllerImpl extends BaseController implements MemberController{
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVO memberVO;

	//@RequestParam Map<String, String> loginMap���� ID�� ��й�ȣ�� MAP�� ����
	@Override
	@RequestMapping(value="/login.do" ,method = RequestMethod.POST)
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav=new ModelAndView();
		//sql������ ����
		memberVO=memberService.login(loginMap);
		//�α��ο� ������ ���
		if(memberVO !=null && memberVO.getMember_id()!=null) {
			//��ȸ�� ȸ�������� ������ isLogOn�� �Ӽ��� true�� �����ϰ� memberInfo�Ӽ����� ȸ�������� �����մϴ�
			HttpSession session=request.getSession();
			session=request.getSession();
			// isLogOn�̶�� �Ӽ��� true�� ������ �α��� �������� ��Ÿ���ϴ�.
			session.setAttribute("isLogOn", true);
			//memberInfo��� �Ӽ��� �α����� ȸ�� ������ �����մϴ�.
			session.setAttribute("memberInfo", memberVO);
			
			//��ǰ �ֹ��������� �α��������� �α��� �� �ٽ� �ֹ��������� �����Ѵ� 
			//���ǿ��� action�̶�� �Ӽ� ���� �����ɴϴ�.
			String action=(String) session.getAttribute("action");
			//action�� "/order/orderEachGoods.do"�� ���, �ֹ� ������ �����մϴ�.
			if(action !=null && action.equals("/order/orderEachGoods.do")) {
				//forward ����� �̵��� URL���� ������ ����� ��ȯ�ϴ� ���� �ƴ϶�, �ٽ� �̵��� URL�� �ǵ��ƿ��� �˴ϴ�.
				mav.setViewName("forward:"+action);
				//�׷��� ������ ������������ ǥ��
			}else {
				mav.setViewName("redirect:/main/main.do");
			}
			
		}else {
			String message="���̵� ��й�ȣ�� Ʋ���ϴ�. �ٽ� �α��� ���ּ���";
			mav.addObject("message",message);
			mav.setViewName("/member/loginForm");
		}
	
		return mav;
	}
	
	@Override
	@RequestMapping(value="/logout.do" ,method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		HttpSession session=request.getSession();
		session.setAttribute("isLogOn", false);
		session.removeAttribute("memberInfo");
		//redirect:Ŭ���̾�Ʈ ������ ������ ��û�� ������, ������ "/main/main.do" URL�� ���û�� ������ �Ǵ� ���� �ǹ��մϴ�.
		//POST ��û �� ��� �������� GET ��û���� ������� �� ��� � ���
		mav.setViewName("redirect:/main/main.do");
		return mav;
	}
	
	//ȸ�����Ը޼���
	@Override
	@RequestMapping(value="/addMember.do" ,method = RequestMethod.POST)
	public ResponseEntity addMember(@ModelAttribute("memberVO") MemberVO _memberVO,
			                HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
		    memberService.addMember(_memberVO);
		    message  = "<script>";
		    message +=" alert('ȸ�� ������ ���ƽ��ϴ�.�α���â���� �̵��մϴ�.');";
		    message += " location.href='"+request.getContextPath()+"/member/loginForm.do';";
		    message += " </script>";
		    
		}catch(Exception e) {
			message  = "<script>";
		    message +=" alert('�۾� �� ������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���');";
		    message += " location.href='"+request.getContextPath()+"/member/memberForm.do';";
		    message += " </script>";
			e.printStackTrace();
		}
		resEntity =new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
	}
	
	
	//���̵� �ߺ��˻� �޼���
	@Override
	@RequestMapping(value="/overlapped.do" ,method = RequestMethod.POST)
	public ResponseEntity overlapped(@RequestParam("id") String id,HttpServletRequest request, HttpServletResponse response) throws Exception{
		ResponseEntity resEntity = null;
		String result = memberService.overlapped(id);
		resEntity =new ResponseEntity(result, HttpStatus.OK);
		return resEntity;
	}

}
