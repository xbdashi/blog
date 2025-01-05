import http from '@/http'

export interface MessagePage{
    pageNum:number,
    pageSize:number
}
export interface Message{
    userId:string,
    content:string,
}
export const useGetMessage = (page:MessagePage)=>{
    return http.post('/message/page',page)
}

export const useAddMessage = (data:Message)=>{
    return http.post('/message',data)
}