package com.poseidon.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.poseidon.dto.BoardDTO;
import com.poseidon.dto.LoginDTO;
import com.poseidon.service.WooriService;

@Controller
public class WooriController {

	@Autowired
	private WooriService wooriService;

	@RequestMapping(value = "/")
	public String index(HttpServletRequest request) throws Exception {
		//ModelAndView mv = new ModelAndView("index");
		return "index";
	}

	@GetMapping(value = "/main")
	public ModelAndView main(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("main");
		//DB에 데이터 요청하기
		List<BoardDTO> list = wooriService.boardList();
		//System.out.println(list);
		mv.addObject("list", list);
		return mv;
	}

	@PostMapping("/login")
	public String login(LoginDTO dto, HttpServletRequest request) {
		LoginDTO result = wooriService.login(dto);
		if (result.getCount() == 1) {
			// 세션만들어주기
			HttpSession session = request.getSession();
			session.setAttribute("name", result.getName());
			session.setAttribute("id", dto.getId());
			return "redirect:/main";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if(session.getAttribute("name") != null || session.getAttribute("id") != null) {			
			session.invalidate();// 모두 초기화!!!!!
		}
		return "redirect:/";// 컨트롤러 다시 돌아가기
	}

	
	//2022-11-21 빅데이터 처리시스템 개발 / 빅데이터 처리시스템 설계하기
	@PostMapping("/write")
	public String write(HttpServletRequest request) throws UnsupportedEncodingException {
		//한글설정
		request.setCharacterEncoding("UTF-8");
		//로그인 했는지 여부를 먼저 물어보기

		//세션에서 사용자 정보 가저오기
		HttpSession session = request.getSession();
		if(session.getAttribute("id") != null 
									&& request.getParameter("title") != null 
									&& request.getParameter("content") != null) {
			//데이터베이스에 글 저장하기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			BoardDTO dto = new BoardDTO();
			dto.setBoard_title(title);
			dto.setBoard_content(content);
			dto.setName((String) session.getAttribute("id"));
			//dto.setMno(3); 번호를 모릅니다. 단, id는 알고 있습니다. 그래서 id를....
			int result = wooriService.write(dto);
			
			if(result == 1) {
				return "redirect:/main?result=ok";//정상
			} else {
				return "redirect:/main?result=error";//문제 발생
			}
		} else {
			return "redirect:/";//로그인 화면으로 던지기
		}
	}
	//그냥 페이지를 열기만 한다  = String
	//데이터를 붙여서 열어야 한다 = ModelAndView
	
	@GetMapping("/detail")
	public ModelAndView detail(HttpServletRequest request) {//bno=글번호
		ModelAndView mv = new ModelAndView("detail");
		if(request.getParameter("bno") != null) {//숫자인데 왜?			
			//System.out.println(request.getParameter("bno"));
			int bno = Integer.parseInt(request.getParameter("bno"));
			BoardDTO dto= new BoardDTO();
			dto.setBoard_no(bno);
			// 컨트롤러 -> 서비스 -> DAO -> sqlSession ->dB
			BoardDTO detail = wooriService.detail(dto);  // bno->dto 수
//			System.out.println(detail.getBoard_title());
//			System.out.println(detail.getBoard_content());
//			System.out.println(detail.getBoard_no());
			mv.addObject("detail", detail); 
			
			
		} else {
			// 작업을 안하면된다 
		}
		
		return mv;
	}
	
	
	@GetMapping("/delete")
	public String delete(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("id") != null) {
			//출력문으로bno 찍기
			System.out.println(request.getParameter("bno"));
			int bno = Integer.parseInt(request.getParameter("bno"));
			BoardDTO dto = new BoardDTO();
			dto.setBoard_no(bno);
			dto.setMid((String)session.getAttribute("id"));
			
			wooriService.delete(dto); // void반환타입이 없습니다. 
			return "redirect:/main";
		} else {
			return "redirect:/";
		}
	}
	
	//업데이트만들기 22-11-22
	//수정하기 -> db내용 가져오기 -> 화면에 출력 -> 수정 -> 저장 -db저
	// httpServlet으로 하는 방법이 있지만 RequstParam이 코드가 더 단순해진
	@GetMapping("/update")
	public ModelAndView update(HttpSession session, @RequestParam(value="bno", required = true) int bno) {
		// 파라미터로 들어오는 bno값을 자동으로 int bno로 변환하고 필수값으로 지정해준다
		
		ModelAndView mv = new ModelAndView("update");
		
		//로그인검사 + bno검사 
		if(session.getAttribute("id") != null) {
			//데이터 가져오기 
			BoardDTO dto =new BoardDTO();
			dto.setBoard_no(bno);
			dto.setMid((String) session.getAttribute("id"));
			
			BoardDTO result = wooriService.detail(dto);
			
			System.out.println(result.getBoard_no());
			System.out.println(result.getBoard_title());
			System.out.println(result.getBoard_content());

			mv.addObject("update", result);
			
			mv.setViewName("update");
			
		}
		//System.out.println("paramd 이 잡은 bno: " + bno);
		return mv;
	}
	
	
	@PostMapping("/update")
	public String update(HttpServletRequest request) {
		System.out.println(request.getParameter("bno"));
		System.out.println(request.getParameter("title"));
		System.out.println(request.getParameter("content"));
		//return "redirect:/detail?bno="+request.getParameter("bno");
		
		BoardDTO dto =new BoardDTO();
		//int bno = interger.parsInt(requst.getParameter("bno"));
		dto.setBoard_no(Integer.parseInt(request.getParameter("bno")));
		dto.setBoard_title(request.getParameter("title"));
		dto.setBoard_content(request.getParameter("content"));
		//세션
		HttpSession session=request.getSession();
		dto.setMid((String)session.getAttribute("id"));
		
		wooriService.update(dto);
		
		
		return "redirect:/detail?bno="+request.getParameter("bno");
		
	}
	
	
	
	
	
	
}