import http from '@/http/index'


export const useGetUser = (id:string)=>{
    return http.get(`/user/${id}`)
}

export const useUpdateUser = (user:any)=>{
    return http.put("/user",user)
}