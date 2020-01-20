package com.neu.t10.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import com.neu.t10.entity.SysMenu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.neu.t10.entity.SysRole;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysRoleDaoTest {

	@Autowired
	SysRoleDao sysRoleDao;
	
	@Test
	public void testSelectById() {
		System.out.println(sysRoleDao.selectById(1L));
	}
	
	
	@Test
	public void testselectByPar(){

		SysRole sysRole=new SysRole();
		sysRole.setRoleName("角色");
		List<SysRole> list=sysRoleDao.selectByPar(sysRole);
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i).getRoleName());
		}

	}
	
	
	

	
	

	@Test
	public void testSelectAll() {
//		System.out.println(sysRoleDao.selectAll());
		List<SysRole> sysRoleList = sysRoleDao.selectAll();
		for(SysRole sysRole : sysRoleList) {
			System.out.println(sysRole.getRoleName());
		}
	}

	 
	@Test
	public void testInsert() {
		SysRole role1 = new SysRole();
		role1.setRoleName("Role1");
		role1.setRemark("hello");
		role1.setCreateBy(1L);
		role1.setCreateDate(new Date());
		System.out.println(sysRoleDao.insert(role1));
		
		
	}

	@Test
	public void testUpdate() {
		SysRole sysRole=new SysRole();
		sysRole.setRoleId(1L);
		sysRole.setRoleName("测试");
		sysRole.setCreateBy(1L);
		sysRole.setCreateDate(new Date());
		sysRole.setRemark("测试");
		sysRoleDao.update(sysRole);
		System.out.println(sysRoleDao.update(sysRole)+" date the test OK");
	}

	@Test
	public void testDelete() {
		 System.out.println("role delece======"+sysRoleDao.delete(2L));
	}


	@Test
	public void testSysRoleDaoSelectRoleMenu(){
		List<SysMenu> sysRoles=sysRoleDao.selectRoleMenu(1L);
		for(SysMenu menu :sysRoles){
			if(menu.getPerms()!=null){
				String[] strs=menu.getPerms().split(",");
				for(String str:strs){
					System.out.println(str);
				}
			}

		}
	}

}
