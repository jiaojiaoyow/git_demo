package com.neu.t10.dao;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysRoleMenuDaoTest {

	
	@Autowired
	SysRoleMenuDao sysRoleMenuDao;
	
	
	@Test
	public void testInsertOne() {
		int i = sysRoleMenuDao.insertOne(1L,3L);
		System.out.println(i);
	}

	@Test
	public void testInsertList() {
		
		long roleId=11;
    	List<Long> list =new ArrayList<Long>();
    	list.add((long)1);
    	list.add((long)2);
    	list.add((long)3);
    	int i=  sysRoleMenuDao.insertList(roleId,list);
    	System.out.println(i);
	}

	 
	@Test
    public void testDeleteByRoleId(){
        System.out.println(sysRoleMenuDao.deleteByroleId(1L));
    }
	
}
