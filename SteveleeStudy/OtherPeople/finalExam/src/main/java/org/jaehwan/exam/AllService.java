package org.jaehwan.exam;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllService {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void insertHistory(LoginHistory history) {
		ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
		dao.insertHistory(history.getId(), history.getPw(), history.getSessionid(), history.getLoginDate());
	}
	
	public Login findUser(Login login) {
		ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
		return dao.findUser(login.getId(), login.getPw());
	}
	
	public List<LoginHistory> findAllHistory() {
		ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
		return dao.findAllHistory();
	}
	
	public List<LoginHistory> findHistoryById(String id) {
		ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
		return dao.findHistoryById(id);
	}

}
