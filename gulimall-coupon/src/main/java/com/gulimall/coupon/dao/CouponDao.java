package com.gulimall.coupon.dao;

import com.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author johnnyjinjing
 * @email johnnyjinjing@gmail.com
 * @date 2021-08-20 21:38:36
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
