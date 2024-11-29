import type { DialogModel } from "@/type/BaseType";
import { reactive } from "vue";

// 封装属性
export default function useDialog(){
    const dialog = reactive<DialogModel>({
        title:'',
        width:630,
        height:280,
        visible:false
    })
    const show = ()=>{
        dialog.visible=true
    }
    const onClose = ()=>{
        dialog.visible=false
    }
    const onConfirm = ()=>{
        dialog.visible=false
    }
    // 返回回去
    return {
        dialog,
        show,
        onClose,
        onConfirm
    }
}