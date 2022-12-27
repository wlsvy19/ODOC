package org.jaehwan.exam;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ProjectDao {
	void insertHistory(@Param("id") String id, @Param("pw") String pw, @Param("sessionid") String sessionid, @Param("date") Date date);
	
	Login findUser(@Param("id") String id, @Param("pw") String pw);
	
	List<LoginHistory> findAllHistory();
	
	List<LoginHistory> findHistoryById(String id);
}
