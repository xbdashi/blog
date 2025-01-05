export interface User {
    id?: string;
    username?: string;
    password?: string;
    email?: string;
    photo?: string;
    phone?: string;
    isIdentity?: string;
    provinceId?: number;
    cityId?: string;
    distractId?: string;
    isDelete?: number;
    qq?: string;
    wchat?: string;
    isLogin?: number;
    version?: number;
    fans?: string;
    focus?: string;
    likes?: string;
    createTime?: Date;
    updateTime?: Date;
}

export interface UserVo extends User {
    fanIds?: number[];
}
export interface Sign {
    id?: number; // 对应Java的Long类型，使用number
    label?: string; // 标签名
    type?: string; // 展示的颜色
    // count字段被@JsonIgnore注解忽略，因此不包含在TypeScript接口中
}
