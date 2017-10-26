package com.bz.thirdparty.map;
import java.util.Date;
import java.util.Map;
import org.apache.commons.collections4.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bz.dao.mapper.map.RegionMapper;
import com.bz.dao.pojo.map.Region;
import com.bz.framework.util.base.ChineseTransformationUtil;
import com.bz.framework.util.http.HttpClientHelper;
import com.bz.framework.util.number.BzCodeGenerate;
/**
 * 
* @ClassName: MapServiceTest 
* @Description: TODO(地图服务测试类) 
* @author 胡竞
* @date 2017年10月24日 上午11:16:42 
*
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapServiceTest {
	private final Logger logger =LoggerFactory.getLogger(MapServiceTest.class);
	@Autowired
	private RegionMapper regionMapper;
	/**
	 * 
	* @作者 胡竞
	* @Title: addMapDateToMysql 
	* @Description: TODO(将高德地图中的行政数据拉取到本地数据库) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Test
	public void addMapDateToMysql() {
		String key="33328462dd7e166eb6c98a81cf9c3cc3";
		String url="http://restapi.amap.com/v3/config/district";
		String subdistrict="1";
		//请求高德地图获取一级城市相关json数据
		/*Map<String, String> dataMap=new HashedMap<String, String>();
		dataMap.put("subdistrict",subdistrict);
		dataMap.put("key", key);
		HttpClientHelper clientHelper=new HttpClientHelper();
		clientHelper.addParameter(dataMap);*/
		Map<String , String> dataMap=new HashedMap<String ,String>();
		dataMap.put("key", key);
		dataMap.put("subdistrict", subdistrict);
		HttpClientHelper clientHelper=new HttpClientHelper();
		clientHelper.addParameter(dataMap);
		try {
			/*String firstMapData=clientHelper.doPost(url);
			logger.info("请求高德地图查询行政区域接口返回结果:"+firstMapData);
			JSONObject json=(JSONObject) JSONObject.parse(firstMapData);
			logger.info("返回的json对象是:"+json);
			JSONArray jsonArray=JSONObject.parseArray(json.getString("districts"));*/
			
			//请求url地址，返回一级城市JSON数据
			String firstMapData=clientHelper.doPost(url);
			logger.info("请求高德地图查询行政区域接口返回结果:"+firstMapData);
			//将返回的json格式的字符串转换为json对象
			JSONObject json=(JSONObject) JSONObject.parse(firstMapData);
			logger.info("返回的json对象是:"+json);
			//将JSON文本中key值为：“districts”的数据转换为JSONArray
			JSONArray jsonArray=JSONObject.parseArray(json.getString("districts"));
			
			for(Object objet:jsonArray) {
				logger.info("获取的最外层地区信息是:"+objet);
				//获取各一级城市信息
				JSONObject firstCityjson=(JSONObject) JSONObject.parse(objet.toString());
				logger.info("获取的一级城市json对象是:"+firstCityjson);
				JSONArray firstCityJSONArray=JSONObject.parseArray(firstCityjson.getString("districts"));
				for(Object oj:firstCityJSONArray) {
					//获取一级城市
					JSONObject jsonObject=JSONObject.parseObject(oj.toString());
					String citycode=jsonObject.getString("citycode");
					String adcode=jsonObject.getString("adcode");
					String name=jsonObject.getString("name");
					String center=jsonObject.getString("center");
					String level=jsonObject.getString("level");
					logger.info("获取的一级城市信息是\n：citycode："+citycode+"\n adcode："+adcode+"\n name："+name+"\ncenter："+center+"\n level:"+level);
					//存储一级城市信息
					Region record=new Region();
					record.setName(name);
					String searKey=name;
					record.setNumber(BzCodeGenerate.getSysNumber());
					record.setParentcode("0");
					record.setLevel(1);
					record.setCitycode(citycode);
					record.setAdcode(adcode);
					record.setLng(center.split(",")[0]);
					record.setLat(center.split(",")[1]);
					if(name.lastIndexOf("市")!=-1||name.lastIndexOf("省")!=-1) {
				    name=name.substring(0, name.length()-1);
					}
					record.setPinyin(ChineseTransformationUtil.getPinyinString(name));
					record.setCreatetime(new Date());
					regionMapper.insertSelective(record);
					//获取二级行政区域
					dataMap.put("keywords", searKey);
					clientHelper.addParameter(dataMap);
					//二级行政区域
					String secendMapData=clientHelper.doPost(url);
					logger.info("二级区域json数据是:"+secendMapData);
					//转换成json对象
					JSONObject jsonObject2=(JSONObject) JSONObject.parse(secendMapData);
					JSONArray secendCityJSONArray=JSONObject.parseArray(jsonObject2.getString("districts"));
					//获取二级区域查询信息
			    	for(Object object:secendCityJSONArray){
			    		 //获取二级城市
						JSONObject jsonObject2s=JSONObject.parseObject(object.toString());
						JSONArray array=JSONObject.parseArray(jsonObject2s.getString("districts"));
						for(Object object2:array) {
							JSONObject jsonObject3=(JSONObject) JSONObject.parse(object2.toString());
							String citycode2=jsonObject3.getString("citycode");
							String adcode2=jsonObject3.getString("adcode");
							String name2=jsonObject3.getString("name");
							String center2=jsonObject3.getString("center");
							String level2=jsonObject3.getString("level");
							logger.info("获取的二级城市信息是\n：citycode："+citycode2+"\n adcode："+adcode2+"\n name："+name2+"\ncenter："+center2+"\n level:"+level2);
							//存储二级城市信息
							Region record2=new Region();
							record2.setName(name2);
							String searKey2=name2;
							record2.setNumber(BzCodeGenerate.getSysNumber());
							record2.setParentcode(record.getId().toString());
							record2.setLevel(2);
							record2.setCitycode(citycode2);
							record2.setAdcode(adcode2);
							record2.setLng(center2.split(",")[0]);
							record2.setLat(center2.split(",")[1]);
							if(name2.lastIndexOf("城区")!=-1||name2.lastIndexOf("郊县")!=-1) {
						    name2=name2.substring(0, name2.length()-1);
							}
							record2.setPinyin(ChineseTransformationUtil.getPinyinString(name2));
							record2.setCreatetime(new Date());
							regionMapper.insertSelective(record2);
							//获取三级行政区域
							dataMap.put("keywords", searKey2);
							clientHelper.addParameter(dataMap);
							//三级级行政区域
							String threedMapData=clientHelper.doPost(url);
							logger.info("三级区域json数据是:"+threedMapData);
							//转换成json对象
							JSONObject jsonObject4=(JSONObject) JSONObject.parse(threedMapData);
							JSONArray threedCityJSONArray=JSONObject.parseArray(jsonObject4.getString("districts"));
							 for(Object object3:threedCityJSONArray){
								    //获取三级城市
									JSONObject jsonObject3s=JSONObject.parseObject(object3.toString());
									JSONArray array3=JSONObject.parseArray(jsonObject3s.getString("districts"));
									for(Object object4:array3) {
										//获取三级城市
										JSONObject jsonObject4s=JSONObject.parseObject(object4.toString());
										String citycode3=jsonObject4s.getString("citycode");
										String adcode3=jsonObject4s.getString("adcode");
										String name3=jsonObject4s.getString("name");
										String center3=jsonObject4s.getString("center");
										String level3=jsonObject4s.getString("level");
										logger.info("获取的三级城市信息是\n：citycode："+citycode3+"\n adcode："+adcode3+"\n name："+name3+"\ncenter："+center3+"\n level:"+level3);   
									    //存储三级城市信息
										Region record3=new Region();
										record3.setName(name3);
										String searKey3=name3;
										record3.setNumber(BzCodeGenerate.getSysNumber());
										record3.setParentcode(record2.getId().toString());
										record3.setLevel(3);
										record3.setCitycode(citycode3);
										record3.setAdcode(adcode3);
										record3.setLng(center3.split(",")[0]);
										record3.setLat(center3.split(",")[1]);
										if(name3.lastIndexOf("城区")!=-1) {
									        name3=name3.substring(0, name3.length()-2);
										}else if(name3.lastIndexOf("区")!=-1) {
											name3=name3.substring(0, name3.length()-1);
										}
										record3.setPinyin(ChineseTransformationUtil.getPinyinString(name3));
										record3.setCreatetime(new Date());
										//存储三级行政区域
										regionMapper.insertSelective(record3);
										//获取四级行政区域
										dataMap.put("keywords", searKey3);
										clientHelper.addParameter(dataMap);
										//三级级行政区域
										String fourMapData=clientHelper.doPost(url);
										logger.info("四级区域json数据是:"+fourMapData);
										//转换成json对象
										JSONObject jsonObject5=(JSONObject) JSONObject.parse(fourMapData);
										JSONArray fourCityJSONArray=JSONObject.parseArray(jsonObject5.getString("districts"));
										for(Object object5:fourCityJSONArray) {
											//获取三级城市
											JSONObject jsonObject5s=JSONObject.parseObject(object5.toString());
											JSONArray array4=JSONObject.parseArray(jsonObject5s.getString("districts"));
											for(Object object6:array4) {
												//获取四级城市
												JSONObject jsonObject6s=JSONObject.parseObject(object6.toString());
												String citycode4=jsonObject6s.getString("citycode");
												String adcode4=jsonObject6s.getString("adcode");
												String name4=jsonObject6s.getString("name");
												String center4=jsonObject6s.getString("center");
												String level4=jsonObject6s.getString("level");
												logger.info("获取的三级城市信息是\n：citycode："+citycode4+"\n adcode："+adcode4+"\n name："+name4+"\ncenter："+center4+"\n level:"+level4);   
											
												 //存储四级城市信息
												Region record4=new Region();
												record4.setName(name4);
												record4.setNumber(BzCodeGenerate.getSysNumber());
												record4.setParentcode(record3.getId().toString());
												record4.setLevel(4);
												record4.setCitycode(citycode4);
												record4.setAdcode(adcode4);
												record4.setLng(center4.split(",")[0]);
												record4.setLat(center4.split(",")[1]);
												record4.setPinyin("");
												record4.setCreatetime(new Date());
												regionMapper.insertSelective(record4);
											}
											
											
											
										}
										
									}
									
								 
								 
							 }
							
							
						}
			    		
			    	}
					
					
					
					
					
				}
			}
		} catch (Exception e) {
			logger.error("请求高德地图查询行政区域接口错误");
			e.printStackTrace();
		}
		
		
	}
}
