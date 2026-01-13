import httpInstance from "../utils/http";
// 获取当前用户的所有账单
export function getBills(){
	return httpInstance({
		url:'/getBills'
	})
}
// 筛选账单
export function getQueryBills(data = {}){
	return httpInstance({
		url:'/queryBills',
		method:'POST',
		data
	})
}
// 添加账单
export function getaddBills(data = {}){
	return httpInstance({
		url:'/addBills',
		method:'POST',
		data
	})
}
// 上传图片
export function getupimg(data = {}){
	return httpInstance({
		url:'/file/upload',
		method:'POST',
		data
	})
}