<template>
  <div class="app-container">
    <el-form
      :model="form"
      ref="form"
      label-width="100px"
      v-loading="formLoading"
      :rules="rules"
    >
      <!-- <el-form-item label="年级：" prop="gradeLevel" required>
        <el-select
          v-model="form.gradeLevel"
          placeholder="年级"
          @change="levelChange"
        >
          <el-option
            v-for="item in levelEnum"
            :key="item.key"
            :value="item.key"
            :label="item.value"
          ></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="标题：" prop="title" required>
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item label="时间限制：" required>
        <!-- <el-date-picker
          v-model="form.limitDateTime"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
      :picker-options="pickerOptions"

        > -->

        <el-date-picker
          v-model="form.limitDateTime"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="学生：" required>
        <el-table
          :data="form.studentItems"
          border
          fit
          highlight-current-row
          style="width: 100%"
        >
          <el-table-column prop="id" label="ID" width="50px" />
          <el-table-column prop="realName" label="真实姓名" width="160px" />
          <el-table-column prop="userName" label="用户名" />
          <el-table-column prop="phone" label="手机号" width="160px" />
          <el-table-column label="操作" align="center" width="160px">
            <template slot-scope="{ row }">
              <el-button
                size="mini"
                type="danger"
                @click="removeStudent(row)"
                class="link-left"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item label="试卷：" required>
        <el-table
          :data="form.paperItems"
          border
          fit
          highlight-current-row
          style="width: 100%"
        >
          <el-table-column
            prop="classifyNames"
            label="题目类型"
            width="160px"
          />
          <el-table-column prop="questionCount" label="题目数量" width="80px" />
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="createTime" label="创建时间" width="160px" />
          <el-table-column label="操作" align="center" width="160px">
            <template slot-scope="{ row }">
              <el-button
                size="mini"
                type="danger"
                @click="removePaper(row)"
                class="link-left"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button type="success" @click="addStudent">添加学生</el-button>
        <el-button type="success" @click="addPaper">添加试卷</el-button>
        <el-button type="success" @click="addRandomPaper"
          >自动生成试卷</el-button
        >
      </el-form-item>
    </el-form>

    <el-dialog :visible.sync="studentPage.showDialog" width="70%">
      <el-form :model="studentPage.queryParam" ref="queryForm" :inline="true">
        <el-form-item label="用户名：" prop="userName">
          <el-input
            v-model="studentPage.queryParam.userName"
            placeholder="用户名"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="examStudentSubmitForm"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
      <el-table
        v-loading="studentPage.listLoading"
        :data="studentPage.tableData"
        @selection-change="handleStudentSelectionChange"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column prop="id" label="ID" width="50px" />
        <el-table-column prop="realName" label="真实姓名" width="160px" />
        <el-table-column prop="userName" label="用户名" />
        <el-table-column prop="phone" label="手机号" width="160px" />
      </el-table>
      <pagination
        v-show="studentPage.total > 0"
        :total="studentPage.total"
        :page.sync="studentPage.queryParam.pageIndex"
        :limit.sync="studentPage.queryParam.pageSize"
        @pagination="studentSearch"
      />
      <span slot="footer" class="dialog-footer">
        <el-button @click="studentPage.showDialog = false">取 消</el-button>
        <el-button type="primary" @click="confirmStudentSelect">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog :visible.sync="paperPage.showDialog" width="70%">
      <el-form :model="paperPage.queryParam" ref="queryForm" :inline="true">
        <el-form-item label="学科：">
          <el-select v-model="paperPage.queryParam.subjectId" clearable>
            <el-option
              v-for="item in paperPage.subjectFilter"
              :key="item.id"
              :value="item.id"
              :label="item.name + ' ( ' + item.levelName + ' )'"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="examPaperSubmitForm"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
      <el-table
        v-loading="paperPage.listLoading"
        :data="paperPage.tableData"
        @selection-change="handleSelectionChange"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column prop="id" label="Id" width="90px" />
        <el-table-column prop="classifyNames" label="题目类型" width="160px" />
        <el-table-column prop="questionCount" label="题目数量" width="80px" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="createTime" label="创建时间" width="160px" />
      </el-table>
      <pagination
        v-show="paperPage.total > 0"
        :total="paperPage.total"
        :page.sync="paperPage.queryParam.pageIndex"
        :limit.sync="paperPage.queryParam.pageSize"
        @pagination="search"
      />
      <span slot="footer" class="dialog-footer">
        <el-button @click="paperPage.showDialog = false">取 消</el-button>
        <el-button type="primary" @click="confirmPaperSelect">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog :visible.sync="paperPage.showRandomDialog" width="70%">
      <el-form
        :model="paperPage.queryRandomParam"
        ref="queryRandomForm"
        :inline="true"
        :rules="randomRules"
      >
        <el-form-item label="题目数量" required prop="questionRandomNum">
          <el-input-number
            v-model="paperPage.queryRandomParam.questionRandomNum"
            placeholder="数量"
            :max="1000"
            controls-position="right"
            style="width: 100px"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="题目类型：" required prop="classify">
          <el-select
            v-model="paperPage.queryRandomParam.classify"
            multiple
            clearable
          >
            <el-option
              v-for="item in topics"
              :key="item.id"
              :value="item.id"
              :label="item.name"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="建议时长：" prop="suggestTime" required>
          <el-input
            v-model="paperPage.queryRandomParam.suggestTime"
            placeholder="分钟"
          />
        </el-form-item>

        <el-form-item label="难度：" prop="difficult" required>
          <el-rate
            v-model="paperPage.queryRandomParam.difficult"
            class="question-item-rate"
          ></el-rate>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="autoCreatePaper">创建</el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="paperPage.randomListLoading"
        :data="paperPage.randomTableData"
        @selection-change="handleSelectionChange"
        border
        fit
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column prop="id" label="Id" width="90px" />
        <el-table-column prop="classifyNames" label="题目类型" width="160px" />
        <el-table-column prop="questionCount" label="题目数量" width="80px" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="createTime" label="创建时间" width="160px" />
      </el-table>
      <pagination
        v-show="paperPage.total > 0"
        :total="paperPage.total"
        :page.sync="paperPage.queryParam.pageIndex"
        :limit.sync="paperPage.queryParam.pageSize"
        @pagination="search"
      />
      <span slot="footer" class="dialog-footer">
        <el-button @click="paperPage.showRandomDialog = false">取 消</el-button>
        <el-button type="primary" @click="confirmRandomPaperSelect"
          >确定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import taskApi from "@/api/task";
