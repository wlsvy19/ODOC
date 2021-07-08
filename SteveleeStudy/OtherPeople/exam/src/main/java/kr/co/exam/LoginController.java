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
		// ������ ������ �� ���� �����ϴ� �� Ȯ�� 
		
	 
	 session = request.getSession();
	 String login = (String) session.getAttribute("id"); //������ �ִ� �� Ȯ���ϰ� 
	 
	 
     if(login == null) { // ������ ��Ű�� ã�ٺ���.. 
    	 
    	//request.getCookies();
    	//usercookie.getValue(); // session id �� �����´� 
    	 
    	 
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
		
		UserVO result = infoService.check_id(vo); // db���� ������ �� 
	
		if(result != null) {
			if(id.equals(result.getId()) && pw.equals(result.getPw())) {
				System.out.println("����");
				// �α��� ���� 

				
				  Cookie usercookie = new Cookie("login",session.getId()); // ��Ű���� sessionid����
				  usercookie.setPath("/"); 
				  usercookie.setMaxAge(60*60*24*7); 
				  response.addCookie(usercookie); // Ŭ���̾�Ʈ�� �־��ֱ� 
				  
				  session.setAttribute("id",result.getId()); // ���ǿ� ����
				  session.setAttribute("pw",result.getPw());
				 						
				
				
				return "/loginlist";
			
			} else {
				System.out.println("����");				
				// �α��� ���� 


				return "/login";
			
			}
		} else { // ���̵� ���� 
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
