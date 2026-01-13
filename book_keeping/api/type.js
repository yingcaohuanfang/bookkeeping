import httpInstance from "../utils/http";
// 获取所有种类
export function getAllType(){
	return httpInstance({
		url:'/getAllType'
	})
}
// 新增种类
export function getAddType(data = {}){
	return httpInstance({
		method:'POST',
		url:'/addType',
		data
	})
}
// 根据id获取种类
export function getTypeById(id){
	return httpInstance({
		url:'/getTypeById/' + id,
	})
}