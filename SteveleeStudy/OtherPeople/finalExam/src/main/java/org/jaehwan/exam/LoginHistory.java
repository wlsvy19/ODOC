package org.jaehwan.exam;

import java.util.Date;

public class LoginHistory {
	
	private String id;
	private String pw;
	private String sessionid;
	private Date loginDate;
	
	public LoginHistory() {
		// TODO Auto-generated constructor stub
	}
	
	public LoginHistory(String id, String pw, String sessionid, Date loginDate) {
		this.id = id;
		this.pw = pw;
		this.sessionid = sessionid;
		this.loginDate = loginDate;
	}
	
	public String getId() {
		return id;
	}
	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	
	

}
