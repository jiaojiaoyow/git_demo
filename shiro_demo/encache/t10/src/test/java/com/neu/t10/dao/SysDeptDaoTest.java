package com.neu.t10.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.neu.t10.entity.SysDept;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SysDeptDaoTest {
	
	@Autowired
	SysDeptDao sysDeptDao;
	

	@Test
	public void testSelectByPar() {
		SysDept sys = new SysDept();
		sys.setParentId(1L);
		//sys.setDeptId(1L);
		//sys.setOrderNum(1);
		List<SysDept> sysDeptList = sysDeptDao.selectByPar(sys);	
		 for(SysDept sysDept :sysDeptList){
	            System.out.println(sysDept.getName());
	        }
	}

	@Test
	public void testInsert() {
		 SysDept sysDept=new SysDept();
	        sysDept.setName("测试");
	        sysDept.setParentId(4L);
	        sysDept.setOrderNum(1);
	        sysDept.setDelFlag(1);
	        Long line=sysDeptDao.insert(sysDept);
	        System.out.println("id = "+sysDept.getDeptId()+"\n"+"line = "+line);
	}

	@Test
	public void testSelectAll() {
		 List<SysDept> sysDeptList=sysDeptDao.selectAll();

	        for(SysDept sysDept :sysDeptList){
	            System.out.println(sysDept.getName());
	        }
	}

	@Test
	public void testSelectById() {
		SysDept sd = sysDeptDao.selectById(2L);
		System.out.println(sd.getName());
	}

	
	@Test
	public void testUpdate() {
		SysDept sysDept = new SysDept();
		sysDept.setDelFlag(12);
		sysDept.setDeptId(new Long(5));
		sysDept.setOrderNum(22);
		sysDept.setName("'Lawrence'");
		sysDept.setParentId(new Long(33));
		System.out.println(sysDeptDao.update(sysDept));
	}

	@Test
	public void testDelete() {
		System.out.println(sysDeptDao.delete(1L));
	}

}
