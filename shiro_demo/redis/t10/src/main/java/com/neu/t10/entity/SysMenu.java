package com.neu.t10.entity;

public class SysMenu {
	
	private Long menuId;
	private Long parentId;
	private String name;
	private String url;
	private String perms;
	private Integer type;
	private String icon;
	private String orderNum;
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPerms() {
		return perms;
	}
	public void setPerms(String perms) {
		this.perms = perms;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Override
	public String toString() {
		return "SysMenu [menuId=" + menuId + ", parentId=" + parentId + ", name=" + name + ", url=" + url + ", perms="
				+ perms + ", type=" + type + ", icon=" + icon + ", orderNum=" + orderNum + "]";
	}
	

}
