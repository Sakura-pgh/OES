package com.mindskip.xzs.controller.student;


import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Classify;
import com.mindskip.xzs.domain.Subject;
import com.mindskip.xzs.domain.User;
import com.mindskip.xzs.repository.ClassifyMapper;
import com.mindskip.xzs.service.ClassifyService;
import com.mindskip.xzs.service.SubjectService;
import com.mindskip.xzs.viewmodel.student.education.SubjectEditRequestVM;
import com.mindskip.xzs.viewmodel.student.education.SubjectVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController("StudentEducationController")
@RequestMapping(value = "/api/student/education")
@AllArgsConstructor
public class EducationController extends BaseApiController {

    private final SubjectService subjectService;
    private final ClassifyService classifyService;

    @RequestMapping(value = "/subject/list", method = RequestMethod.POST)
    public RestResponse<List<SubjectVM>> list() {
        User user = getCurrentUser();
        List<Subject> subjects = subjectService.getSubjectByLevel(user.getUserLevel());
        List<SubjectVM> subjectVMS = subjects.stream().map(d -> {
            SubjectVM subjectVM = modelMapper.map(d, SubjectVM.class);
            subjectVM.setId(String.valueOf(d.getId()));
            return subjectVM;
        }).collect(Collectors.toList());
        return RestResponse.ok(subjectVMS);
    }

    @RequestMapping(value = "/subject/select/{id}", method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id) {
        Subject subject = subjectService.selectById(id);
        SubjectEditRequestVM vm = modelMapper.map(subject, SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }

    @RequestMapping(value = "/classify/list", method = RequestMethod.POST)
    public RestResponse<List<Classify>> listClassify() {
        List<Classify> classifies = classifyService.allClassify();
        return RestResponse.ok(classifies);
    }
}
