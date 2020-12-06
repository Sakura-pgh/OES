package com.mindskip.xzs.viewmodel.admin.education;

import com.mindskip.xzs.viewmodel.BaseVM;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClassifyEditRequestVM extends BaseVM {

    private Integer id;

    @NotBlank
    private String name;
}
