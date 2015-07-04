package com.yuandu.erp.common.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.yuandu.erp.common.config.Global;
import com.yuandu.erp.common.utils.CookieUtils;

/**
 * 分页类
 * @param <T>
 */
public class FlexPage<T> {
	
	/** 当前第几页 */
	private int page;
	/** 每页大小 */
	private int pagesize = Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）;
	/** 排序的列名 */
	private String sortname;
	/** 升序或者降序; desc;asc */
	private String sortorder = "desc";
	/** 总数多少 */
	private int total;
	/** 查询出来的数据 */
	private List<T> rows = Lists.newArrayList();
	
	private int first;// 首页索引
	private int last;// 尾页索引
	
	public FlexPage(){}
	
	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 */
	public FlexPage(HttpServletRequest request, HttpServletResponse response){
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("page");
		if (StringUtils.isNumeric(no)){
			CookieUtils.setCookie(response, "page", no);
			this.setPage(Integer.parseInt(no));
		}else{
			no = CookieUtils.getCookie(request, "page");
			if (StringUtils.isNumeric(no)){
				this.setPage(Integer.parseInt(no));
			}
		}
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pagesize");
		if (StringUtils.isNumeric(size)){
			CookieUtils.setCookie(response, "pagesize", size);
			this.setPagesize(Integer.parseInt(size));
		}else {
			no = CookieUtils.getCookie(request, "pagesize");
			if (StringUtils.isNumeric(size)){
				this.setPagesize(Integer.parseInt(size));
			}
		}
		// 设置排序参数
		String orderBy = request.getParameter("sortname");
		if (StringUtils.isNotBlank(orderBy)){
			this.setSortname(sortname);
		}
		String sortorder = request.getParameter("sortorder");
		if (StringUtils.isNotBlank(sortorder)){
			this.setSortorder(sortorder);
		}
	}
	
	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 * @param defaultPageSize 默认分页大小，如果传递 -1 则为不分页，返回所有数据
	 */
	public FlexPage(HttpServletRequest request, HttpServletResponse response, int defaultPageSize){
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("page");
		if (StringUtils.isNumeric(no)){
			CookieUtils.setCookie(response, "page", no);
			this.setPage(Integer.parseInt(no));
		}else {
			no = CookieUtils.getCookie(request, "page");
			if (StringUtils.isNumeric(no)){
				this.setPage(Integer.parseInt(no));
			}
		}
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pagesize");
		if (StringUtils.isNumeric(size)){
			CookieUtils.setCookie(response, "pagesize", size);
			this.setPagesize(Integer.parseInt(size));
		}else if (defaultPageSize != -2){
			this.pagesize = defaultPageSize;
		}else {
			no = CookieUtils.getCookie(request, "pagesize");
			if (StringUtils.isNumeric(size)){
				this.setPagesize(Integer.parseInt(size));
			}
		}
		// 设置排序参数
		String orderBy = request.getParameter("sortname");
		if (StringUtils.isNotBlank(orderBy)){
			this.setSortname(sortname);
		}
		String sortorder = request.getParameter("sortorder");
		if (StringUtils.isNotBlank(sortorder)){
			this.setSortorder(sortorder);
		}
	}

	public FlexPage(int page, int pagesize, int total, List<T> rows) {
		this.page = page;
		this.pagesize = pagesize;
		this.total = total;
		this.rows = rows;
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 */
	public FlexPage(int page, int pagesize) {
		this(page, pagesize, 0);
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 */
	public FlexPage(int page, int pagesize, int count) {
		this(page, pagesize, count, new ArrayList<T>());
	}
	
	/**
	 * @return 返回给页面需要用的
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page
	 *            返回给页面需要用的
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return 每页大小
	 */
	public int getPagesize() {
		return pagesize;
	}

	/**
	 * @param pagesize
	 *            每页大小
	 */
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize <= 0 ? 10 : pagesize;
	}

	/**
	 * @return 排序的列名
	 */
	public String getSortname() {
		return sortname;
	}

	/**
	 * @param sortname
	 *            排序的列名
	 */
	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	/**
	 * @return 升序或者降序; desc;asc
	 */
	public String getSortorder() {
		return sortorder;
	}

	/**
	 * @param sortorder
	 *            升序或者降序; desc;asc
	 */
	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	/**
	 * @return 总数多少
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            总数多少
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return 查询出来的数据
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            查询出来的数据
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
		initialize();
	}

	/**
	 * 首页索引
	 * @return
	 */
	/**
	 * 获取 Hibernate FirstResult
	 */
	@JsonIgnore
	public int getFirstResult(){
		int firstResult = (getPage() - 1) * getPagesize();
		if (firstResult >= getTotal()) {
			firstResult = 0;
		}
		return firstResult;
	}
	
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPagesize();
	}
	
	/**
	 * 获取页面总数
	 * @return getLast();
	 */
	@JsonIgnore
	public int getTotalPage() {
		return this.last;
	}
	
	/**
	 * 初始化参数
	 */
	public void initialize(){
				
		this.first = 1;
		
		this.last = (int)(total / (this.pagesize < 1 ? 10 : this.pagesize) + first - 1);
		
		if (this.total % this.pagesize != 0 || this.last == 0) {
			this.last++;
		}

		if (this.last < this.first) {
			this.last = this.first;
		}
		
		if (this.page <= 1) {
			this.page = this.first;
		}

		if (this.page >= this.last) {
			this.page = this.last;
		}

		if (this.page < this.first) {// 如果当前页小于首页
			this.page = this.first;
		}

		if (this.page > this.last) {// 如果当前页大于尾页
			this.page = this.last;
		}
	}
	
}

