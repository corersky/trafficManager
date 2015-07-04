package com.yuandu.erp.common.persistence;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuandu.erp.common.supcan.annotation.treelist.cols.SupCol;
import com.yuandu.erp.common.utils.LongUtils;
import com.yuandu.erp.common.utils.Reflections;
import com.yuandu.erp.modules.sys.entity.User;
import com.yuandu.erp.modules.sys.utils.UserUtils;

/**
 * 数据Entity类
 */
public abstract class TreeEntity<T> extends BaseEntity<T> {

	private static final long serialVersionUID = 1L;

	protected Long id;			//实体编号（唯一标识）
	protected String remarks;	// 备注
	protected User createBy;	// 创建者
	protected Date createDate;	// 创建日期
	protected User updateBy;	// 更新者
	protected Date updateDate;	// 更新日期
	protected Integer delFlag; 	// 删除标记（0：正常；1：删除；2：审核）
	
	protected T parent;	// 父级编号
	protected String parentIds; // 所有父级编号
	protected String name; 	// 机构名称
	protected Integer sort;		// 排序
	
	public TreeEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
		this.sort = 10;
	}
	
	public TreeEntity(Long id) {
		super();
		this.id = id;
	}
	
	@SupCol(isUnique="true", isHide="true")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert(){
		
		/**(统一不再使用)
		// 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
		if (!this.isNewRecord){
			setId(IdGen.uuid());
		}*/
		User user = UserUtils.getUser();
		if (LongUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
	
	/**
	 * 更新之前执行方法，需要手动调用
	 */
	@Override
	public void preUpdate(){
		User user = UserUtils.getUser();
		if (LongUtils.isNotBlank(user.getId())){
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}
	
	@Length(min=0, max=255)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@JsonIgnore
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@Length(min=1, max=2000)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@JsonIgnore
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        DataEntity<?> that = (DataEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
	
	/**
	 * 是否是新记录（默认：false），调用getIsNewRecord()设置新记录，使用自定义ID。
	 * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
	 */
	@JsonIgnore
	public boolean getIsNewRecord(){
		return LongUtils.isBlank(id);
	}
	
	/**
	 * 父对象，只能通过子类实现，父类实现mybatis无法读取
	 * @return
	 */
	public abstract T getParent();

	/**
	 * 父对象，只能通过子类实现，父类实现mybatis无法读取
	 * @return
	 */
	public abstract void setParent(T parent);

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	@JsonIgnore
	public Long getParentId() {
		Long id = null;
		if (parent != null){
			id = (Long)(Reflections.getFieldValue(parent, "id"));
		}
		if(id==null){
			id = 0l;
		}
		return id;
	}
	
}
