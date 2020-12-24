package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.TaskExam;
import com.mindskip.xzs.viewmodel.admin.task.TaskPageRequestVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskExamMapper extends BaseMapper<TaskExam> {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskExam record);

    int insertSelective(TaskExam record);

    TaskExam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskExam record);

    int updateByPrimaryKey(TaskExam record);

    List<Integer> getTaskExamUser(Integer id);

    int deleteTaskExamUserByTaskExamId(Integer id);

    int insertTaskExamUser(@Param("id")Integer taskExamId, @Param("userIds")List<Integer> userIds);

    List<TaskExam> page(TaskPageRequestVM requestVM);

    List<TaskExam> getByGradeLevel(Integer gradeLevel);

    List<TaskExam> getByGradeLevelAndUserId(@Param("gradeLevel")Integer gradeLevel, @Param("userId")Integer userId);
}
