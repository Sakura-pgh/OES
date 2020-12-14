package com.mindskip.xzs.viewmodel.admin.exam;

import lombok.Data;

import java.util.List;

@Data
public class ExamResponseVM {
    private Integer id;

    private String name;

    private Integer questionCount;

    private Integer score;

    private String createTime;

    private Integer createUser;

    private Integer subjectId;

    private String classifyNames;

    private Integer paperType;

    private Integer frameTextContentId;
}
