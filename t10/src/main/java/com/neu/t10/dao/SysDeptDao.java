package com.neu.t10.dao;

import java.util.List;

import com.neu.t10.entity.SysDept;

// XML方式实现 
public interface SysDeptDao {
	
	//根据条件查询部们
	public List<SysDept> selectByPar(SysDept obj);
    
	//插入
	public Long insert(SysDept obj);
	
	//查询全部
	public List<SysDept> selectAll();
	
	//查询
	public SysDept selectById(Long id);
	
	//更新
	public Integer update(SysDept obj);
	
	//删除
	public Integer delete(Long id);

}
