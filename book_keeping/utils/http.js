import axios from "axios";

const httpInstance = axios.create({
	baseURL:'http://192.168.1.212:9999',
})

// 添加请求拦截器
httpInstance.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
	const tokens = JSON.parse(localStorage.getItem('token')) || '';
	const token = tokens.token;
	// 传token
	config.headers.token = token;
    return config;
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  });

// 添加响应拦截器
httpInstance.interceptors.response.use(function (response) {
    // 对响应数据做点什么
	
	// if(response.data.code == 401){
	// 	localStorage.clear('usermessage');
	// 	uni.switchTab({
	// 		url:'/pages/login/login'
	// 	})
	// }
	
    return response.data;
  }, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
  });

export default httpInstance;