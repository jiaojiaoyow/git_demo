package com.neu.t10.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserRoleDaoTest {

	@Autowired
	SysUserRoleDao sysUserRoleDao;
	@Test
	public void testInsertOne() {
	   int a = sysUserRoleDao.insertOne(1,1);
	   if (a>0){
	      System.out.println("插入成功（一个 ，用户角色关系）");
	   }else {
	      System.out.println("插入失败（一个 ，用户角色关系）");
	   }
	}
	@Test
	public void testInsertManyList() {
	   Map<String,Object> map = new HashMap<>();
	   Integer userId =  1 ;
	   List<Integer> roleIdList = new ArrayList<>();
	   roleIdList.add(1);
	   roleIdList.add(2);

	   map.put("userId",userId);
	   map.put("roleIdList",roleIdList);

	   int a = sysUserRoleDao.insertManyList(map);
	   if (a>0){
	      System.out.println("插入成功（一个 用户的多个角色）");
	   }else {
	      System.out.println("插入失败（一个 用户的多个角色）");
	   }
	}
	@Test
	public void testDeleteByUserId() {
	   int a = sysUserRoleDao.deleteByUserId(1);
	   if (a>0){
	      System.out.println("删除成功（删除用户ID所对应的所有关联关系）");
	   }else {
	      System.out.println("删除失败（删除用户ID所对应的所有关联关系）");
	   }
	}


}
