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

	//@RequestParam Map<String, String> loginMap으로 ID와 비밀번호를 MAP에 저장
	@Override
	@RequestMapping(value="/login.do" ,method = RequestMethod.POST)
	public ModelAndView login(@RequestParam Map<String, String> loginMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav=new ModelAndView();
		//sql문으로 전달
		memberVO=memberService.login(loginMap);
		//로그인에 성공한 경우
		if(memberVO !=null && memberVO.getMember_id()!=null) {
			//조회한 회원정보를 가져와 isLogOn의 속성을 true로 설정하고 memberInfo속성으로 회원정보를 저장합니다
			HttpSession session=request.getSession();
			session=request.getSession();
			// isLogOn이라는 속성을 true로 설정해 로그인 상태임을 나타냅니다.
			session.setAttribute("isLogOn", true);
			//memberInfo라는 속성에 로그인한 회원 정보를 저장합니다.
			session.setAttribute("memberInfo", memberVO);
			
			//상품 주문과정에서 로그인했으면 로그인 후 다시 주문과정으로 진행한다 
			//세션에서 action이라는 속성 값을 가져옵니다.
			String action=(String) session.getAttribute("action");
			//action이 "/order/orderEachGoods.do"인 경우, 주문 과정을 진행합니다.
			if(action !=null && action.equals("/order/orderEachGoods.do")) {
				//forward 방식은 이동한 URL에서 생성된 결과를 반환하는 것이 아니라, 다시 이동한 URL로 되돌아오게 됩니다.
				mav.setViewName("forward:"+action);
				//그렇지 않으면 메인페이지를 표시
			}else {
				mav.setViewName("redirect:/main/main.do");
			}
			
		}else {
			String message="아이디나 비밀번호가 틀립니다. 다시 로그인 해주세요";
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
		//redirect:클라이언트 측에서 서버로 요청을 보내면, 서버는 "/main/main.do" URL로 재요청을 보내게 되는 것을 의미합니다.
		//POST 요청 후 결과 페이지를 GET 요청으로 보여줘야 할 경우 등에 사용
		mav.setViewName("redirect:/main/main.do");
		return mav;
	}
	
	//회원가입메서드
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
		    message +=" alert('회원 가입을 마쳤습니다.로그인창으로 이동합니다.');";
		    message += " location.href='"+request.getContextPath()+"/member/loginForm.do';";
		    message += " </script>";
		    
		}catch(Exception e) {
			message  = "<script>";
		    message +=" alert('작업 중 오류가 발생했습니다. 다시 시도해 주세요');";
		    message += " location.href='"+request.getContextPath()+"/member/memberForm.do';";
		    message += " </script>";
			e.printStackTrace();
		}
		resEntity =new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
	}
	
	
	//아이디 중복검사 메서드
	@Override
	@RequestMapping(value="/overlapped.do" ,method = RequestMethod.POST)
	public ResponseEntity overlapped(@RequestParam("id") String id,HttpServletRequest request, HttpServletResponse response) throws Exception{
		ResponseEntity resEntity = null;
		String result = memberService.overlapped(id);
		resEntity =new ResponseEntity(result, HttpStatus.OK);
		return resEntity;
	}

}
