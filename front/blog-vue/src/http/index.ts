import router from "@/router";
import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse, type InternalAxiosRequestConfig } from "axios";
import { ElMessage } from "element-plus";
const consfig = {
    baseURL: "/api",
    timeout: 5000,
};
export interface Result<T = any>{
    code: number;
    msg:string;
    data: T;
}
class Http{
    private instance: AxiosInstance;
    // 初始化
    constructor(configs: AxiosRequestConfig) {
        this.instance = axios.create(configs);
        this.interceptors();
    }
     // 请求拦截器,返回请求结果
    private interceptors(){
        this.instance.interceptors.request.use(
            (config:InternalAxiosRequestConfig) => {
                console.log("进入请求拦截器")
                // 获取token
                let token = sessionStorage.getItem("token");
                if(token && config){
                    config.headers!['token'] = token;
                }
                return config;
            },
            (err:any) => {
                ElMessage.error(
                    '请求失败'
                )
                return err;
            }
        );
        // 响应拦截器
        this.instance.interceptors.response.use(
            (res:AxiosResponse) => {
                console.log('进入响应拦截器')
                if(res.data.code === 401){
                    ElMessage.warning(res.data.msg)
                    router.push("/login")
                    return
                }
                if(res.data.code == 200){
                    return res.data;
                }else{
                    ElMessage.error(res.data.msg || '接口报错')
                    // 这里就会跳到异常
                    return Promise.reject(res.data.msg || '接口报错')
                }
            },
           (err:any) => {
                err.data = {}
                err.data.msg = '服务器异常请联系管理员'
                return err;
           }
        )
    }

    // post请求
    post<T = Result>(url:string,data?:object):Promise<T>{
        return this.instance.post(url,data)
    }
    // put请求
    put<T = Result>(url:string,data?:object):Promise<T>{
        return this.instance.put(url,data)
    }
     // get
     get<T = Result>(url:string,parmas?:object):Promise<T>{
        return this.instance.get(url, {params: parmas})
    }
    // get<T = Result>(url: string, params?:object):Promise<T> {
    //     return this.instance.get(url, { params });
    // }
    // put请求
    delete<T = Result>(url:string):Promise<T>{
        return this.instance.delete(url)
    }
    // 图片上传
    upload<T = Result>(url:string,data?:object):Promise<T>{
        return this.instance.post(url,data,{
            headers:{
                'Content-Type':'multipart/form-data'
            }
        })
    }
}

export default new Http(consfig);