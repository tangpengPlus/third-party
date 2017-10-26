package com.bz.thirdparty.core.service.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bz.dao.pojo.merchant.MerchantShop;
import com.bz.framework.error.exception.ExternalException;
import com.bz.framework.util.http.HttpClientHelper;
import com.bz.open.core.service.map.MapService;
import com.bz.thirdparty.core.model.map.MapData;
import com.bz.framework.constant.exception.BzExceptionEnum.ExternalExceptionEnum;
/**
 * 
 * 
 * 作者: 彭云山
 * 描述:地图服务实现
 * 创建时间:2017年10月18日下午4:29:54
 * 修改备注:
 */
@Service(version="1.0.0",interfaceClass=com.bz.open.core.service.map.MapService.class)
public class MapServiceImpl implements MapService{
	
	@Value("${map.key}")
	private String mapKey;//高德地图key
	
	@Value("${map.tableid}")
	private String tableId;//高德地图云图数据Id
	
	@Value("${map.addMerchantInfoUrl}")
	private String addMerchantInfoUrl;//增加商户地图定点Url
	
	@Value("${map.deleteMerchantInfoUrl}")
	private String deleteMerchantInfoUrl;//删除商户id的Url
	
	@Value("${map.updateMerchantInfoUrl}")
	private String updateMerchantInfoUrl;//修改商家地图定点的Url
	
	@Value("${map.selectLocalityMerchantInfoUrl}")
	private String selectLocalityMerchantInfoUrl;//本地检索请求地址
	
	@Value("${map.selectPeripheryMerchantInfoUrl}")
	private String selectPeripheryMerchantInfoUrl;//周边检索请求
	
	@Value("${map.selectByIdMerchantInfoUrl}")
	private String selectByIdMerchantInfoUrl;//id检索请求地址
	
	@Value("${map.slectByConditionMerchantInfoUrl}")
	private String slectByConditionMerchantInfoUrl;//条件搜索请求地址
	
	
	private HttpClientHelper clientHelper;//htttp请求
	
	private final Logger logger=LoggerFactory.getLogger(MapServiceImpl.class);

