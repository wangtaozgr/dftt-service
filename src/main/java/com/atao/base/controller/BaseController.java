package com.atao.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atao.base.model.BaseEntity;
import com.atao.base.service.BaseService;

public abstract class BaseController<T extends BaseEntity> implements IController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected abstract BaseService<T> getService();

}
