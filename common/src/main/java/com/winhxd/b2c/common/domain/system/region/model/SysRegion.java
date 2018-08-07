package com.winhxd.b2c.common.domain.system.region.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel
public class SysRegion implements Serializable{

    private static final long serialVersionUID = -2960449858467596217L;
    @ApiModelProperty(value = "地理区域编号")
    private String regionCode;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区县")
    private String town;

    @ApiModelProperty(value = "乡镇")
    private String county;

    @ApiModelProperty(value = "居委会，村")
    private String village;

    @ApiModelProperty(value = "省编号")
    private String provinceCode;

    @ApiModelProperty(value = "市编号")
    private String cityCode;

    @ApiModelProperty(value = "区县编号")
    private String countyCode;

    @ApiModelProperty(value = "乡镇编号")
    private String townCode;

    @ApiModelProperty(value = "居委会，村编号")
    private String villageCode;

     /**
     * 地域级别
     * 1.	省
     * 2.	市
     * 3.	区县
     * 4.	/镇（街道）
     * 5.	村（居委会）
     */
     @ApiModelProperty(value = "区域行政级别（1=省，2=市，3=区县，4=乡/镇，5=村/居委会）")
    private Integer level;

    @ApiModelProperty(value = "111=居委会")
    private Integer villageType;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getVillageType() {
        return villageType;
    }

    public void setVillageType(Integer villageType) {
        this.villageType = villageType;
    }
}