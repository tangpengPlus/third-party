package com.bz.thirdparty.map;

public class GaodeMapModel {
	
	private String citycode;//城市编码
	
	private String adcode;//区域编码
	
	private String name;//行政区名称
	
	private String polyline;//行政区边界坐标点
	
	private String center;//城市中心点
	
	private String  level;//行政区划级别

	/**
	 * @return the citycode
	 */
	public String getCitycode() {
		return citycode;
	}

	/**
	 * @param citycode the citycode to set
	 */
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	/**
	 * @return the adcode
	 */
	public String getAdcode() {
		return adcode;
	}

	/**
	 * @param adcode the adcode to set
	 */
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the polyline
	 */
	public String getPolyline() {
		return polyline;
	}

	/**
	 * @param polyline the polyline to set
	 */
	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}

	/**
	 * @return the center
	 */
	public String getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	public void setCenter(String center) {
		this.center = center;
	}

	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	
	
	
	
}
