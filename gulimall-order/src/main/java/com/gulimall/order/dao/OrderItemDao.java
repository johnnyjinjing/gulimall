package com.gulimall.order.dao;

import com.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author johnnyjinjing
 * @email johnnyjinjing@gmail.com
 * @date 2021-08-20 21:58:21
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
