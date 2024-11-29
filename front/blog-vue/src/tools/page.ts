import { reactive } from "vue";

export const loadAllParams = reactive({
    pageNum: 1,
    pageSize: 10000,
  });
  
export const loadTop8Params = reactive({
    pageNum: 1,
    pageSize: 8,
  });