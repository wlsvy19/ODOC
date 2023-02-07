package com.example.hellospring.domain;

import javax.persistence.*;

@Entity // JPA가 관리하는 entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 자동 생성되는거다
    private Long id;

    // @Column(name="username") // DB 컬럼명과 매핑 설정 할 수 있음
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
