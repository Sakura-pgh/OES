package com.mindskip.xzs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.domain.Classify;
import com.mindskip.xzs.repository.ClassifyMapper;
import com.mindskip.xzs.service.ClassifyService;
import com.mindskip.xzs.viewmodel.admin.education.ClassifyPageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassifyServiceImpl extends BaseServiceImpl<Classify> implements ClassifyService {

    private final static String CACHE_NAME = "Classify";
    private final ClassifyMapper classifyMapper;

    @Autowired
    public ClassifyServiceImpl(ClassifyMapper classifyMapper) {
        super(classifyMapper);
        this.classifyMapper = classifyMapper;
    }

    @Override
//    @Cacheable(value = CACHE_NAME, key = "#id", unless = "#result == null")
    public Classify selectById(Integer id) {
        return super.selectById(id);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public int deleteById(Integer id) {
        return super.deleteById(id);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#record.id")
    public int updateByIdFilter(Classify record) {
        return super.updateByIdFilter(record);
    }

    @Override
    public List<Classify> allClassify() {
        return classifyMapper.allClassify();
    }

    @Override
    public PageInfo<Classify> page(ClassifyPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                classifyMapper.page(requestVM)
        );
    }

}
