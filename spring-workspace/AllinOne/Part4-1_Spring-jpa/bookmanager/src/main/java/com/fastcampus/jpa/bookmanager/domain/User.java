package com.fastcampus.jpa.bookmanager.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

//@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 1씩증가
	private Long id;

	@NonNull
	private String name;

	@NonNull
	private String email;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

//	@OneToMany(fetch = FetchType.EAGER)
//	private List<Address> address;

	public User() {
	}
}
