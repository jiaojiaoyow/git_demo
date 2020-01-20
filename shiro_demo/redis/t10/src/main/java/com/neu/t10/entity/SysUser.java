
package com.neu.t10.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户表  sys_user
 * 
 * sys_user : SysUser
 * 
 * 类型要用复杂类型 long  0 Long null
 * 
 * @author admin
 *
 */
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long userId;  

	private String username;  

	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 状态 0：禁用 1：正常
	 */
	private Integer status;

	private Long deptId;
	
	//辅助
	private String deptName;

	/**
	 * 密码盐.
	 * @return
	 */
	public String getCredentialsSalt(){
		return this.username+this.salt;
	}
	//重新对盐重新进行了定义，用户名+salt，这样就更加不容易被破解


	
	

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	// 创建者，创建时间
	private Long createBy;

	private Date createDate;

	
	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
