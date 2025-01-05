import type { Sign, UserVo } from "../user";

export interface EssayMondel {
    id?: string; // 对应Java的Long类型，使用number
    userId?: string;
    title?: string;
    describes?: string; // 建议改为description以符合命名规范
    content?: string;
    views?: string;
    statu?: string; // 建议改为status以符合命名规范
    likes?: string;
    commentNum?: string;
    createTime?: Date;
    updateTime?: Date;
    user?: UserVo;
    labelIds?: number[]; // 对应Java的Long数组，使用number[]
    signsList?: Sign[];
}
