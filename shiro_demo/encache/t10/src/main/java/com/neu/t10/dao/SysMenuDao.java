package com.neu.t10.dao;

import java.util.List;

import com.neu.t10.entity.SysMenu;

public interface SysMenuDao {
	
	    
	    
		//对象条件
		public Long insert(SysMenu obj);
		
		public List<SysMenu> selectAll();
		
		//根据父菜单查询子菜单
		public List<SysMenu> selectByPid(Long pid);
		
		public SysMenu selectById(Long id);
				

		public Integer update(SysMenu obj);
		
		
		public Integer delete(Long id);
		
		
	

}
