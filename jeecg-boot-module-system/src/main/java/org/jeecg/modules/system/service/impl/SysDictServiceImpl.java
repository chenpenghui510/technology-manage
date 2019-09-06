package org.jeecg.modules.system.service.impl;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.mapper.SysDictItemMapper;
import org.jeecg.modules.system.mapper.SysDictMapper;
import org.jeecg.modules.system.model.TreeSelectModel;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Service
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;
	@Autowired
    private SysDictItemMapper sysDictItemMapper;

	/**
	 * 通过查询指定code 获取字典
	 * @param code
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstant.DICT_CACHE,key = "#code")
	public List<DictModel> queryDictItemsByCode(String code) {
		log.info("无缓存dictCache的时候调用这里！");
		return sysDictMapper.queryDictItemsByCode(code);
	}

	/**
	 * 通过查询指定code 获取字典值text
	 * @param code
	 * @param key
	 * @return
	 */

	@Override
	@Cacheable(value = CacheConstant.DICT_CACHE)
	public String queryDictTextByKey(String code, String key) {
		log.info("无缓存dictText的时候调用这里！");
		return sysDictMapper.queryDictTextByKey(code, key);
	}

	/**
	 * 通过查询指定table的 text code 获取字典
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @return
	 */
	@Override
	//@Cacheable(value = "dictTableCache")
	public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
		log.info("无缓存dictTableList的时候调用这里！");
		return sysDictMapper.queryTableDictItemsByCode(table,text,code);
	}

	@Override
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
		log.info("无缓存dictTableList的时候调用这里！");
		return sysDictMapper.queryTableDictItemsByCodeAndFilter(table,text,code,filterSql);
	}
	
	/**
	 * 通过查询指定table的 text code 获取字典值text
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @param key
	 * @return
	 */
	@Override
	@Cacheable(value = "dictTableCache")
	public String queryTableDictTextByKey(String table,String text,String code, String key) {
		log.info("无缓存dictTable的时候调用这里！");
		return sysDictMapper.queryTableDictTextByKey(table,text,code,key);
	}

    /**
     * 根据字典类型id删除关联表中其对应的数据
     */
    @Override
    public boolean deleteByDictId(SysDict sysDict) {
        sysDict.setDelFlag(CommonConstant.DEL_FLAG_1);
        return  this.updateById(sysDict);
    }

    @Override
    @Transactional
    public void saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList) {

        sysDictMapper.insert(sysDict);
        if (sysDictItemList != null) {
            for (SysDictItem entity : sysDictItemList) {
                entity.setDictId(sysDict.getId());
                sysDictItemMapper.insert(entity);
            }
        }
    }

	@Override
	public List<DictModel> queryAllDepartBackDictModel() {
		return baseMapper.queryAllDepartBackDictModel();
	}

	@Override
	public List<DictModel> queryAllUserBackDictModel() {
		return baseMapper.queryAllUserBackDictModel();
	}
	
	@Override
	public List<DictModel> queryTableDictItems(String table, String text, String code, String keyword) {
		return baseMapper.queryTableDictItems(table, text, code, "%"+keyword+"%");
	}

	@Override
	public List<TreeSelectModel> queryTreeList(String table, String text, String code, String pidField, String pid, String hasChildField) {
		return baseMapper.queryTreeList(table, text, code, pidField, pid,hasChildField);
	}

	public Object getDictInfoByObj(Object entity){
		ObjectMapper mapper = new ObjectMapper();
		String json="{}";
		try {
			//解决@JsonFormat注解解析不了的问题详见SysAnnouncement类的@JsonFormat
			json = mapper.writeValueAsString(entity);
		} catch (JsonProcessingException e) {
			log.error("json解析失败"+e.getMessage(),e);
		}
		JSONObject item = JSONObject.parseObject(json);
		for (Field field : oConvertUtils.getAllFields(entity)) {
			if (field.getAnnotation(Dict.class) != null) {
				String code = field.getAnnotation(Dict.class).dicCode();
				String text = field.getAnnotation(Dict.class).dicText();
				String table = field.getAnnotation(Dict.class).dictTable();
				String key = String.valueOf(item.get(field.getName()));

				//翻译字典值对应的txt
				String textValue = translateDictValue(code, text, table, key);

				log.debug(" 字典Val : "+ textValue);
				log.debug(" __翻译字典字段__ "+field.getName() + CommonConstant.DICT_TEXT_SUFFIX+"： "+ textValue);
				item.put(field.getName() + CommonConstant.DICT_TEXT_SUFFIX, textValue);
			}
			//date类型默认转换string格式化日期
			if (field.getType().getName().equals("java.util.Date")&&field.getAnnotation(JsonFormat.class)==null&&item.get(field.getName())!=null){
				SimpleDateFormat aDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				item.put(field.getName(), aDate.format(new Date((Long) item.get(field.getName()))));
			}
		}
		return item;
	}

	/**
	 *  翻译字典文本
	 * @param code
	 * @param text
	 * @param table
	 * @param key
	 * @return
	 */
	private String translateDictValue(String code, String text, String table, String key) {
		if(oConvertUtils.isEmpty(key)) {
			return null;
		}
		StringBuffer textValue=new StringBuffer();
		String[] keys = key.split(",");
		for (String k : keys) {
			String tmpValue = null;
			log.debug(" 字典 key : "+ k);
			if (k.trim().length() == 0) {
				continue; //跳过循环
			}
			if (!org.springframework.util.StringUtils.isEmpty(table)){
				tmpValue= this.queryTableDictTextByKey(table,text,code,k.trim());
			}else {
				tmpValue = this.queryDictTextByKey(code, k.trim());
			}

			if (tmpValue != null) {
				if (!"".equals(textValue.toString())) {
					textValue.append(",");
				}
				textValue.append(tmpValue);
			}

		}
		return textValue.toString();
	}

}
