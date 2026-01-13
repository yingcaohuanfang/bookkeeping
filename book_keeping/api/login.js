import httpInstance from "../utils/http";

export function getLogin(data = {}){
	return httpInstance({
		method:'POST',
		url:'/login',
		data
	})
}