package com.atao.base.support;

public interface DBQuery {

	/**
	 * @return 是否为Distinct查询
	 */
	boolean isDistinct();

	/**
	 *
	 * @return select子句
	 */
	String getSelectClause();

	/**
	 *
	 * @return from子句
	 */
	String getFromClause();

	/**
	 * sql语句片段列表，被参数分隔
	 * 
	 * @return sql语句片段列表
	 */
	String[] getWhereSqls();

	/**
	 * @return 参数列表
	 */
	Object[] getWhereValues();

	/**
	 *
	 * @return 末尾Sql子句
	 */
	String getWhereEndSql();

	/**
	 *
	 * @return 排序子句
	 */
	String getOrderByClause();
}