import userApi from "@/api/user";
import examPaperApi from "@/api/examPaper";
import Pagination from "@/components/Pagination";
import { mapGetters, mapState, mapActions } from "vuex";

export default {
  components: { Pagination },
  data() {
    let diffcultValid = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请选择难度"));
      } else {
        return callback();
      }
    };

    let countValid = (rule, value, callback) => {
      if (!value || value < 1) {
        return callback(new Error("请输入数量"));
      } else {
        return callback();
      }
    };

    return {
      form: {
        id: null,
        gradeLevel: 1,
        title: "",
        limitDateTime: "",
        studentItems: [],
        paperItems: []
      },
      formLoading: false,
      paperPage: {
        subjectFilter: null,
        multipleSelection: [],
        showDialog: false,
        showRandomDialog: false,
        queryParam: {
          subjectId: null,
          level: null,
          paperType: 6,
          pageIndex: 1,
          pageSize: 5
        },
        queryRandomParam: {
          level: 1,
          paperType: 6,
          classify: [],
          questionRandomNum: "",
          difficult: null,
          suggestTime: null,
          pageIndex: 1,
          pageSize: 5
        },
        listLoading: true,
        randomListLoading: false,
        tableData: [],
        randomTableData: [],
        total: 0
      },
      studentPage: {
        subjectFilter: null,
        multipleSelection: [],
        showDialog: false,
        showRandomDialog: false,
        queryParam: {
          userName: "",
          role: 1,
          pageIndex: 1,
          pageSize: 10
        },
        queryRandomParam: {
          level: 1,
          paperType: 6,
          classify: [],
          questionRandomNum: "",
          difficult: null,
          suggestTime: null,
          pageIndex: 1,
          pageSize: 5
        },
        listLoading: true,
        randomListLoading: false,
        tableData: [],
        randomTableData: [],
        total: 0
      },
      rules: {
        gradeLevel: [
          { required: true, message: "请输入年级", trigger: "change" }
        ],
        title: [{ required: true, message: "请输入任务标题", trigger: "blur" }]
      },

      randomRules: {
        classify: [
          {
            type: "array",
            required: true,
            message: "请选择题目类型",
            trigger: "change"
          }
        ],
        difficult: [{ validator: diffcultValid }],
        questionRandomNum: [{ validator: countValid }],
        suggestTime: [
          { required: true, message: "请输入建议时长", trigger: "change" }
        ]
      }
    };
  },
  created() {
    let _this = this;
    this.initSubject(function() {
      _this.paperPage.subjectFilter = _this.subjects;
    });

    this.initTopic();

    let id = this.$route.query.id;
    if (id && parseInt(id) !== 0) {
      _this.formLoading = true;
      taskApi.select(id).then(re => {
        _this.form = re.response;
        _this.formLoading = false;
      });
    }
  },
  methods: {
    addPaper() {
      this.paperPage.queryParam.level = this.form.gradeLevel;
      this.paperPage.showDialog = true;
      this.search();
    },
    addStudent() {
      this.studentPage.queryParam.level = this.form.gradeLevel;
      this.studentPage.showDialog = true;
      this.studentSearch();
    },
    addRandomPaper() {
      console.log("showRandomDialog");
      this.paperPage.queryParam.level = this.form.gradeLevel;
      this.paperPage.showRandomDialog = true;
      // this.search();
    },
    confirmStudentSelect() {
      this.studentPage.multipleSelection.forEach(ep =>
        this.form.studentItems.push(ep)
      );
      this.studentPage.showDialog = false;
    },
    confirmPaperSelect() {
      this.paperPage.multipleSelection.forEach(ep =>
        this.form.paperItems.push(ep)
      );
      this.paperPage.showDialog = false;
    },
    confirmRandomPaperSelect() {
      this.paperPage.multipleSelection.forEach(ep =>
        this.form.paperItems.push(ep)
      );
      this.paperPage.showRandomDialog = false;
    },
    search() {
      this.paperPage.showDialog = true;
      this.listLoading = true;
      examPaperApi.taskExamPage(this.paperPage.queryParam).then(data => {
        const re = data.response;
        this.paperPage.tableData = re.list;
        this.paperPage.total = re.total;
        this.paperPage.queryParam.pageIndex = re.pageNum;
        this.paperPage.listLoading = false;
      });
    },
    studentSearch() {
      this.studentPage.showDialog = true;
      this.listLoading = true;
      userApi.getUserPageList(this.studentPage.queryParam).then(data => {
        const re = data.response;
        this.studentPage.tableData = re.list;
        this.studentPage.total = re.total;
        this.studentPage.queryParam.pageIndex = re.pageNum;
        this.studentPage.listLoading = false;
      });
    },
    handleSelectionChange(val) {
      this.paperPage.multipleSelection = val;
    },
    handleStudentSelectionChange(val) {
      this.studentPage.multipleSelection = val;
    },
    examPaperSubmitForm() {
      this.paperPage.queryParam.pageIndex = 1;
      this.search();
    },
    examStudentSubmitForm() {
      this.studentPage.queryParam.pageIndex = 1;
      this.studentSearch();
    },
    autoCreatePaper() {
      this.paperPage.queryRandomParam.pageIndex = 1;
      this.paperPage.randomListLoading = true;

      let _this = this;
      this.$refs.queryRandomForm.validate(valid => {
        console.log("valid: ", valid);
        if (valid) {
          this.formLoading = true;
          taskApi
            .autoCreate(this.paperPage.queryRandomParam)
            .then(data => {
              if (data.code === 1 && data.response.id) {
                _this.paperPage.randomTableData = [data.response];
                //    this.search();
                // _this.$message.success(data.message);
                // _this.delCurrentView(_this).then(() => {
                //   _this.$router.push("/task/list");
                // });
              } else {
                _this.paperPage.randomTableData = [];
                _this.$message.error(data.message);
              }
              _this.formLoading = false;
              _this.paperPage.randomListLoading = false;
            })
            .catch(e => {
              _this.formLoading = false;
              _this.paperPage.randomListLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    levelChange() {
      this.paperPage.queryParam.subjectId = null;
      this.paperPage.subjectFilter = this.subjects.filter(
        data => data.level === this.form.gradeLevel
      );
    },
    removePaper(row) {
      this.form.paperItems.forEach((item, index, arr) => {
        if (item.id === row.id) {
          arr.splice(index, 1);
        }
      });
    },
    removeStudent(row) {
      this.form.studentItems.forEach((item, index, arr) => {
        if (item.id === row.id) {
          arr.splice(index, 1);
        }
      });
    },
    submitForm() {
      let _this = this;
      this.$refs.form.validate(valid => {
        if (valid) {
          this.formLoading = true;
          taskApi
            .edit(this.form)
            .then(data => {
              if (data.code === 1) {
                _this.$message.success(data.message);
                _this.delCurrentView(_this).then(() => {
                  _this.$router.push("/task/list");
                });
              } else {
                _this.$message.error(data.message);
              }
              _this.formLoading = false;
            })
            .catch(e => {
              _this.formLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    resetForm() {
      this.$refs["form"].resetFields();
    },
    subjectFormatter(row, column, cellValue, index) {
      return this.subjectEnumFormat(cellValue);
    },

    ...mapActions("exam", {
      initSubject: "initSubject",
      initTopic: "initTopic"
    }),
    ...mapActions("tagsView", { delCurrentView: "delCurrentView" })
  },
  computed: {
    ...mapGetters("enumItem", ["enumFormat"]),
    ...mapState("enumItem", {
      questionTypeEnum: state => state.exam.question.typeEnum,
      levelEnum: state => state.user.levelEnum
    }),
    ...mapGetters("exam", ["subjectEnumFormat"]),
    ...mapState("exam", {
      subjects: state => state.subjects,
      topics: state => state.topics
    })
  }
};
</script>
