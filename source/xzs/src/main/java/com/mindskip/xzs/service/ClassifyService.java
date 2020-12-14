package com.mindskip.xzs.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.domain.Classify;
import com.mindskip.xzs.viewmodel.admin.education.ClassifyPageRequestVM;

import java.util.List;

public interface ClassifyService extends BaseService<Classify> {

    List<Classify> allClassify();

    PageInfo<Classify> page(ClassifyPageRequestVM requestVM);
}
