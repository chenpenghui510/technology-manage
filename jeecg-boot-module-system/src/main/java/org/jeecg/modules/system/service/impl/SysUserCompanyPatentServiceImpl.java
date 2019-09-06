package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.SysUserCompanyPatentMapper;
import org.jeecg.modules.system.service.ISysUserCompanyDataService;
import org.jeecg.modules.system.service.ISysUserCompanyPatentService;
import org.jeecg.modules.system.util.GainetUtils;
import org.jeecg.modules.system.vo.SysUserPatentCountVo;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业用户专利著作
 * @Author: jeecg-boot
 * @Date:   2019-08-16
 * @Version: V1.0
 */
@Service
public class SysUserCompanyPatentServiceImpl extends ServiceImpl<SysUserCompanyPatentMapper, SysUserCompanyPatent> implements ISysUserCompanyPatentService {

    @Resource
    private SysUserCompanyPatentMapper sysUserCompanyPatentMapper;

    @Resource
    private ISysUserCompanyDataService sysUserCompanyDataService;

    @Override
    public Map<String,Object> countByUserId(String userId){
        //总发明专利数
        Integer all=sysUserCompanyPatentMapper.countByUserIdAndSort(userId,null);
        //有效发明专利数
        Integer valid=sysUserCompanyPatentMapper.countByUserIdAndSort(userId,1);
        Map<String,Object> rsMap=new HashMap<>();
        rsMap.put("all",all);
        rsMap.put("valid",valid);
        return rsMap;
    }

