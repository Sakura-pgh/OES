package com.mindskip.xzs.service.impl;

import com.mindskip.xzs.domain.ExamPaper;
import com.mindskip.xzs.domain.TaskExam;
import com.mindskip.xzs.domain.TextContent;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.domain.task.TaskItemObject;
import com.mindskip.xzs.repository.ExamPaperMapper;
import com.mindskip.xzs.repository.TaskExamMapper;
import com.mindskip.xzs.repository.UserMapper;
import com.mindskip.xzs.service.TaskExamService;
import com.mindskip.xzs.service.TextContentService;
import com.mindskip.xzs.service.enums.ActionEnum;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.utility.JsonUtil;
import com.mindskip.xzs.utility.ModelMapperSingle;
import com.mindskip.xzs.viewmodel.admin.exam.ExamResponseVM;
import com.mindskip.xzs.viewmodel.admin.task.TaskPageRequestVM;
import com.mindskip.xzs.viewmodel.admin.task.TaskRequestVM;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.viewmodel.admin.user.UserCreateVM;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskExamServiceImpl extends BaseServiceImpl<TaskExam> implements TaskExamService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    private final TaskExamMapper taskExamMapper;
    private final TextContentService textContentService;
    private final ExamPaperMapper examPaperMapper;
    private final UserMapper userMapper;

    @Autowired
    public TaskExamServiceImpl(TaskExamMapper taskExamMapper, TextContentService textContentService, ExamPaperMapper examPaperMapper, UserMapper userMapper) {
        super(taskExamMapper);
        this.taskExamMapper = taskExamMapper;
        this.textContentService = textContentService;
        this.examPaperMapper = examPaperMapper;
        this.userMapper = userMapper;
    }

    @Override
    public PageInfo<TaskExam> page(TaskPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                taskExamMapper.page(requestVM)
        );
    }

    @Override
    @Transactional
    public void edit(TaskRequestVM model, User user) {
        ActionEnum actionEnum = (model.getId() == null) ? ActionEnum.ADD : ActionEnum.UPDATE;
        boolean limitUser = model.getStudentItems() != null && model.getStudentItems().size() != 0;
        TaskExam taskExam = null;
        if (actionEnum == ActionEnum.ADD) {
            Date now = new Date();
            taskExam = modelMapper.map(model, TaskExam.class);
            taskExam.setCreateUser(user.getId());
            taskExam.setCreateUserName(user.getUserName());
            taskExam.setCreateTime(now);
            taskExam.setDeleted(false);
            taskExam.setLimitUser(limitUser);
            taskExam.setLimitStartTime(DateTimeUtil.parse(model.getLimitDateTime().get(0), DateTimeUtil.STANDER_FORMAT));
            taskExam.setLimitEndTime(DateTimeUtil.parse(model.getLimitDateTime().get(1), DateTimeUtil.STANDER_FORMAT));

            //保存任务结构
            TextContent textContent = textContentService.jsonConvertInsert(model.getPaperItems(), now, p -> {
                TaskItemObject taskItemObject = new TaskItemObject();
                taskItemObject.setExamPaperId(p.getId());
                taskItemObject.setExamPaperName(p.getName());
                return taskItemObject;
            });
            textContentService.insertByFilter(textContent);
            taskExam.setFrameTextContentId(textContent.getId());
            taskExamMapper.insertSelective(taskExam);
        } else {
            taskExam = taskExamMapper.selectByPrimaryKey(model.getId());
            modelMapper.map(model, taskExam);
            taskExam.setLimitUser(limitUser);
            List<String> limitDateTime = model.getLimitDateTime();
            taskExam.setLimitStartTime(DateTimeUtil.parse(limitDateTime.get(0), DateTimeUtil.STANDER_FORMAT));
            taskExam.setLimitEndTime(DateTimeUtil.parse(limitDateTime.get(1), DateTimeUtil.STANDER_FORMAT));

            TextContent textContent = textContentService.selectById(taskExam.getFrameTextContentId());
            //清空试卷任务的试卷Id，后面会统一设置
            List<Integer> paperIds = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemObject.class)
                    .stream()
                    .map(d -> d.getExamPaperId())
                    .collect(Collectors.toList());
            examPaperMapper.clearTaskPaper(paperIds);

            //更新任务结构
            textContentService.jsonConvertUpdate(textContent, model.getPaperItems(), p -> {
                TaskItemObject taskItemObject = new TaskItemObject();
                taskItemObject.setExamPaperId(p.getId());
                taskItemObject.setExamPaperName(p.getName());
                return taskItemObject;
            });
            textContentService.updateByIdFilter(textContent);
            taskExamMapper.updateByPrimaryKeySelective(taskExam);
        }

        //更新试卷的taskId
        List<Integer> paperIds = model.getPaperItems().stream().map(d -> d.getId()).collect(Collectors.toList());
        examPaperMapper.updateTaskPaper(taskExam.getId(), paperIds);
        model.setId(taskExam.getId());

        taskExamMapper.deleteTaskExamUserByTaskExamId(taskExam.getId());
        if (limitUser){
            // add user into exam
            List<Integer> userIds = new ArrayList<>();
            for (UserCreateVM student : model.getStudentItems())
                userIds.add(student.getId());
            taskExamMapper.insertTaskExamUser(taskExam.getId(), userIds);
        }
    }

    @Override
    public TaskRequestVM taskExamToVM(Integer id) {
        TaskExam taskExam = taskExamMapper.selectByPrimaryKey(id);
        TaskRequestVM vm = modelMapper.map(taskExam, TaskRequestVM.class);
        TextContent textContent = textContentService.selectById(taskExam.getFrameTextContentId());
        List<ExamResponseVM> examResponseVMS = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemObject.class).stream().map(tk -> {
            ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(tk.getExamPaperId());
            ExamResponseVM examResponseVM = modelMapper.map(examPaper, ExamResponseVM.class);
            examResponseVM.setCreateTime(DateTimeUtil.dateFormat(examPaper.getCreateTime()));
            return examResponseVM;
        }).collect(Collectors.toList());
        vm.setPaperItems(examResponseVMS);
        List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateFormat(taskExam.getLimitStartTime()), DateTimeUtil.dateFormat(taskExam.getLimitEndTime()));
        vm.setLimitDateTime(limitDateTime);
        if (taskExam.getLimitUser()){
            List<Integer> userIds = taskExamMapper.getTaskExamUser(id);
            if (userIds.size() != 0){
                List<User> users = userMapper.selectByIds(userIds);
                List<UserCreateVM> userCreateVMs = new ArrayList<>();
                for (User user : users)
                    userCreateVMs.add(modelMapper.map(user, UserCreateVM.class));
                vm.setStudentItems(userCreateVMs);
            }
        }
        return vm;
    }

    @Override
    public List<TaskExam> getByGradeLevel(Integer gradeLevel) {
        return taskExamMapper.getByGradeLevel(gradeLevel);
    }
}
