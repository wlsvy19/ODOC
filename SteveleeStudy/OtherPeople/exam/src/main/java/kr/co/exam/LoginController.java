package kr.co.exam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	
	

	@Autowired private InfoServiceImpl infoService;

	@RequestMapping(value = "/")
	public String login(HttpSession session, HttpServletRequest request, Model model) {
		// 페이지 열었을 때 세션 존재하는 지 확인 
		
	 
	 session = request.getSession();
	 String login = (String) session.getAttribute("id"); //새션이 있는 지 확인하고 
	 
	 
     if(login == null) { // 없으면 쿠키를 찾다본다.. 
    	 
    	//request.getCookies();
    	//usercookie.getValue(); // session id 를 가져온다 
    	 
    	 
     } else {
    	 model.addAttribute("login", login);
    	 
     }
     
		
		
		
		
		return "login";
	}
	
	@ResponseBody
	@RequestMapping(value = "/loginaction", method = RequestMethod.POST)
	public String loginAction(@RequestBody UserVO vo,HttpSession session,HttpServletResponse response) {
		
		System.out.println(vo.getId());
		
		String id = vo.getId();
		String pw = vo.getPw();
		
		UserVO result = infoService.check_id(vo); // db에서 가져온 값 
	
		if(result != null) {
			if(id.equals(result.getId()) && pw.equals(result.getPw())) {
				System.out.println("성공");
				// 로그인 성공 

				
				  Cookie usercookie = new Cookie("login",session.getId()); // 쿠키만들어서 sessionid저장
				  usercookie.setPath("/"); 
				  usercookie.setMaxAge(60*60*24*7); 
				  response.addCookie(usercookie); // 클라이언트에 넣어주기 
				  
				  session.setAttribute("id",result.getId()); // 세션에 저장
				  session.setAttribute("pw",result.getPw());
				 						
				
				
				return "/loginlist";
			
			} else {
				System.out.println("실패");				
				// 로그인 실패 


				return "/login";
			
			}
		} else { // 아이디 없음 
			return "/login";
		
		}
		
		
		
	}
	

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value="/loginactionpost", method = RequestMethod.POST) public
	 * String postAction(@RequestBody UserVO vo, Model model) {
	 * System.out.println("======post");
	 * 
	 * return "loginlist"; }
	 * 
	 * 
	 */
	

}
