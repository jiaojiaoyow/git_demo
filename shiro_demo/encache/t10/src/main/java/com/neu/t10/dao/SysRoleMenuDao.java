package com.neu.t10.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 角色 、菜单 关联 
 * @author admin
 *
 */
public interface SysRoleMenuDao {

	/**
	 * 角色  菜单 添加 ，关联关系 ,一个
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public Integer insertOne(Long roleId,Long menuId); 
	
	
	/**
	 * 批量添加
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public Integer insertList(@Param("roleId")Long roleId,@Param("menuIds")List<Long> menuIds);
	
	
	/**
	 * 删除角色ID所对应的所有关联关系
	 * @param roleId
	 * @return
	 */
	public Integer deleteByroleId(Long roleId);
	
	
	
	
	
	
	
	
}
