import { post } from "@/utils/request";

export default {
  pageList: query => post("/api/admin/task/page", query),
  edit: query => post("/api/admin/task/edit", query),
  autoCreate: query => post("/api/admin/exam/paper/auto_create_paper", query),
  select: id => post("/api/admin/task/select/" + id),
  deleteTask: id => post("/api/admin/task/delete/" + id)
};
