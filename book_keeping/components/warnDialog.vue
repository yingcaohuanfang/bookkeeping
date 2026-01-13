<template>
	<view class="warn">
		<view class="content" @click.stop>
			<p>{{prop.text}}</p>
		</view>
	</view>
</template>

<script setup>
	const prop = defineProps({
		text:String
	})
</script>

<style scoped>
.warn{
	width: 100vw;
	height: 100vh;
	position: absolute;
	top: 0;
	left: 0;
	display: flex;
	align-items: center;
	justify-content: center;
}
.content{
	padding: 20px;
	background-color: white;
	border-radius: 10px;
	box-shadow: 0 0 5px 5px rgba(0, 0, 0, .3);
}
</style>