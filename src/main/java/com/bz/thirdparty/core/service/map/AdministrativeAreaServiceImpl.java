package com.bz.thirdparty.core.service.map;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.bz.dao.mapper.map.RegionMapper;
import com.bz.dao.pojo.map.Region;
import com.bz.framework.error.exception.ExternalException;
import com.bz.open.core.service.map.AdministrativeAreaService;
@Service(version="1.0.0",interfaceClass=com.bz.open.core.service.map.AdministrativeAreaService.class)
public class AdministrativeAreaServiceImpl implements AdministrativeAreaService{
   private final Logger logger=LoggerFactory.getLogger(AdministrativeAreaServiceImpl.class);
	@Autowired
    RegionMapper regionMapper;
	@Value("${sms.apikey}")
	private String apikey;
	@Override
	public List<Region> getProvinceLeveRegion() throws ExternalException {
		logger.info("获取一级区域服务开始"+apikey);
		Region region=new Region();
		region.setLevel(1);
		return regionMapper.selectList(region);
	}

}
