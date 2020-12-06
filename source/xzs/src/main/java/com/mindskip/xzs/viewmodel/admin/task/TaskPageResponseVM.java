package com.mindskip.xzs.viewmodel.admin.task;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TaskPageResponseVM {

    private Integer id;

    private String title;

    private Integer gradeLevel;

    private String createUserName;

    private String createTime;

    private List<String> limitDateTime;

    private Boolean deleted;

}
