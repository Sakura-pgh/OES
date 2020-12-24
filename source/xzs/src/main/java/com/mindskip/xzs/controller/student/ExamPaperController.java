package com.mindskip.xzs.controller.student;

import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.ExamPaper;
import com.mindskip.xzs.service.ExamPaperAnswerService;
import com.mindskip.xzs.service.ExamPaperService;
import com.mindskip.xzs.utility.DateTimeUtil;
import com.mindskip.xzs.utility.PageInfoHelper;
import com.mindskip.xzs.viewmodel.admin.exam.AutoCreatePaperRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.mindskip.xzs.viewmodel.admin.exam.ExamResponseVM;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperPageResponseVM;
import com.mindskip.xzs.viewmodel.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("StudentExamPaperController")
@RequestMapping(value = "/api/student/exam/paper")
@AllArgsConstructor
public class ExamPaperController extends BaseApiController {

    private final ExamPaperService examPaperService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final ApplicationEventPublisher eventPublisher;


    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<ExamPaperEditRequestVM> select(@PathVariable Integer id) {
        ExamPaperEditRequestVM vm = examPaperService.examPaperToVM(id);
        return RestResponse.ok(vm);
    }


    @RequestMapping(value = "/pageList", method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamPaperPageResponseVM>> pageList(@RequestBody @Valid ExamPaperPageVM model) {
        PageInfo<ExamPaper> pageInfo = examPaperService.studentPage(model);
        PageInfo<ExamPaperPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamPaperPageResponseVM vm = modelMapper.map(e, ExamPaperPageResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/auto_create_paper", method = RequestMethod.POST)
    public RestResponse<ExamResponseVM> auto_create_paper(@RequestBody @Valid AutoCreatePaperRequestVM model) {
        ExamResponseVM result = examPaperService.autoCreatePaper(model, getCurrentUser());
        if (result == null) return RestResponse.fail(-1, "no question");
        return RestResponse.ok(result);
    }

    @RequestMapping(value = "/auto_questions", method = RequestMethod.POST)
    public RestResponse<ExamPaperEditRequestVM> auto_questions(@RequestBody @Valid AutoCreatePaperRequestVM model) {
        ExamPaperEditRequestVM result = examPaperService.autoQuestions(model, getCurrentUser());
        if (result == null) return RestResponse.fail(-1, "no question");
        return RestResponse.ok(result);
    }

    @RequestMapping(value = "/custom_page", method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamResponseVM>> custom_page(@RequestBody ExamPaperPageRequestVM model) {
        PageInfo<ExamPaper> pageInfo = examPaperService.page(model, getCurrentUser());
        PageInfo<ExamResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamResponseVM vm = modelMapper.map(e, ExamResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }

}
