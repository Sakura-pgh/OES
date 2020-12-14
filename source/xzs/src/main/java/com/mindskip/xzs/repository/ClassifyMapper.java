package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.Classify;
import com.mindskip.xzs.viewmodel.admin.education.ClassifyPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface ClassifyMapper extends BaseMapper<Classify> {
    int deleteByPrimaryKey(Integer id);

    int insert(Classify record);

    int insertSelective(Classify record);

    Classify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Classify record);

    int updateByPrimaryKey(Classify record);

    List<String> getNamesByIds(Set<Integer> classifyIds);

    List<Classify> allClassify();

    List<Classify> page(ClassifyPageRequestVM requestVM);
}
