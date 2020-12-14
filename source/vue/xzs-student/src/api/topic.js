import { post } from "@/utils/request";

export default {
  list: query => post("/api/student/education/classify/list"),
  pageList: query => post("/api/student/education/classify/list", query),
  edit: query => post("/api/student/education/classify/edit", query),
  select: id => post("/api/student/education/classify/select/" + id),
  deleteSubject: id => post("/api/student/education/classify/delete/" + id)
};
