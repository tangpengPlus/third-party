package com.bz.map;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.bz.dao.pojo.merchant.MerchantShop;
import com.bz.open.core.service.map.MapService;
import com.bz.thirdparty.BzThirdPartyApplication;
import com.bz.thirdparty.core.service.map.MapServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest
public class BzMapTest {

	@Autowired
	private MapServiceImpl dao;
	
	@Test
	public void testaddMerchantInfoToMap() {
		MerchantShop merchantShop = new MerchantShop();
		merchantShop.setId(1);
		merchantShop.setName("测试商家");
		merchantShop.setNumber("02234");
		merchantShop.setDetailaddress("富力海洋广场2号楼(北门)");
		merchantShop.setCoordinate("106.536324,29.582747");
		merchantShop.setPhone("12345678");
		merchantShop.setId(3);
		merchantShop.setShopfacadeimage("http://img1.imgtn.bdimg.com/it/u=2899492451,3422113638&fm=27&gp=0.jpg");
		merchantShop.setLicenseimage("http://img1.imgtn.bdimg.com/it/u=2899492451,3422113638&fm=27&gp=0.jpg");
		//增加测试
		//dao.addMerchantInfoToMap(merchantShop);
		//删除测试
		//dao.deleteMerchantInfoToMap(1);
		//修改测试
		//dao.updateMerchantInfoToMap(merchantShop);
		//测试周边select
//		List<MerchantShop> list = dao.selectLocalityMerchantInfoToMap("测试商家","重庆");
//		System.out.println(list);
		//周边地图检索
//		List<MerchantShop> list = dao.selectPeripheryMerchantInfoToMap("106.536324,29.582747");
//		System.out.println(list);
		//多边形测试
	//	List<MerchantShop> list = dao.selectPolygonMerchantInfoToMap("106.536324,29.582747;106.536324,29.583747;106.536324,29.582947");
		//ID检索
//		MerchantShop merchantShop2 = dao.selectByIdMerchantInfoToMap(2);
//		System.out.println(merchantShop2);
		//条件检索
//		List<MerchantShop> list= dao.selectByIdMerchantInfoToMap("山");
//		System.out.println(list);
		
	}
}