    public List<SysUserPatentCountVo> sysUserPatentCountVo(Integer year){
        List<SysUserPatentCountVo> sysUserPatentCountVos=new ArrayList<>();
        try{
            List<Map<String,Object>> userList=sysUserCompanyPatentMapper.userList();
            if(userList==null||userList.size()==0){
                return sysUserPatentCountVos;
            }
            if(GainetUtils.isEmpty(year)){
            List<Integer> yearList=sysUserCompanyPatentMapper.yearList();
            if(yearList==null||yearList.size()==0){
                return sysUserPatentCountVos;
            }
            //循环年已年为单位
            for(int i=0;i<yearList.size();i++){
                Integer yearInteger=yearList.get(i);
                //子循环企业用户
                for (int j=0;j<userList.size();j++){
                    SysUserPatentCountVo sysUserPatentCountVo=new SysUserPatentCountVo();
                    sysUserPatentCountVo.setYear(yearInteger);
                    String userId=GainetUtils.stringOf(userList.get(j).get("id"));
                    String userName=GainetUtils.stringOf(userList.get(j).get("realname"));
                    sysUserPatentCountVo.setUserId(userId);
                    sysUserPatentCountVo.setUserName(userName);
                    //1.专利总数
                    Integer all=sysUserCompanyPatentMapper.censusCount(yearInteger,null,null,null,userId);
                    sysUserPatentCountVo.setAll(all);
                    //2.有效发明专利
                    Integer invent=sysUserCompanyPatentMapper.censusCount(yearInteger,"1",null,null,userId);
                    sysUserPatentCountVo.setInvent(invent);
                    //3.有效实用新型专利
                    Integer practical=sysUserCompanyPatentMapper.censusCount(yearInteger,"2",null,null,userId);
                    sysUserPatentCountVo.setPractical(practical);
                    //4.有效外观专利
                    Integer exterior=sysUserCompanyPatentMapper.censusCount(yearInteger,"3",null,null,userId);
                    sysUserPatentCountVo.setExterior(exterior);
                    //5.有效国际专利
                    Integer international=sysUserCompanyPatentMapper.censusCount(yearInteger,"4",null,null,userId);
                    sysUserPatentCountVo.setInternational(international);
                    //6.国家金奖
                    Integer countrySpecial=sysUserCompanyPatentMapper.censusCount(yearInteger,null,"1","0",userId);
                    sysUserPatentCountVo.setCountrySpecial(countrySpecial);
                    //7.国家一等奖
                    Integer countryOne=sysUserCompanyPatentMapper.censusCount(yearInteger,null,"1","1",userId);
                    sysUserPatentCountVo.setCountryOne(countryOne);
                    //8.国家二等奖
                    Integer countryTwo=sysUserCompanyPatentMapper.censusCount(yearInteger,null,"1","2",userId);
                    sysUserPatentCountVo.setCountryTwo(countryTwo);
                    //9.省特等奖
                    Integer provinceSpecial=sysUserCompanyPatentMapper.censusCount(yearInteger,null,"2","0",userId);
                    sysUserPatentCountVo.setProvinceSpecial(provinceSpecial);
                    //10.省一等奖
                    Integer provinceOne=sysUserCompanyPatentMapper.censusCount(yearInteger,null,"2","1",userId);
                    sysUserPatentCountVo.setProvinceOne(provinceOne);
                    //11.省二等奖
                    Integer provinceTwo=sysUserCompanyPatentMapper.censusCount(yearInteger,null,"2","2",userId);
                    sysUserPatentCountVo.setProvinceTwo(provinceTwo);
                    // 开始获取企业相关信息
                    /*QueryWrapper<SysBlacklist> queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("user_id",userId);
                    List<SysBlacklist> sysBlacklists=list(queryWrapper);
                    QueryWrapper<SysUserPersonalData> queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("user_id",userId);
                    List<SysUserPersonalData> sysUserPersonalDataList=list(queryWrapper);*/
                    QueryWrapper<SysUserCompanyData>queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("user_id",userId);
                    List<SysUserCompanyData> sysUserCompanyDataList=sysUserCompanyDataService.list(queryWrapper);
                    if(sysUserCompanyDataList!=null&&sysUserCompanyDataList.size()>0){
                        SysUserCompanyData sysUserCompanyData=sysUserCompanyDataList.get(0);
                        if(sysUserCompanyData!=null){
                            sysUserPatentCountVo.setNationalAdvantage(sysUserCompanyData.getNationalAdvantage());
                            sysUserPatentCountVo.setProvincialAdvantage(sysUserCompanyData.getProvincialAdvantage());
                            sysUserPatentCountVo.setCityAdvantage(sysUserCompanyData.getCityAdvantage());
                            sysUserPatentCountVo.setViolate(sysUserCompanyData.getViolate());
                            sysUserPatentCountVo.setIsoTime(sysUserCompanyData.getIsoTime());
                            sysUserPatentCountVo.setFinancingAmount(sysUserCompanyData.getFinancingAmount());
                            sysUserPatentCountVo.setFinancingTime(sysUserCompanyData.getFinancingTime());
                            sysUserPatentCountVo.setGovernmentAid(sysUserCompanyData.getGovernmentAid());
                        }
                    }
                    sysUserPatentCountVos.add(sysUserPatentCountVo);
                }
            }
            }/*else {

                for (int j = 0; j < userList.size(); j++) {
                    SysUserPatentCountVo sysUserPatentCountVo = new SysUserPatentCountVo();
                    sysUserPatentCountVo.setYear(year);
                    String userId = GainetUtils.stringOf(userList.get(j).get("id"));
                    String userName = GainetUtils.stringOf(userList.get(j).get("username"));
                    sysUserPatentCountVo.setUserId(userId);
                    sysUserPatentCountVo.setUserName(userName);
                    //1.专利总数
                    Integer all = sysUserCompanyPatentMapper.censusCount(year, null, null, null, userId);
                    sysUserPatentCountVo.setAll(all);
                    //2.有效发明专利
                    Integer invent = sysUserCompanyPatentMapper.censusCount(year, "1", null, null, userId);
                    sysUserPatentCountVo.setInvent(invent);
                    //3.有效实用新型专利
                    Integer practical = sysUserCompanyPatentMapper.censusCount(year, "2", null, null, userId);
                    sysUserPatentCountVo.setPractical(practical);
                    //4.有效外观专利
                    Integer exterior = sysUserCompanyPatentMapper.censusCount(year, "3", null, null, userId);
                    sysUserPatentCountVo.setExterior(exterior);
                    //5.有效国际专利
                    Integer international = sysUserCompanyPatentMapper.censusCount(year, "4", null, null, userId);
                    sysUserPatentCountVo.setInternational(international);
                    //6.国家金奖
                    Integer countrySpecial = sysUserCompanyPatentMapper.censusCount(year, null, "1", "0", userId);
                    sysUserPatentCountVo.setCountrySpecial(countrySpecial);
                    //7.国家一等奖
                    Integer countryOne = sysUserCompanyPatentMapper.censusCount(year, null, "1", "1", userId);
                    sysUserPatentCountVo.setCountryOne(countryOne);
                    //8.国家二等奖
                    Integer countryTwo = sysUserCompanyPatentMapper.censusCount(year, null, "1", "2", userId);
                    sysUserPatentCountVo.setCountryTwo(countryTwo);
                    //9.省特等奖
                    Integer provinceSpecial = sysUserCompanyPatentMapper.censusCount(year, null, "2", "0", userId);
                    sysUserPatentCountVo.setProvinceSpecial(provinceSpecial);
                    //10.省一等奖
                    Integer provinceOne = sysUserCompanyPatentMapper.censusCount(year, null, "2", "1", userId);
                    sysUserPatentCountVo.setProvinceOne(provinceOne);
                    //11.省二等奖
                    Integer provinceTwo = sysUserCompanyPatentMapper.censusCount(year, null, "2", "2", userId);
                    sysUserPatentCountVo.setProvinceTwo(provinceTwo);
                    // 开始获取企业相关信息
                    *//*QueryWrapper<SysBlacklist> queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("user_id",userId);
                    List<SysBlacklist> sysBlacklists=list(queryWrapper);
                    QueryWrapper<SysUserPersonalData> queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("user_id",userId);
                    List<SysUserPersonalData> sysUserPersonalDataList=list(queryWrapper);*//*
                    QueryWrapper<SysUserCompanyData> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("user_id", userId);
                    List<SysUserCompanyData> sysUserCompanyDataList = sysUserCompanyDataService.list(queryWrapper);
                    if (sysUserCompanyDataList != null && sysUserCompanyDataList.size() > 0) {
                        SysUserCompanyData sysUserCompanyData = sysUserCompanyDataList.get(0);
                        if (sysUserCompanyData != null) {
                            sysUserPatentCountVo.setNationalAdvantage(sysUserCompanyData.getNationalAdvantage());
                            sysUserPatentCountVo.setProvincialAdvantage(sysUserCompanyData.getProvincialAdvantage());
                            sysUserPatentCountVo.setCityAdvantage(sysUserCompanyData.getCityAdvantage());
                            sysUserPatentCountVo.setIsoTime(sysUserCompanyData.getIsoTime());
                            sysUserPatentCountVo.setFinancingAmount(sysUserCompanyData.getFinancingAmount());
                            sysUserPatentCountVo.setFinancingTime(sysUserCompanyData.getFinancingTime());
                            sysUserPatentCountVo.setGovernmentAid(sysUserCompanyData.getGovernmentAid());
                        }
                    }

                    sysUserPatentCountVos.add(sysUserPatentCountVo);

                }
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return sysUserPatentCountVos;
    }
   public IPage<SysUserCompanyPatent> pageListNew(Page<SysUserCompanyPatent> page, String name, String sort, String level, String prize, String startTime, String endTime,String userId){
        return sysUserCompanyPatentMapper.pageListNew(page,name,sort,level,prize,startTime,endTime,userId);
   }
}
