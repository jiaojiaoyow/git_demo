package com.neu.t10.dao;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neu.t10.util.UserRegisteSalt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.neu.t10.entity.SysUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserDaoTest {

	@Autowired
	SysUserDao Userdao;

	UserRegisteSalt userRegisteSalt=new UserRegisteSalt();

	@Test
	public void testres(){
		String a=userRegisteSalt.encryptPassword("123456","admin"+"8d78869f470951332959580424d4bf4f");
		System.out.println(a);
	}
	 

	 

	@Test
	public void testselectByPar() {
		SysUser sysuser = new SysUser();
		sysuser.setUsername("user02");
		System.out.println(Userdao.selectByPar(sysuser));
	}

	@Test
	public void testInsert() {
		 SysUser u=new SysUser();
	       u.setUsername("li34");
	       u.setPassword("1111");
	       
	       System.out.println( Userdao.insert(u));  //返回受影响行数
	       System.out.println( u.getUserId());   //返回主键
	}

	@Test
	public void testUpdate() {
		SysUser obj=new SysUser();
		obj.setUserId(4L);
		obj.setUsername("test123456");
		obj.setPassword("123456");
		obj.setSalt("salt");
		obj.setStatus(0);
		obj.setDeptId(1L);
		obj.setDeptName("test");
		System.out.println(Userdao.update(obj));
	}

	@Test
	public void testDelete() {
		 System.out.println("User delece======"+Userdao.delete(2L));
	}

	@Test
	public void testSelectAllwithDeptName() {
		List<SysUser> userList=Userdao.selectAllwithDeptName();
		for(int i=0;i<userList.size();i++){
			System.out.println(userList.get(i).getDeptName());
	
		}
	}

	@Test
	public void testSelectAllwithAll() {
		 Map<String,Object> m=new HashMap<>();
	        //m.put("username","user01");
	        //m.put("userId","3");
	        System.out.println(Userdao.selectAllwithAll(m));
	}
	

    @Test
    public void sysUserDaoSelectById()
    {
        Long i=3l;
        SysUser user=Userdao.selectById(i);
        System.out.print(user.getUsername());
    }
	
    @Test
    public void testSysUserDaoSelectAll() {
    	System.out.println(Userdao.selectAll().get(0).getUsername());
    }

    @Test
	public void testSysUserDaoSerlectByName(){
		System.out.println(Userdao.selectByName("admin"));
	}

	@Test
	public void testSysUserDaoSelectUserRole(){
		System.out.println(Userdao.selectUserRole(1L));
	}

}
