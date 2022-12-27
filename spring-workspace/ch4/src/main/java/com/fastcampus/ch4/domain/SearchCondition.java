package com.fastcampus.ch4.domain;

import org.springframework.web.util.UriComponentsBuilder;

/**
 * 검색 조건을 위한 domain
 */
public class SearchCondition {
    private Integer page = 1;
    private Integer pageSize = 10;
    //    private Integer offset = 0;
    private String keyword = "";
    private String option = ""; // 제목+내용 , 제목 , 내용

    public SearchCondition() {
    }

    public SearchCondition(Integer page, Integer pageSize, String keyword, String option) {
        this.page = page;
        this.pageSize = pageSize;
        this.keyword = keyword;
        this.option = option;
    }


    // 지정된 페이지를 보여주는 쿼리 스트링
    public String getQueryString(Integer page) {
        // ?page=1&pageSize=10&option=T&keyword="title"
        return UriComponentsBuilder.newInstance()
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .queryParam("option", option)
                .queryParam("keyword", keyword)
                .build().toString();
    }

    // 페이지 미지정시 보여주는 쿼리 스트링(중복제거 하기 위해 위에꺼 갔다씀)
    public String getQueryString() {
        return getQueryString(page);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return ((page - 1) * pageSize);
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "SearchCondition{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", offset=" + getOffset() +
                ", keyword='" + keyword + '\'' +
                ", option='" + option + '\'' +
                '}';
    }
}
