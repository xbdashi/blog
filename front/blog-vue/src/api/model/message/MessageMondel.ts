import type { User } from "../user";

export interface UserMessage {
    id?: string;
    userId?: string;
    content: string;
    createTime: string;
    user?: User;
}

export interface MessageResponse {
    code: number;
    msg: string;
    data: {
        rows: UserMessage[];
        total: number;
    }
}
