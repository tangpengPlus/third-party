package com.bz.thirdparty.map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bz.open.core.service.map.AdministrativeAreaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdministrativeAreaTest {
	private final Logger logger=LoggerFactory.getLogger(AdministrativeAreaTest.class);
	
@Autowired
private AdministrativeAreaService administrativeAreaService;
	
	@Test
	public void testGetProvinceLeveRegion() {
		logger.info("获取到所有一级城市信息是:"+administrativeAreaService.getProvinceLeveRegion());
		
		
	}
}
