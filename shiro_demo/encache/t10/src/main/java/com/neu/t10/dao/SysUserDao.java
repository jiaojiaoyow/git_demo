 
package com.neu.t10.dao;

import java.util.List;
import java.util.Map;

import com.neu.t10.entity.SysRole;
import com.neu.t10.entity.SysUser;

/**
 * 系统用户
 * 实现dao
 * 注解：
 * xml：配置，建议
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:34:11
 */
public interface SysUserDao  {
	
	
	public SysUser selectById(Long id);

	public List<SysUser> selectAll();
	
	//多个条件 @Param("name") ,多 用 map
	public List<SysUser> selectByPar(SysUser user);
    
	//对象条件
	public Long insert(SysUser obj);
	
	

	public Integer update(SysUser obj);

	public Integer delete(Long id);
	
	
	
	//带部门名称
	
	
	public List<SysUser> selectAllwithDeptName();
	
	
	
	//查询，带权限，菜单
	List<Map<String,Object>> selectAllwithAll(Map m);


	//查询用户的角色
	List<SysRole> selectUserRole(Long userId);

	//根据用户名查找用户
	SysUser selectByName(String username);
	
	
	
	
	 

}
