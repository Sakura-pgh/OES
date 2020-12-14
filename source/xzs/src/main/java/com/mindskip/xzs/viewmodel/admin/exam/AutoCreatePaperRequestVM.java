package com.mindskip.xzs.viewmodel.admin.exam;
import lombok.Data;

@Data
public class AutoCreatePaperRequestVM {

    private Integer[] classify;

    private Integer questionRandomNum;

    private Integer difficult;

    private Integer suggestTime;
}
