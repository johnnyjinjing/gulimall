package com.gulimall.ware.dao;

import com.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author johnnyjinjing
 * @email johnnyjinjing@gmail.com
 * @date 2021-08-20 22:04:52
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
