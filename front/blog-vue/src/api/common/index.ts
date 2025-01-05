import http from "@/http";
import type { userMondel } from "../model/userMondel";



export const useGetEmailCode = (data:userMondel)=>{
    return http.post('/sys/email',data,{timeout:30000})
}
export const useResetPwd = (data:userMondel)=>{
    return http.post('/sys/resetPwd',data)
}


