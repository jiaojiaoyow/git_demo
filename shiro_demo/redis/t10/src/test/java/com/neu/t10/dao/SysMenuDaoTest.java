package com.neu.t10.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.neu.t10.entity.SysMenu;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SysMenuDaoTest {

	@Autowired
	SysMenuDao sysMenuDao;


	


	@Test
    public void testSysMenuDaoinsert(){
        SysMenu sm=new SysMenu();
       
        sm.setParentId((long)3);
        sm.setName("测试okbuok2");
        sm.setOrderNum("0");
        sm.setPerms("sys:user:delete");
        sm.setType(2);

        System.out.println(sysMenuDao.insert(sm));
    }
	
	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectAll() {
		List<SysMenu> list=sysMenuDao.selectAll();
		for(SysMenu x: list){
			System.out.println(x.toString());
		}
	}

	@Test
	public void testSelectByPid() {
		List<SysMenu> sysMenus = sysMenuDao.selectByPid(1L);
        for (SysMenu sysMenu : sysMenus){
            System.out.println(sysMenu);
        }
	}

	@Test
	public void testSelectById() {
		System.out.println("菜单名称为："+sysMenuDao.selectById(1L).getName());
	}

	@Test
	public void testUpdate() {
		SysMenu menu=new SysMenu();
		menu.setMenuId(1L);
		menu.setUrl("111111");
		sysMenuDao.update(menu);
	}

	@Test
	public void testDelete() {
		
		System.out.println(sysMenuDao.delete(10L));
	}

}
