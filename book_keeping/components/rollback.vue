<template>
	<view class="rollback" @click="rollback">
		<img src="@/static/image/rollback.svg" />
	</view>
</template>

<script setup>
	function rollback(){
		let cannavback = getCurrentPages();
		if(cannavback && cannavback.length > 1){
			// uni.navigateBack({
			// 	delta:1
			// })
			uni.navigateBack()
		}else{
			history.back();
		}
	}
</script>

<style scoped>
	img{
		width: 25.5rpx;
		height: 100%;
	}
</style>