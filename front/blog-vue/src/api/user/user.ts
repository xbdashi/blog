import http from '@/http/index'


export const useGetUser = (id:string)=>{
    return http.post(`/user/${id}`)
}