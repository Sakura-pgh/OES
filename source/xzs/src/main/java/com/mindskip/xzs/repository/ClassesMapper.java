package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.Classes;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassesMapper extends BaseMapper<Classes> {
    int deleteByPrimaryKey(Integer id);

    int insert(Classes record);

    int insertSelective(Classes record);

    Classes selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Classes record);

    int updateByPrimaryKey(Classes record);

}
