package kr.co.exam;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements InfoService {


	
	@Autowired private SqlSession sqlSession;

	@Override
	public UserVO check_id(UserVO vo) {
		// TODO Auto-generated method stub
		System.out.println("dao");
		String id = vo.getId();
		Object result = sqlSession.selectOne("mapper.selectOneUser", id);
		System.out.println(result);
	
	return sqlSession.selectOne("mapper.selectOneUser", id);
	}

}
