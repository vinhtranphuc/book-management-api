package com.book.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.book.common.FieldMap;
import com.book.common.Utils;
import com.book.mybatis.mapper.AuthorMapper;
import com.book.mybatis.mapper.BookMapper;

@Service
public class LibaryService {
	
	@Resource
	private BookMapper bookMapper;
	
	@Resource
	private AuthorMapper authorMapper;
	
	private List<FieldMap> books;
	
	private List<FieldMap> authors;
	
	/**
	 * get book list with page
	 * @param params
	 * @return
	 */
	public Map<String,Object> getBooks(Map<String, Object> params) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		String page = (String) params.get("page");
		String recordsNo = (String) params.get("records_no");
		
		int pageInt = 0;
		int lastPage = 0;
		
		int totalRecords = bookMapper.selectBooksTotCnt(params);
		result.put("total_records", totalRecords);

		// get list of page
		if(Utils.isInteger(page) && Utils.isInteger(recordsNo)) {
			
			int recordsNoInt = Integer.parseInt(recordsNo);
			if(recordsNoInt == 0) {
				return result;
			}
			
			if(totalRecords < recordsNoInt) {
				books = bookMapper.selectBooks(params);
				result.put("page_of_list", 1);
				result.put("last_page", 1);
			} else {
				lastPage = totalRecords/recordsNoInt + ((totalRecords%recordsNoInt)>0?1:0);
				pageInt = Integer.parseInt(page);
				pageInt = pageInt<=0?lastPage:pageInt>lastPage?1:pageInt;
				
				int startPost = (pageInt-1)*recordsNoInt;
				params.put("start_list", startPost);
				books = bookMapper.selectBooks(params);
				
				result.put("page_of_list", pageInt);
				result.put("last_page", lastPage);
			}
		} else {
			books =  bookMapper.selectBooks(params);
			result.put("page_of_list", 1);
			result.put("last_page", 1);
		}
		
		books.stream().forEach(t-> {
			t.put("authors", authorMapper.selectAuthorsByBookId(t));
		});
		result.put("list", books);
		return result;
	}

	/**
	 * get all authors
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getAuthors() {
		return authorMapper.selectAuthors();
	}
	
	/**
	 * get page of author list
	 * @param params
	 * @return
	 */
	public Map<String, Object> getPageAuthors(Map<String,Object> params) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		String page = (String) params.get("page");
		String recordsNo = (String) params.get("records_no");
		
		int pageInt = 0;
		int lastPage = 0;
		
		int totalRecords = authorMapper.selectAuthorsTotCnt(params);
		result.put("total_records", totalRecords);

		// get list of page
		if(Utils.isInteger(page) && Utils.isInteger(recordsNo)) {
			
			int recordsNoInt = Integer.parseInt(recordsNo);
			if(recordsNoInt == 0) {
				return result;
			}
			
			if(totalRecords < recordsNoInt) {
				authors = authorMapper.selectPageAuthors(params);
				result.put("page_of_list", 1);
				result.put("last_page", 1);
			} else {
				lastPage = totalRecords/recordsNoInt + ((totalRecords%recordsNoInt)>0?1:0);
				pageInt = Integer.parseInt(page);
				pageInt = pageInt<=0?lastPage:pageInt>lastPage?1:pageInt;
				
				int startPost = (pageInt-1)*recordsNoInt;
				params.put("start_list", startPost);
				authors = authorMapper.selectPageAuthors(params);
				
				result.put("page_of_list", pageInt);
				result.put("last_page", lastPage);
			}
		} else {
			authors =  authorMapper.selectPageAuthors(params);
			result.put("page_of_list", 1);
			result.put("last_page", 1);
		}
		
		result.put("list", authors);
		return result;
	}

	public void createAuthor(Map<String, Object> params) {
		authorMapper.insertAuthor(params);
	}
	
	@Transactional
	public void editAuthor(@Valid Map<String, Object> params) {
		authorMapper.updateAuthor(params);
	}
	
	@Transactional
	public void deleteAuthor(Map<String, Object> params) {
		authorMapper.deleteBooksAuthors(params);
		authorMapper.deleteAuthor(params);
	}
	
	@Transactional
	public void createBook(Map<String, Object> params) {
		
		@SuppressWarnings("unchecked")
		List<Integer> authors = (List<Integer>) params.get("authors");
		
		bookMapper.insertBook(params);
		authors.stream().forEach(t->{
			params.put("author_id", t);
			bookMapper.insertBooksAuthors(params);
		});
	}
	
	@Transactional
	public void editBook(Map<String, Object> params) {
		
		bookMapper.deleteBooksAuthorsByBookId(params);
		
		@SuppressWarnings("unchecked")
		List<Integer> authors = (List<Integer>) params.get("authors");
		authors.stream().forEach(t->{
			params.put("author_id", t);
			bookMapper.insertBooksAuthors(params);
		});
		
		bookMapper.updateBook(params);
	}

	public void deleteBook(Map<String, Object> params) {
		bookMapper.deleteBooksAuthorsByBookId(params);
		bookMapper.deleteBook(params);
	}
}
