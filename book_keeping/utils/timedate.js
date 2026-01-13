export function timedate(data){
		// 空值保护
		if(!data) return '';
		
		const time = new Date(data);
		// 非法日期保护
		if(isNaN(time.getTime())) return data;
		
		const year = time.getFullYear();
		const month = (time.getMonth() + 1).toString().padStart(2,'0');
		const day = time.getDate().toString().padStart(2,'0');
		
		return `${year}年${month}月${day}日`;
	}