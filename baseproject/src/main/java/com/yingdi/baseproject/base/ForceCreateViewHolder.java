package com.yingdi.baseproject.base;

/**
 * 增加了判断强制创建接口的ViewHolder
 * 
 * @author WangYingDi
 */
public interface ForceCreateViewHolder<T> extends ViewHolder<T> {

	boolean forceCreate(T data);
}
