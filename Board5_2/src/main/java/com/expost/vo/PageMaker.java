package com.expost.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

@Getter
@ToString(exclude="pageList")
@Log
public class PageMaker<T> {
	private Page<T> result;
	
	private Pageable nextPage;
	private Pageable prevPage;
	
	private int currentPageNum;
	private int totalPageNum;
	
	private Pageable currentPage;
	private List<Pageable> pageList;
	
	public PageMaker(Page<T> result) { // 페이지 객체를 매개변수로 받아서 Pageable 객체로.
		//페이지 번호 출력에 필요한 정보 처리.
		this.result = result;
		this.currentPage = result.getPageable();
		this.currentPageNum = currentPage.getPageNumber();
		this.totalPageNum = result.getTotalPages();
		this.pageList = new ArrayList<>();
		calcPages();
	}
	public void calcPages() {
		int tempEndNum = (int)(Math.ceil(this.currentPageNum/10.0)*10);
		int startNum = tempEndNum - 9;
		
		Pageable startPage = this.currentPage;
		
		for(int i=startNum;i<tempEndNum;i++) {
			startPage = startPage.previousOrFirst();
		}
		this.prevPage = startPage.getPageNumber() <= 0? null : startPage.previousOrFirst();
		
		// nextPage
		if(this.totalPageNum<tempEndNum) {
			tempEndNum = this.totalPageNum;
			this.nextPage = null;
		}

		for(int i=startNum;i<=tempEndNum;i++) {
			pageList.add(startPage);
			startPage = startPage.next();
		}
		this.nextPage = startPage.getPageNumber() +1< totalPageNum ? startPage:null;
		
	}
}