	public MapServiceImpl() {
		clientHelper=new HttpClientHelper();
    }
	
	
	/**
	 * 描述增加定点
	 * 创建时间：2017年10月20日17:05:02
	 * @author 彭云山
	 * @param merchantShop 增加的商户对象
	 * @throws ExternalException {@link ExternalException} 第三方服务异常封装
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean addMerchantInfoToMap(MerchantShop merchantShop) throws ExternalException {
		logger.info("增加商户云图定位:【"+merchantShop+"】");
		Assert.notNull(merchantShop);
		if(StringUtils.isEmpty(merchantShop.getNumber())) {
			logger.error("增加商户云图定位失败。原因对象【MerchantShop】中属性【number:商家编号为空】");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "增加商户云图定位失败。原因对象【MerchantShop】中属性【number:商家编号为空】");
		}
		if(StringUtils.isEmpty(merchantShop.getName())) {
			logger.error("增加商户云图定位失败。原因对象【MerchantShop】中属性【name:商家名称为空】");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "增加商户云图定位失败。原因对象【MerchantBase】中属性【name:商家名称为空】");
		}
        if(StringUtils.isEmpty(merchantShop.getDetailaddress())) {
			logger.error("增加商户云图定位失败。原因对象【MerchantShop】中属性【detailaddress:商家详细地址为空】");
		    throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "增加商户云图定位失败。原因对象【MerchantShop】中属性【detailaddress:商家详细地址为空】");
		}
        if(StringUtils.isEmpty(merchantShop.getCoordinate())) {
        	logger.error("增加商户云图定位失败。原因对象【MerchantShop】中属性【coordinate:店铺经纬度为空】");
		    throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "增加商户云图定位失败。原因对象【MerchantShop】中属性【coordinate:店铺经纬度为空】");
		}
        if(StringUtils.isEmpty(merchantShop.getPhone())) {
        	logger.error("增加商户云图定位失败。原因对象【MerchantShop】中属性【phone:店铺电话为空】");
		    throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "增加商户云图定位失败。原因对象【MerchantShop】中属性【phone:店铺电话为空】");
		}
        if(StringUtils.isEmpty(merchantShop.getShopfacadeimage())) {
        	logger.error("增加商户云图定位失败。原因对象【MerchantShop】中属性【shopfacadeimage:店铺门面图片为空】");
		    throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "增加商户云图定位失败。原因对象【MerchantShop】中属性【shopfacadeimage:店铺门面图片为空】");
		}
		//向云图增加商户定点
        Map<String,String> mapData=new HashMap<String,String>();
        mapData.put("key",mapKey);
        mapData.put("tableid",this.tableId);
        mapData.put("loctype", "1");
        //构造高德地图josn数据
        MapData data =new MapData();
        data.setAddress(merchantShop.getDetailaddress());
        data.setCoordtype("1");
        data.setLocation(merchantShop.getCoordinate());
       
        data.setMerchantImage(merchantShop.getLicenseimage());
        data.setMerchantName(merchantShop.getName());
        data.setName(merchantShop.getName());
        data.setMerchantDiscript("描述：106.536324,29.582747");
        
        mapData.put("data",JSONObject.toJSON(data).toString());
        
        clientHelper.addParameter(mapData);
        
        try {
			JSONObject json=(JSONObject) JSONObject.parse(clientHelper.doPost(addMerchantInfoUrl));
		} catch (Exception e) {
			logger.error("增加商户云图定位失败。向高德地图写入定点数据失败");
		    throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "增加商户云图定位失败。向高德地图写入定点数据失败");
		}
        return true;
	}
	
	/**
	 * 描述：根据指定的ID删除商家
	 * 创建时间：2017年10月20日17:05:02
	 * 作者：彭云山
	 * @param id 删除商家的ID
	 * @throws ExternalException {@link ExternalException} 第三方服务异常封装
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean deleteMerchantInfoToMap(Integer id) throws ExternalException {
		logger.info("删除云图定位编号为：【"+id+"】的商户");
		Assert.notNull(id);
		if(StringUtils.isEmpty(id.toString())) {
        	logger.error("删除商户云图定位的id失败。原因对象【MerchantShop】中属性【id:店铺门id为空】");
		    throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "删除商户云图定位的id失败。原因对象【MerchantShop】中属性【id:店铺门id为空】");
		}
		//向云图删除商户定点id
        Map<String,String> mapData=new HashMap<String,String>();
        mapData.put("key", mapKey);
        mapData.put("tableid",this.tableId);
        mapData.put("ids", id.toString());
        clientHelper.addParameter(mapData);
        try {
			clientHelper.doPost(deleteMerchantInfoUrl);
		} catch (Exception e) {
			logger.error("删除云图定位编号为：【"+id+"】的商户出现错误",e);
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR,"删除云图定位编号为：【"+id+"】的商户出现错误");
		}
		return true;
	}
 
	/**
	 * 描述：修改商家的定点
	 * 创建时间：2017年10月20日17:05:02
	 * 作者：彭云山
	 * @param merchantShop 修改后的商家
	 * @throws ExternalException {@link ExternalException} 第三方服务异常封装
	 */
	@Override
	public boolean updateMerchantInfoToMap(MerchantShop merchantShop) throws ExternalException {
		logger.info("修改商户云图定位【"+merchantShop+"】");
		Assert.notNull(merchantShop);
		if(StringUtils.isEmpty(merchantShop.getNumber())) {
			logger.error("修改云图定位失败，失败原因：对象【merchantShop】中属性【number:商家编号】为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR,"修改云图定位失败，失败原因：对象【merchantShop】中属性【number:商家编号】为空" );
		}
		if(StringUtils.isEmpty(merchantShop.getName())) {
			logger.error("修改云图定位失败，失败原因：对象【merchantShop】中属性【name:名字】为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR,"修改云图定位失败，失败原因：对象【merchantShop】中属性【name:名字】为空");
		}
		if(StringUtils.isEmpty(merchantShop.getDetailaddress())) {
			logger.error("修改云图定位失败，失败原因：对象【merchantShop】中属性【detailaddress:商家地址】为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "修改云图定位失败，失败原因：对象【merchantShop】中属性【detailaddress:商家地址】为空");
			
		}
		if(StringUtils.isEmpty(merchantShop.getCoordinate())) {
			logger.error("修改云图定位失败，失败原因：对象【merchantShop】中属性【Coordinate:商家地址经纬】为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "修改云图定位失败，失败原因：对象【merchantShop】中属性【Coordinate:商家地址经纬】为空");
		}
		if(StringUtils.isEmpty(merchantShop.getPhone())) {
			logger.error("修改云图定位失败，失败原因：对象【merchantShop】中属性【phone:商家联系电话】为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR,"修改云图定位失败，失败原因：对象【merchantShop】中属性【phone:商家联系电话】为空");
		}
		if(StringUtils.isEmpty(merchantShop.getShopfacadeimage())) {
			logger.error("修改云图定位失败，失败原因：对象【merchantShop】中属性【Shopfacadeimage:商家店铺门面照片】为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "修改云图定位失败，失败原因：对象【merchantShop】中属性【Shopfacadeimage:商家店铺门面照片】为空");
		}
		//向云图商家定点
		Map<String,String> mapData = new HashMap<String,String>();
		mapData.put("key", mapKey);
		mapData.put("tableid", this.tableId);
		mapData.put("loctype", "1");//[1]：经纬度；格式示例：104.394729,31.125698  [2]：地址
		//构造高德地josn数据
		MapData data = new  MapData();
		data.setId(merchantShop.getId().toString());
		data.setName(merchantShop.getName());
		data.setLocation(merchantShop.getCoordinate());
		data.setCoordtype("1"); //1: gps 2: autonavi 3: baidu
		data.setAddress(merchantShop.getDetailaddress());
		//云图自定义字段
		data.setMerchantImage(merchantShop.getLicenseimage());
	    data.setMerchantName(merchantShop.getName());
	    data.setName(merchantShop.getName());
	    data.setMerchantDiscript("宝众商家定点");
	    mapData.put("data",JSONObject.toJSON(data).toString());
	    clientHelper.addParameter(mapData);
	    try {
			clientHelper.doPost(updateMerchantInfoUrl);
		} catch (Exception e) {
			logger.error("修改商户云图定位失败。向高德地图写入定点数据失败");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "修改商户云图定位失败。向高德地图写入定点数据失败");
		}
		return true;
	}


	
	/**
	 * 描述：根据用户输入的关键字和城市检索商家信息，当city为全国或输入错误是，会检索全国的商家
	 * 创建时间：2017年10月20日17:05:02
	 * 作者：彭云山
	 * @param keywords 商家关键字
	 * @param city 城市名称
	 * @throws ExternalException {@link ExternalException} 第三方服务异常封装
	 */
	@Override
	public List<MerchantShop> selectLocalityMerchantInfoToMap(String keywords,String city) throws ExternalException {
		List<MapData> list = new ArrayList<>();
		List<MerchantShop> merchantShops = new ArrayList<>();
		logger.info("本地检索请求【"+keywords+"】");
		Map<String,String> mapData=new HashMap<String,String>();
		mapData.put("key", mapKey);
		mapData.put("tableid",this.tableId);
		mapData.put("keywords",keywords);
		mapData.put("city", city);
		clientHelper.addParameter(mapData);
		logger.info(mapData.toString());
		try {
			logger.info(clientHelper.doPost(selectLocalityMerchantInfoUrl).toString());
			Object jsonObject = JSONObject.parse(clientHelper.doPost(selectLocalityMerchantInfoUrl));
			logger.info(jsonObject.toString());
			JSONObject json=JSON.parseObject(jsonObject.toString());
			
			JSONArray jsons=JSONArray.parseArray(json.get("datas").toString());
//			JSONArray info=JSONArray.parseArray(json.get("info").toString());
//			logger.info(jsons.toString());
//			if(!(info.equals("OK"))) {
//				logger.error("云图本地检索失败,地图传回的参数错误");
//				throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图本地检索失败,地图传回的参数错误");
//			}
			Iterator iterator = jsons.iterator();
			while(iterator.hasNext()) {
				 MapData mapdata = new MapData();
				 MerchantShop merchantShop = new MerchantShop();
				 JSONObject sObj = (JSONObject)iterator.next();
				 //获取地图得到的信息，存入MapDate实体内
				 mapdata.setId((String) sObj.get("_id"));
 				 mapdata.setName((String) sObj.get("_name"));
				 mapdata.setLocation((String) sObj.get("_location"));
				 mapdata.setCoordtype((String) sObj.get("coordtype"));//坐标类型 可选值：1: gps 2: autonavi 3: baidu
				 mapdata.setAddress((String) sObj.get("_address"));
				 mapdata.setMerchantName((String) sObj.get("merchantName"));//店铺名称
				 mapdata.setMerchantType((String) sObj.get("merchantType"));
				 mapdata.setMerchantImage((String) sObj.get("merchantImage"));
				 mapdata.setMerchantDiscript((String) sObj.get("merchantDiscript"));
				 mapdata.set_province((String) sObj.get("_province"));
				 mapdata.set_city((String) sObj.get("_city"));
				 mapdata.set_district((String) sObj.get("_district"));
				 //将地图得到的信息转图merchantShop实体内
				 merchantShop.setId(Integer.parseInt(mapdata.getId()));//商家的ID
				 merchantShop.setName(mapdata.getName());//店铺的名字
				 merchantShop.setCoordinate(mapdata.getLocation());//商家的经纬度
				 merchantShop.setCitylevecode(mapdata.get_province());//省级编码
				 merchantShop.setCitylevecode(mapdata.get_city());//市级编码
				 merchantShop.setCitylevecode(mapdata.get_district());//区级编码
				 merchantShop.setDetailaddress(mapdata.getAddress());//详细地址
				 merchantShop.setShopfacadeimage(mapdata.getMerchantImage());//店铺门面图片
				 merchantShops.add(merchantShop);
				 list.add(mapdata);
			 }
		} catch (Exception e) {
			logger.error("云图本地检索失败，向地图传入检索条件失败");
		    throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图本地检索失败，向地图传入检索条件失败");
		}
		return merchantShops;
	}
	/**
	 * 描述：根据用户提供的中心点左边，进行周边商家检索
	 * 创建时间：2017年10月20日17:05:02
	 * 作者：彭云山
	 * @param center 用户中心点坐标
	 * @throws ExternalException {@link ExternalException} 第三方服务异常封装
	 */
	@Override
	public List<MerchantShop> selectPeripheryMerchantInfoToMap(String center) throws ExternalException {
		logger.info("发起【"+center+"】周边检索请求");
		List<MapData> list = new ArrayList<>();
		List<MerchantShop> merchantShops = new ArrayList<>();
		Assert.notNull(center);
		if(StringUtils.isEmpty(center)) {
			logger.error("定位周围云图定位失败。【center:输入的中心点为空】");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "定位周围云图定位失败。【center:输入的中心点为空】");
		}
		String[] split = center.split(",");
		
		if(center.split(",").length != 2 && split[0] != "" && split[0] != null && split[1] != "" && split[1] != null) {
			logger.error("云图周边检索失败。向高德地图写入中心定数据失败");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图周边检索失败。向高德地图写入中心定数据失败");
		}
		Map<String,String> mapData=new HashMap<String,String>();
		mapData.put("key", mapKey);
		mapData.put("tableid",this.tableId);
		mapData.put("city", "全国");
		mapData.put("center", center);
		clientHelper.addParameter(mapData);
		try {
			Object jsonObject = JSONObject.parse(clientHelper.doPost(selectPeripheryMerchantInfoUrl));
			JSONObject json=JSON.parseObject(jsonObject.toString());
			JSONArray jsons=JSONArray.parseArray(json.get("datas").toString());
			Iterator iterator = jsons.iterator();
			while(iterator.hasNext()) {
				 MapData mapdata = new MapData();
				 MerchantShop merchantShop = new MerchantShop();
				 JSONObject sObj = (JSONObject)iterator.next();
				 //获取地图得到的信息，存入MapDate实体内
				 mapdata.setId((String) sObj.get("_id"));
 				 mapdata.setName((String) sObj.get("_name"));
				 mapdata.setLocation((String) sObj.get("_location"));
				 mapdata.setCoordtype((String) sObj.get("coordtype"));//坐标类型 可选值：1: gps 2: autonavi 3: baidu
				 mapdata.setAddress((String) sObj.get("_address"));
				 mapdata.setMerchantName((String) sObj.get("merchantName"));//店铺名称
				 mapdata.setMerchantType((String) sObj.get("merchantType"));
				 mapdata.setMerchantImage((String) sObj.get("merchantImage"));
				 mapdata.setMerchantDiscript((String) sObj.get("merchantDiscript"));
				 mapdata.set_province((String) sObj.get("_province"));
				 mapdata.set_city((String) sObj.get("_city"));
				 mapdata.set_district((String) sObj.get("_district"));
				 //将地图得到的信息转图merchantShop实体内
				 merchantShop.setId(Integer.parseInt(mapdata.getId()));//商家的ID
				 merchantShop.setName(mapdata.getName());//店铺的名字
				 merchantShop.setCoordinate(mapdata.getLocation());//商家的经纬度
				 merchantShop.setCitylevecode(mapdata.get_province());//省级编码
				 merchantShop.setCitylevecode(mapdata.get_city());//市级编码
				 merchantShop.setCitylevecode(mapdata.get_district());//区级编码
				 merchantShop.setDetailaddress(mapdata.getAddress());//详细地址
				 merchantShop.setShopfacadeimage(mapdata.getMerchantImage());//店铺门面图片
				 merchantShops.add(merchantShop);
				 list.add(mapdata);
			 }
		} catch (Exception e) {
			logger.error("云图周边检索失败。向高德地图写入中心定数据失败");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图周边检索失败。向高德地图写入中心定数据失败");
		}
		return merchantShops;
	}


	/**
	 * 描述：根据用户提供的经纬坐标点，进行多边形检索
	 * 创建时间：2017年10月20日17:05:02
	 * 作者：彭云山
	 * @param polygon 商家坐标
	 * @throws ExternalException {@link ExternalException} 第三方服务异常封装
	 */
	@Override
	public List<MerchantShop> selectPolygonMerchantInfoToMap(String polygon) throws ExternalException {
		logger.info("发起【"+polygon+"】的多边检索请求");
		List<MapData> list = new ArrayList<>();
		List<MerchantShop> merchantShops = new ArrayList<>();
		Assert.notNull(polygon);
		if(StringUtils.isEmpty(polygon)) {
			logger.error("多边形云图定位失败。【polygon】:输入的多边形的经纬为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "多边形云图定位失败。【polygon:输入的多边形的经纬为空】");
		}
		if((polygon.split(";").length)<=1 ) {
			logger.info("云图多边检索失败,输入的经纬度格式错误");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图多边检索失败,输入的经纬度格式错误");
		}
		if(polygon.replace(";", ",").split(",").length%2 ==1) {
			logger.info("云图多边检索失败,输入的经纬度格式错误");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图多边检索失败,输入的经纬度格式错误");
		}
		Map<String,String> mapData=new HashMap<String,String>();
		mapData.put("key", mapKey);
		mapData.put("tableid",this.tableId);
		mapData.put("city", "全国");
		mapData.put("polygon", polygon);
		clientHelper.addParameter(mapData);
		try {
			Object jsonObject = JSONObject.parse(clientHelper.doPost(selectPeripheryMerchantInfoUrl));
			JSONObject json=JSON.parseObject(jsonObject.toString());
			JSONArray jsons=JSONArray.parseArray(json.get("datas").toString());
			Iterator iterator = jsons.iterator();
			while(iterator.hasNext()) {
				 MapData mapdata = new MapData();
				 MerchantShop merchantShop = new MerchantShop();
				 JSONObject sObj = (JSONObject)iterator.next();
				 //获取地图得到的信息，存入MapDate实体内
				 mapdata.setId((String) sObj.get("_id"));
 				 mapdata.setName((String) sObj.get("_name"));
				 mapdata.setLocation((String) sObj.get("_location"));
				 mapdata.setCoordtype((String) sObj.get("coordtype"));//坐标类型 可选值：1: gps 2: autonavi 3: baidu
				 mapdata.setAddress((String) sObj.get("_address"));
				 mapdata.setMerchantName((String) sObj.get("merchantName"));//店铺名称
				 mapdata.setMerchantType((String) sObj.get("merchantType"));
				 mapdata.setMerchantImage((String) sObj.get("merchantImage"));
				 mapdata.setMerchantDiscript((String) sObj.get("merchantDiscript"));
				 mapdata.set_province((String) sObj.get("_province"));
				 mapdata.set_city((String) sObj.get("_city"));
				 mapdata.set_district((String) sObj.get("_district"));
				 //将地图得到的信息转图merchantShop实体内
				 merchantShop.setId(Integer.parseInt(mapdata.getId()));//商家的ID
				 merchantShop.setName(mapdata.getName());//店铺的名字
				 merchantShop.setCoordinate(mapdata.getLocation());//商家的经纬度
				 merchantShop.setCitylevecode(mapdata.get_province());//省级编码
				 merchantShop.setCitylevecode(mapdata.get_city());//市级编码
				 merchantShop.setCitylevecode(mapdata.get_district());//区级编码
				 merchantShop.setDetailaddress(mapdata.getAddress());//详细地址
				 merchantShop.setShopfacadeimage(mapdata.getMerchantImage());//店铺门面图片
				 merchantShops.add(merchantShop);
				 list.add(mapdata);
			}
		} catch (Exception e) {
			logger.info("云图多边形检索失败。向高德地图写入中心定数据失败");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图多边形检索失败。向高德地图写入中心定数据失败");
		}
		return merchantShops;
	}

	/**
	 * 描述：根据用户提供商家ID，进行ID检索
	 * 创建时间：2017年10月20日17:05:02
	 * 作者：彭云山
	 * @param id 商家得ID
	 * @throws ExternalException {@link ExternalException} 第三方服务异常封装
	 */
	@Override
	public MerchantShop selectByIdMerchantInfoToMap(Integer id) throws ExternalException {
		logger.info("发起【id】的检索请求");
		MerchantShop merchantShop = new MerchantShop();
		Assert.notNull(id);
		if(StringUtils.isEmpty(id.toString())) {
			logger.info("云图ID检索失败。数据【ID】为空");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图ID检索失败。数据【ID】为空");
		}
		if(id<1) {
			logger.info("云图ID检索失败。数据【ID】格式不正确");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图ID检索失败。数据【ID】格式不正确");
		}
		Map<String,String> mapData=new HashMap<String,String>();
		mapData.put("key", mapKey);
		mapData.put("tableid",this.tableId);
		mapData.put("_id", id.toString());
		clientHelper.addParameter(mapData);
		try {
			Object jsonObject = JSONObject.parse(clientHelper.doPost(selectByIdMerchantInfoUrl));
			JSONObject json=JSON.parseObject(jsonObject.toString());
			JSONArray jsons=JSONArray.parseArray(json.get("datas").toString());
			
			Iterator iterator = jsons.iterator();
			while(iterator.hasNext()) {
				 MapData mapdata = new MapData();
				 JSONObject sObj = (JSONObject)iterator.next();
				 //获取地图得到的信息，存入MapDate实体内
				 mapdata.setId((String) sObj.get("_id"));
 				 mapdata.setName((String) sObj.get("_name"));
				 mapdata.setLocation((String) sObj.get("_location"));
				 mapdata.setCoordtype((String) sObj.get("coordtype"));//坐标类型 可选值：1: gps 2: autonavi 3: baidu
				 mapdata.setAddress((String) sObj.get("_address"));
				 mapdata.setMerchantName((String) sObj.get("merchantName"));//店铺名称
				 mapdata.setMerchantType((String) sObj.get("merchantType"));
				 mapdata.setMerchantImage((String) sObj.get("merchantImage"));
				 mapdata.setMerchantDiscript((String) sObj.get("merchantDiscript"));
				 mapdata.set_province((String) sObj.get("_province"));
				 mapdata.set_city((String) sObj.get("_city"));
				 mapdata.set_district((String) sObj.get("_district"));
				 //将地图得到的信息转图merchantShop实体内
				 merchantShop.setId(Integer.parseInt(mapdata.getId()));//商家的ID
				 merchantShop.setName(mapdata.getName());//店铺的名字
				 merchantShop.setCoordinate(mapdata.getLocation());//商家的经纬度
				 merchantShop.setCitylevecode(mapdata.get_province());//省级编码
				 merchantShop.setCitylevecode(mapdata.get_city());//市级编码
				 merchantShop.setCitylevecode(mapdata.get_district());//区级编码
				 merchantShop.setDetailaddress(mapdata.getAddress());//详细地址
				 merchantShop.setShopfacadeimage(mapdata.getMerchantImage());//店铺门面图片
			 }
		} catch (Exception e) {
			logger.info("云图ID检索失败。向高德地图写入中心定数据失败");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图ID检索失败。向高德地图写入中心定数据失败");
		}
		return merchantShop;
	}


	/**
	 * 描述：根据用户提供条件，进行条件检索,
	 * 创建时间：2017年10月20日17:05:02
	 * 作者：彭云山
	 * @param filter 用户提供的搜索条件
	 * @throws ExternalException {@link ExternalException} 第三方服务异常封装
	 */
	@Override
	public List<MerchantShop> selectByIdMerchantInfoToMap(String filter) throws ExternalException {
		logger.info("根据【"+filter+"】条件查询云图定位");
		List<MapData> list = new ArrayList<>();
		List<MerchantShop> merchantShops = new ArrayList<>();
		
		Map<String,String> mapData=new HashMap<String,String>();
		mapData.put("key", mapKey);
		mapData.put("tableid",this.tableId);
		mapData.put("filter",filter);
		clientHelper.addParameter(mapData);
		
		try {
			Object jsonObject = JSONObject.parse(clientHelper.doPost(slectByConditionMerchantInfoUrl));
			JSONObject json=JSON.parseObject(jsonObject.toString());
			JSONArray jsons=JSONArray.parseArray(json.get("datas").toString());
			Iterator iterator = jsons.iterator();
			while(iterator.hasNext()) {
				 MapData mapdata = new MapData();
				 MerchantShop merchantShop = new MerchantShop();
				 JSONObject sObj = (JSONObject)iterator.next();
				 //获取地图得到的信息，存入MapDate实体内
				 mapdata.setId((String) sObj.get("_id"));
 				 mapdata.setName((String) sObj.get("_name"));
				 mapdata.setLocation((String) sObj.get("_location"));
				 mapdata.setCoordtype((String) sObj.get("coordtype"));//坐标类型 可选值：1: gps 2: autonavi 3: baidu
				 mapdata.setAddress((String) sObj.get("_address"));
				 mapdata.setMerchantName((String) sObj.get("merchantName"));//店铺名称
				 mapdata.setMerchantType((String) sObj.get("merchantType"));
				 mapdata.setMerchantImage((String) sObj.get("merchantImage"));
				 mapdata.setMerchantDiscript((String) sObj.get("merchantDiscript"));//商家描述
				 mapdata.set_province((String) sObj.get("_province"));
				 mapdata.set_city((String) sObj.get("_city"));
				 mapdata.set_district((String) sObj.get("_district"));
				 //将地图得到的信息转图merchantShop实体内
				 merchantShop.setId(Integer.parseInt(mapdata.getId()));//商家的ID
				 merchantShop.setName(mapdata.getName());//店铺的名字
				 merchantShop.setCoordinate(mapdata.getLocation());//商家的经纬度
				 merchantShop.setCitylevecode(mapdata.get_province());//省级编码
				 merchantShop.setCitylevecode(mapdata.get_city());//市级编码
				 merchantShop.setCitylevecode(mapdata.get_district());//区级编码
				 merchantShop.setDetailaddress(mapdata.getAddress());//详细地址
				 merchantShop.setShopfacadeimage(mapdata.getMerchantImage());//店铺门面图片
				 merchantShops.add(merchantShop);
				 list.add(mapdata);
			}
		} catch (Exception e) {
			logger.error("云图条件检索失败,地图传回的参数错误");
			throw new ExternalException(ExternalExceptionEnum.EXTERNAL_MAP_SERVER_ERROR, "云图条件检索失败,地图传回的参数错误");
		}
		return merchantShops;
	}
}
