package org.jeecg.modules.system.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.mapper.SysDepartMapper;
import org.jeecg.modules.system.mapper.SysUserDepartMapper;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.netty.util.internal.StringUtil;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
@Service
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {

	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private SysDepartMapper sysDepartMapper;



	/**
	 * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
	 */
	@Override
	public List<SysDepartTreeModel> queryTreeList() {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<SysDepartTreeModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
		return listResult;
	}

	/**
	 * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
	 */
	/*@Override
	public List<SysDepartTreeModel> queryTreeList() {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, 0);
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		*//*List<SysDepartTreeModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);*//*

		List<SysDepartTreeModel> listResult = wrapTreeDataToTreeList(list);
		return listResult;
	}*/




	public List<SysDepartTreeModel> wrapTreeDataToTreeList(List<SysDepart> recordList) {
		// 在该方法每请求一次,都要对全局list集合进行一次清理
		List<DepartIdModel> idList = new ArrayList<>(4);
		List<SysDepartTreeModel> records = new ArrayList<>();
		for (int i = 0; i < recordList.size(); i++) {
			SysDepart depart = recordList.get(i);
			//查询到 部门负责人
			SysDepartTreeModel dptTree = new SysDepartTreeModel(depart);
			String username = sysUserService.getUserNameByDptId(depart.getId());
			SysDepartTreeModel treeModel = new SysDepartTreeModel(depart);
			treeModel.setUserName(username);
			records.add(treeModel);
		}
		List<SysDepartTreeModel> tree = FindsDepartsChildrenUtil.findChildren(records, idList);
		FindsDepartsChildrenUtil.setEmptyChildrenAsNull(tree);
		return tree;
	}



	/**
	 * saveDepartData 对应 add 保存用户在页面添加的新的部门对象数据
	 */
	@Override
	@Transactional
	public void saveDepartData(SysDepart sysDepart, String username) {
		if (sysDepart != null && username != null) {
			if (sysDepart.getParentId() == null) {
				sysDepart.setParentId("");
			}
			String s = UUID.randomUUID().toString().replace("-", "");
			sysDepart.setId(s);
			// 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
			// 获取父级ID
			String parentId = sysDepart.getParentId();
			String orgType = generateOrgCode(parentId);
			sysDepart.setOrgType(String.valueOf(orgType));
			sysDepart.setCreateTime(new Date());
			sysDepart.setDelFlag("0");
			this.save(sysDepart);
		}

	}

	/**
	 * saveDepartData 的调用方法,生成部门编码和部门类型
	 *
	 * @param parentId
	 * @return
	 */
	private String generateOrgCode(String parentId) {
		//update-begin--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		LambdaQueryWrapper<SysDepart> query1 = new LambdaQueryWrapper<SysDepart>();
		// 创建一个List集合,存储查询返回的所有SysDepart对象
		List<SysDepart> departList = new ArrayList<>();
		// 定义部门类型
		String orgType = "";
		// 如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回
		if (StringUtil.isNullOrEmpty(parentId)) {
			// 线判断数据库中的表是否为空,空则直接返回初始编码
			query1.eq(SysDepart::getParentId, "");
			departList = this.list(query1);
			if(departList == null || departList.size() == 0) {
				return orgType;
			}else {
				SysDepart depart = departList.get(0);
				orgType = depart.getOrgType();
			}
		} else { // 反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
			// 封装查询同级的条件
			query.eq(SysDepart::getParentId, parentId);
			// 降序排序
			// 查询出同级部门的集合
			List<SysDepart> parentList = this.list(query);
			// 查询出父级部门
			SysDepart depart = this.getById(parentId);
			// 获取父级部门的Code
			// 根据父级部门类型算出当前部门的类型
			orgType = String.valueOf(Integer.valueOf("".equals(depart.getOrgType())?"1":depart.getOrgType()) + 1);
			// 处理同级部门为null的情况
		}
		// 返回最终封装了部门编码和部门类型的数组
		return orgType;
		//update-end--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
	}


	/**
	 * removeDepartDataById 对应 delete方法 根据ID删除相关部门数据
	 *
	 */
	/*
	 * @Override
	 *
	 * @Transactional public boolean removeDepartDataById(String id) {
	 * System.out.println("要删除的ID 为=============================>>>>>"+id); boolean
	 * flag = this.removeById(id); return flag; }
	 */

	/**
	 * updateDepartDataById 对应 edit 根据部门主键来更新对应的部门数据
	 */
	@Override
	@Transactional
	public Boolean updateDepartDataById(SysDepart sysDepart, String username) {
		if (sysDepart != null && username != null) {
			sysDepart.setUpdateTime(new Date());
			sysDepart.setUpdateBy(username);
			this.updateById(sysDepart);
			return true;
		} else {
			return false;
		}

	}


	/**
	 * <p>
	 * 根据关键字搜索相关的部门数据    模糊查询
	 * </p>
	 */
	@Override
	public List<SysDepartTreeModel> searhBy(String keyWord) {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();//模糊查询
		query.like(SysDepart::getDepartName, keyWord);
		//update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索回显优化--------------------
		SysDepartTreeModel model = new SysDepartTreeModel();
		List<SysDepart> departList = this.list(query);
		List<SysDepartTreeModel> newList = new ArrayList<>();
		if(departList.size() > 0) {
			for(SysDepart depart : departList) {
				model = new SysDepartTreeModel(depart);
				model.setChildren(null);
				//update-end--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索功回显优化----------------------
				newList.add(model);
			}
			return newList;
		}
		return null;
	}

	/**
	 * 根据部门id删除并且删除其可能存在的子级任何部门
	 */
	@Override
	public boolean delete(String id) {
		List<String> idList = new ArrayList<>();
		idList.add(id);

		this.checkChildrenExists(id, idList);
		boolean flag=false;
		for(String depId:idList){
			List<Map> business = sysDepartMapper.isDepHasBusiness(depId);
			if(business!=null&&business.size()>0){
				flag=true;
				break;
			}
		}
		if(flag){
			return false;
		}else{
			//循环删除与员工的关联关系
			for(String depId:idList){
				sysDepartMapper.deleteUserDepart(depId);
			}
		}
		boolean ok = this.removeByIds(idList);
		return ok;
	}

	/**
	 * delete 方法调用
	 * @param id
	 * @param idList
	 */
	private void checkChildrenExists(String id, List<String> idList) {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getParentId,id);
		List<SysDepart> departList = this.list(query);
		if(departList != null && departList.size() > 0) {
			for(SysDepart depart : departList) {
				idList.add(depart.getId());
				this.checkChildrenExists(depart.getId(), idList);
			}
		}
	}

	@Override
	public List<SysDepart> queryUserDeparts(String userId) {
		return baseMapper.queryUserDeparts(userId);
	}

}
