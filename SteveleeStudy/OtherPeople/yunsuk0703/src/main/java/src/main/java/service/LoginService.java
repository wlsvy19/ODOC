package src.main.java.service;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import src.main.java.Member;
import src.main.java.DAO.LoginDAO;

@Component
public class LoginService {
	
	@Autowired
	LoginDAO loginDAO;
	public String memberLogin(Member member) {
	return loginDAO.selectMember(member);	
	}
	
}
