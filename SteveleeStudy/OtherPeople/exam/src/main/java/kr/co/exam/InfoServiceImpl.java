package kr.co.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl implements InfoService {
	
	@Autowired private UserDao userDao;
	
	@Override
	public UserVO check_id(UserVO vo) {
		// TODO Auto-generated method stub
		
		return userDao.check_id(vo);
	}

}
