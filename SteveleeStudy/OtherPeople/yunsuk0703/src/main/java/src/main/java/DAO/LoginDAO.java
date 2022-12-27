package src.main.java.DAO;

import java.lang.reflect.Member;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public class LoginDAO {
	
	@Autowired
	SqlSession sqlSession;
	
	public String selectMember(Member member) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("src/main/java/DAO/LoginDAO" + "selectMember" + member);
		
	}
	
	
}
