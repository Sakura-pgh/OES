import { post } from "@/utils/request";

export default {
  list: query => post("/api/admin/education/classify/list"),
  pageList: query => post("/api/admin/education/classify/list", query),
  edit: query => post("/api/admin/education/classify/edit", query),
  select: id => post("/api/admin/education/classify/select/" + id),
  deleteSubject: id => post("/api/admin/education/classify/delete/" + id)
};
