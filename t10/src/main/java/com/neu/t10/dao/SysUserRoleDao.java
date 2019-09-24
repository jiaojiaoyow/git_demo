 
package com.neu.t10.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 用户与角色对应关系
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:34:46
 */
public interface SysUserRoleDao  {
	
	
	/**
	    * 一个 ，用户角色关系
	    * @param userId
	    * @param RoleId
	    * @return
	    */
	   public Integer insertOne(@Param("userId") Integer userId,@Param("roleId") Integer RoleId);

	   /**
	    *  一个 用户的多个角色
	    * @param map
	     * @return
	     */
	   public Integer insertManyList(@Param("map")Map<String,Object> map);

	   /**
	    * 删除用户ID所对应的所有关联关系
	    * @param userId
	    * @return
	    */
	   public Integer deleteByUserId(@Param("userId") Integer userId);

	
	
	
}
