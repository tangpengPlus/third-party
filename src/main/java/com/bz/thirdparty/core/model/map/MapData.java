package com.bz.thirdparty.core.model.map;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.bz.framework.pojo.base.BasePojo;
/**
 * 
 * 作者: 彭云山
 * 描述:高德地图云图定点信息
 * 创建时间:2017年10月18日下午5:31:11
 * 修改备注:
 */
public class MapData extends BasePojo{

	private static final long serialVersionUID = -1836491046276019889L;

	 @JSONField(name="_id")
	private String id;//高德地图定点数据_Id
	
	 @JSONField(name="_name")
	private String name;//_数据名称
	
	@JSONField(name="_location")
	private String location;//_坐标
	
	private String coordtype;//坐标类型 可选值：1: gps 2: autonavi 3: baidu
	
	private String _province;//_所在省
	private String _city;//_所在市
	private String _district;//_所在区
	private Date _createtime;//_创建时间
	private Date _updatetime;//_更新时间
	private String filter;//_查询条件
	
	@JSONField(name="_address")
	private String address;//_地址
	
	private String merchantName;//店铺名称
	
	private String merchantType;//店铺类型
	
	private String merchantImage;//店铺形象图
	
	private String merchantDiscript;//商家描述

	

	public String getCoordtype() {
		return coordtype;
	}

	public void setCoordtype(String coordtype) {
		this.coordtype = coordtype;
	}

    
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getMerchantImage() {
		return merchantImage;
	}

	public void setMerchantImage(String merchantImage) {
		this.merchantImage = merchantImage;
	}

	public String getMerchantDiscript() {
		return merchantDiscript;
	}

	public void setMerchantDiscript(String merchantDiscript) {
		this.merchantDiscript = merchantDiscript;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String get_province() {
		return _province;
	}

	public void set_province(String _province) {
		this._province = _province;
	}

	public String get_city() {
		return _city;
	}

	public void set_city(String _city) {
		this._city = _city;
	}

	public String get_district() {
		return _district;
	}

	public void set_district(String _district) {
		this._district = _district;
	}

	public Date get_createtime() {
		return _createtime;
	}

	public void set_createtime(Date _createtime) {
		this._createtime = _createtime;
	}

	public Date get_updatetime() {
		return _updatetime;
	}

	public void set_updatetime(Date _updatetime) {
		this._updatetime = _updatetime;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	
	
	

}
