import http from "@/http";


// 查询文章
export const useGetAssay = (data: any) => {
  return http.post("/essay/page", data);
};

// 获取所有的标签
export const useGetAllTag = (data:any) => {
  return http.post("/sign/page",data);
};
// 最热文章
export const useGetHotAssay = (data:any) => {
  return http.post("/essay/hot",data);
};
// 最新文章
export const useGetLatestAssay = (data:any) => {
  return http.post("/essay/latest",data);
};
// 新增文章
export const useAddEssay = (data: any) => {
  return http.post("/essay", data);
};
// 新增标签
export const useAddTag = (data: any) => {
  return http.post("/sign", data);
};
// 新增浏览量
export const useAddViews = (id: string) => {
  return http.post(`/essay/view/${id}`);
};
// 根据文章id查询对应的评论
export const useGetCommentByEssayId = (essayId: string) => {
  return http.get(`/comment/${essayId}`);
};
// 新增评论
export const useAddComment = (data: any) => {
  return http.post("/comment", data);
};
// 获取验证码
export const useGetCaptcha = () => {
  return http.get("/code");
};
// 登录
export const useLogin = (data: any) => {
  return http.post("/login", data);
};