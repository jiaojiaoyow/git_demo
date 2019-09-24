 
package com.neu.t10.entity;


import java.io.Serializable;
import java.util.Date;

 
 /**
  * 角色
  * @author admin
  *
  */
public class SysRole implements Serializable {
	private static final long serialVersionUID = 1L;
	
	 
	private Long roleId;

	 
	private String roleName;

	/**
	 * 备注
	 */
	private String remark;

	 
	// 创建者，创建时间
			private Long createBy;
			 
			private Date createDate;
	 
	
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
