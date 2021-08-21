package com.gulimall.product.dao;

import com.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author johnnyjinjing
 * @email johnnyjinjing@gmail.com
 * @date 2021-08-19 13:34:51
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
