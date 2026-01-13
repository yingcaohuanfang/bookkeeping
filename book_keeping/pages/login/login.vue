<template>
	<view class="login box">
		<img src="@/static/image/logo.svg" />
		<view class="login-input">
			<view class="account">
				<p>账号</p>
				<input type="text" v-model="account" maxlength="15" placeholder="请填写账号"/>
			</view>
			<view class="password">
				<p>密码</p>
				<input type="password" v-model="password" maxlength="15" placeholder="请填写密码"/>
			</view>
		</view>
		<button @click="login">登录</button>
		
		<warn-dialog v-if="show" @click="show = false" :text="warndata"></warn-dialog>
	</view>
</template>

<script setup>
import { ref } from 'vue';
import { getLogin } from '../../api/login';
import warnDialog from '@/components/warnDialog.vue'

const account = ref('');
const password = ref('');
const show = ref(false);
const warndata = ref('');

function login(){
	if(!account.value || !password.value){
		show.value = true;
		warndata.value = '请填写账号或密码'
	}else{
		getLogin({
			username:account.value,
			password:password.value
		}).then(res => {
			if(res.code == '200'){
				localStorage.setItem('token',JSON.stringify(res.data));
				uni.switchTab({
					url:'/pages/home/home'
				})
			}else{
				show.value = true;
				warndata.value = '账号或密码错误'
			}
		})
	}
}
	
</script>

<style scoped lang="scss">
	.login{
		overflow: hidden;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: space-around;
	}
	img{
		width: 169.68rpx;
		height: 179.86rpx;
	}
	.login-input{
		width: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 33.93rpx;
		p{
			text-wrap: nowrap;
		}
		.account,.password{
			display: flex;
			border-bottom: 1.7rpx solid rgba(147, 147, 147, 1);
			padding: 17px;
			input{
				text-align: center;
			}
		}
	}
	.login-input,input{
		font-size: 34rpx;
	}
	button{
		width: 306rpx;
		height: 102rpx;
		line-height: 102rpx;
		font-size: 34rpx;
		border-radius: 34rpx;
		background-color: #00E5ED;
	}
</style>