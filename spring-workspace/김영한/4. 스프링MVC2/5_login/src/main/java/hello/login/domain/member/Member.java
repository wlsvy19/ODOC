package hello.login.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {
	private Long id; // DB에 저장되는 ID

	@NotEmpty
	private String loginId; // 로그인할 때 ID
	@NotEmpty
	private String name; // 사용자 이름
	@NotEmpty
	private String password; // 패스워드
}
