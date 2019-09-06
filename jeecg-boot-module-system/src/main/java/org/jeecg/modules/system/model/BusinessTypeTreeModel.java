package org.jeecg.modules.system.model;

import org.jeecg.modules.system.entity.Business;
import org.jeecg.modules.system.entity.BusinessType;
import org.jeecg.modules.system.entity.SysDepart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
public class BusinessTypeTreeModel implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 对应  id字段,前端数据树中的key*/
    private String key;

    /** 对应  大分类1  小分类2   */
    private String value;

    /** 对应  业务name字段,前端数据树中的title*/
    private String title;

    private boolean isLeaf;

    private String id;

    private Date createTime;

    private List<BusinessTypeTreeModel> children = new ArrayList<>();


    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     * @param businessType
     */
    public BusinessTypeTreeModel(BusinessType businessType) {
        this.key = businessType.getId();
        this.value = "1";
        this.title = businessType.getName();
        this.createTime = businessType.getCreateTime();
        this.id = businessType.getId();
        this.isLeaf = false;
    }

    public BusinessTypeTreeModel(Business business) {
        this.key = business.getId().toString();
        this.value = "0";
        this.title = business.getBusinessName();
        this.createTime = business.getCreateTime();
        this.id = business.getId().toString();
        this.isLeaf = true;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isleaf) {
        this.isLeaf = isleaf;
    }

    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BusinessTypeTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<BusinessTypeTreeModel> children) {
        if (children==null){
            this.isLeaf=true;
        }
        this.children = children;
    }

    public BusinessTypeTreeModel() { }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessTypeTreeModel model = (BusinessTypeTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(children, model.children);
    }

    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(id, createTime, children);
    }

}
