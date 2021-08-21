package com.gulimall.member.dao;

import com.gulimall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 * 
 * @author johnnyjinjing
 * @email johnnyjinjing@gmail.com
 * @date 2021-08-20 21:46:59
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {
	
}
