import http from "@/http";

export const useGetEmailCode = (data:any)=>{
    return http.post('/email',data,{timeout:30000})
}
export const useResetPwd = (data:any)=>{
    return http.post('/sys/resetPwd',data)
}


