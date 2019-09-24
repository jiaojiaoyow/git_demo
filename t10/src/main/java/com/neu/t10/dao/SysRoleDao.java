 
package com.neu.t10.dao;

import java.util.List;

import com.neu.t10.entity.SysMenu;
import com.neu.t10.entity.SysRole;

/**
 * 角色管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:33:33
 */
public interface SysRoleDao  {

	public SysRole selectById(Long id);

	public List<SysRole> selectAll();
	
	//多个条件 @Param("name") ,多 用 map
	public List<SysRole> selectByPar(SysRole  obj);
    
	//对象条件
	public Long insert(SysRole obj);

	
	public Integer update(SysRole obj);

	//删除角色
	public Integer delete(Long id);

	//查询角色拥有的菜单
	List<SysMenu> selectRoleMenu(Long roleId);
}
