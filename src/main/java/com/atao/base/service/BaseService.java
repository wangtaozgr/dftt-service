package com.atao.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.transaction.annotation.Transactional;

import com.atao.base.mapper.BaseMapper;
import com.atao.base.model.BaseEntity;
import com.atao.base.support.MyPage;
import com.atao.base.util.BeanMapUtils;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.weekend.Weekend;

public abstract class BaseService<T extends BaseEntity> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected SqlSessionTemplate sqlSessionTemplate;

	public abstract BaseMapper<T> getMapper();

	private List<EntityColumn> idCol = null;
	private Class<T> entityClass;
	private EntityTable entityTable;

	/**
	 * 添加通用的条件
	 * 
	 * @param t
	 */
	protected Weekend<T> genSqlExample(T t) {
		Weekend<T> weekend = Weekend.of(getEntityClass());
		return weekend;
	}

	public T queryOne(T t, Map<String, Object> params) {
		if (t == null) {
			t = convert2Bean(params);
		}
		T o = getMapper().selectOne(t);
		return o;
	}

	/**
	 * 自定义查询条件，
	 * 
	 * @param t
	 * @param params
	 * @return
	 */
	public List<T> queryList(T t, Map<String, Object> params) {
		if (t == null) {
			t = convert2Bean(params);
		}
		if (t != null && t.getPage() != null && t.getRows() != null) {
			PageHelper.startPage(t.getPage(), t.getRows());
		}
		return queryByExample(t, params);
	}

	public MyPage<T> queryPage(T t, Map<String, Object> params) {
		List<T> list = queryListPage(t, params);
		MyPage<T> page = new MyPage<T>(list);
		return page;
	}

	public int getCount(T t) {
		return getMapper().selectCount(t);
	}

	public List<T> queryAll() {
		return getMapper().selectAll();
	}

	public T queryById(Object id) {
		T o = getMapper().selectByPrimaryKey(id);
		return o;
	}

	public boolean exists(Object id) {
		return getMapper().existsWithPrimaryKey(id);
	}

	/**
	 * 根据id修改不为空的字段
	 * 
	 * @param t
	 * @return
	 */
	@Transactional
	public int updateBySelect(T t) {
		return this.getMapper().updateByPrimaryKeySelective(t);
	}

	@Transactional
	public int insert(T t) {
		return getMapper().insertSelective(t);
	}

	@Transactional
	public int save(T t) {
		int r = this.getMapper().updateByPrimaryKeySelective(t);
		if (r == 0) {
			r = getMapper().insertSelective(t);
		}
		return r;
	}

	/*
	 * public String genId() { getEntityClass(); return
	 * KeyFactory.getKeyGenerator(entityTable.getSchema() +
	 * entityTable.getName()).genNextKey(); }
	 */
	@Transactional
	public int delete(T t) {
		return getMapper().delete(t);
	}
	
	@Transactional
	public int deleteById(Object id) {
		return getMapper().deleteByPrimaryKey(id);

	}

	/**
	 * 批量查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<T> queryByIds(Object... ids) {
		List<T> rstList = new ArrayList<T>();
		for (Object id : ids) {
			T t = this.queryById(id);
			if (null == t)
				continue;
			rstList.add(t);
		}
		return rstList;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	public void deleteByids(Object... ids) {
		for (Object id : ids) {
			deleteById(id);
		}
	}

	private List<T> queryByExample(T t, Map<String, Object> params) {
		if (t == null) {
			t = convert2Bean(params);
		}
		Example example = genSqlExample(t, params);
		return getMapper().selectByExample(example);
	}

	private List<T> queryListPage(T t, Map<String, Object> params) {
		if (t == null) {
			t = convert2Bean(params);
		}
		if (t != null) {
			t.initPage();
			PageHelper.startPage(t.getPage(), t.getRows());
		}
		return queryByExample(t, params);
	}

	private Weekend<T> genSqlExample(T t, Map<String, Object> params) {
		Weekend<T> weekend = genSqlExample(t);
		// 添加排序 格式：a=0&b=1,说明0：升序、1倒序
		if (params != null && params.containsKey("sorts") && params.get("sorts") != null) {
			String sorts = params.get("sorts").toString().trim();
			for (String sort : sorts.split("&")) {
				String[] s = sort.split("=");
				if ("0".equals(s[1])) {
					weekend.orderBy(s[0]).asc();
				} else {
					weekend.orderBy(s[0]).desc();
				}
			}
		}
		return weekend;
	}

	private T convert2Bean(Map<String, Object> params) {
		return BeanMapUtils.convertMapToBean(getEntityClass(), params);
	}

	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		if (entityClass != null)
			return entityClass;
		ResolvableType resolvableType = ResolvableType.forClass(getClass());
		entityClass = (Class<T>) resolvableType.as(BaseService.class).getGenerics()[0].resolve();
		entityTable = EntityHelper.getEntityTable(entityClass);
		return entityClass;
	}

	protected List<EntityColumn> getIdCol() {
		if (idCol != null) {
			return idCol;
		}
		if (null == entityClass) {
			entityClass = getEntityClass();
		}
		Set<EntityColumn> cols = entityTable.getEntityClassPKColumns();
		// 获取主键值
		idCol = new ArrayList<>();
		idCol.addAll(cols);
		return idCol;
	}

}