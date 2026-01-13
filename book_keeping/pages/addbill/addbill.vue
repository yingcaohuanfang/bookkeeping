<template>
	<view class="addbill box">
		<view class="header">
			<rollback></rollback>
			<h3>添加账单</h3>
			<h3 v-if="false">账单详情</h3>
			<p class="state" v-if="false">驳回</p>
		</view>
		<view class="content">
			<scroll-view scroll-y="true">
				<ul>
					<li class="amount">
						<view class="amount-header">
							<text>金额（元）</text>
							<view class="amount-state" :class="amountstate == '0' ? 'income' : 'expenditure'" @click="amountdialog = !amountdialog">
								<p ref="amountstateref">收入</p>
								<img src="@/static/image/amount-icon.svg" />
							</view>
							<!-- 金额类型弹窗 -->
							<view class="amount-state-dialog" v-if="amountdialog">
								<p class="selectstate" v-for="(item,index) in amountStateList" :class="item.color"
								 @click="selectamountstate(index)" ref="selectAmountStateRef">
									{{item.text}}
								</p>
							</view>
						</view>
						<input type="text" placeholder="请输入" v-model="amountinput" />
					</li>
					<li class="date">
						<text>日期</text>
						<picker mode="date" :value="date" fields="day" @change="datechange">
							<view>{{timedate(date)}}</view>
							<img src="@/static/image/date.svg" />
						</picker>
					</li>
					<li class="type">
						<text>种类</text>
						<view class="type-select" @click="typedialog = !typedialog">
							<p ref="typeref">食品饮料</p>
							<img src="@/static/image/down.svg" />
						</view>
						<!-- 种类弹窗 -->
						<view class="type-dialog" v-if="typedialog">
							<scroll-view scroll-y="true">
								<view class="typelists">
									<p v-for="item in typedata" @click="selecttype(item)">
										{{item.typename}}
									</p>
								</view>
							</scroll-view>
							<view class="addtype">
								<img src="@/static/image/addicon.svg" />
								<text @click="addtype">添加分类</text>
							</view>
								<cover-view>1111</cover-view>
						</view>
					</li>
					<li class="remark">
						<text>备注</text>
						<textarea auto-height placeholder="请输入" :value="remarktext"></textarea>
					</li>
					<li class="image">
						<text>图片</text>
						
						<view class="addimg" @click="addimg">
							<img src="@/static/image/addicon.svg" />
							<text>添加图片</text>
						</view>
					</li>
				</ul>
				<button @click="addbill">确定</button>
			</scroll-view>
		</view>
	</view>
</template>

<script setup>
	import { onMounted, ref } from 'vue';
	import rollback from '@/components/rollback.vue';
	import { timedate } from '../../utils/timedate';
	import { getAllType,getAddType } from '../../api/type';
	import { getaddBills,getupimg } from '../../api/home';
	
	const amountdialog = ref(false);	//金额弹窗的显示和隐藏
	const date = ref('');	//日期
	const typedialog = ref(false);	//种类弹窗的显示和隐藏
	const amountinput = ref('');	//金额
	const remarktext = ref('');		//备注
	const typedata = ref('');	//所有种类数据
	
	const amountstate = ref('0'); 	//金额状态
	const amountstateref = ref();	//金额状态ref
	const selectAmountStateRef = ref(); 	//选择金额状态ref
	const typeref = ref();	//种类文本的ref
	
	const amountStateList = ref([
		{text:'收入',color:'income'},
		{text:'支出',color:'expenditure'},
	])
	
	// 获取所有种类
	function getAllTypeApi(){
		getAllType().then(res => {
			typedata.value = res.data;
		})
	}
	// 更换金额类型
	function selectamountstate(index){
		amountstate.value = index;
		amountstateref.value.innerHTML = selectAmountStateRef.value[index].innerHTML
		amountdialog.value = false;
	}
	// 选择日期
	function datechange(e){
		date.value = e.detail.value
	}
	// 选择种类
	function selecttype(item){
		typeref.value.innerHTML = item.typename;
		typedialog.value = false;
	}
	// 添加种类
	function addtype(){
		
	}
	// 添加图片
	function addimg(){
		
	}
	// 添加账单
	function addbill(){
		
	}
	
	onMounted(() => {
		getAllTypeApi();
	})
</script>

<style scoped lang="scss">
	.addbill{
		padding: 85rpx;
		display: flex;
		flex-direction: column;
		gap: 17rpx;
	}
	.header{
		height: 85rpx;
		display: flex;
		justify-content: center;
		text-wrap: nowrap;
		position: relative;
		h3{
			font-size: 57.8rpx;
			font-weight: 400;
		}
		.rollback{
			position: absolute;
			top: 50%;
			left: 0;
			transform: translate(0, -50%);
		}
	}
	.content{
		flex: 1;
		background-color: white;
		border-radius: 34rpx;
		padding: 34rpx;
		overflow: hidden;
		scroll-view{
			height: 100%;
		}
		ul{
			display: flex;
			flex-direction: column;
			gap: 34rpx;
			list-style: none;
			color: #666666;
			font-size: 34rpx;
		}
		.amount,.date,.type,.remark,.image{
			display: flex;
			flex-direction: column;
			gap: 17rpx;
		}
		.amount{
			.amount-header{
				display: flex;
				justify-content: space-between;
				position: relative;
				.income{
					background-color: #00A86B;
				}
				.expenditure{
					background-color: #FD3C4A;
				}
				.amount-state{
					display: flex;
					align-items: center;
					gap: 25.5rpx;
					color: white;
					font-size: 23.8rpx;
					border-radius: 999rpx;
					padding: 8.5rpx 17rpx;
					img{
						width: 18.7rpx;
						height: 30.6rpx;
					}
				}
				.amount-state-dialog{
					position: absolute;
					top: 100%;
					right: 0;
					z-index: 1;
					border-radius: 34rpx;
					padding: 20.4rpx 11.9rpx;
					background-color: #E6E6E6;
					font-size: 23.8rpx;
					color: white;
					margin-top: 8.5rpx;
					.selectstate{
						padding: 8.5rpx 42.5rpx;
						border-radius: 34rpx;
					}
					.income{
						background-color: #00A86B;
						margin-bottom: 17rpx;
					}
					.expenditure{
						background-color: #FD3C4A;
					}
				}
			}
			input{
				width: 100%;
				height: 85rpx;
				padding-left: 17rpx;
				font-size: 40.8rpx;
				border: 1.7rpx solid #DDDDDD;
				border-radius: 17rpx;
			}
		}
		.date{
			picker{
				display: flex;
				align-items: center;
				// justify-content: space-between;
				position: relative;
				padding: 0 17rpx;
				width: 100%;
				height: 85rpx;
				border: 1px solid #DDDDDD;
				border-radius: 17rpx;
				img{
					width:34rpx;
					height: 34rpx;
					position: absolute;
					right: 17rpx;
					top: 30%;
				}
			}
		}
		.type{
			color: black;
			position: relative;
			text{
				color: #666666;
			}
			.type-select{
				display: flex;
				align-items: center;
				justify-content: space-between;
				height: 85rpx;
				border: 1px solid #DDDDDD;
				border-radius: 17rpx;
				padding: 17rpx 34rpx;
			}
			.type-dialog{
				width:100%;
				position: absolute;
				top: 100%;
				margin-top: 17rpx;
				background-color: #EFEFEF;
				border-radius: 34rpx;
				padding: 17rpx 25.5rpx;
				z-index: 1;
				scroll-view{
					max-height: 180px;
					overflow: hidden;
					.typelists{
						display: flex;
						flex-wrap: wrap;
						gap: 25.5rpx;
						font-size: 40.8rpx;
					}
					p{
						width: 204rpx;
						height: 85rpx;
						display: flex;
						align-items: center;
						justify-content: center;
						background-color: white;
						border-radius: 34rpx;
						padding: 17rpx 34rpx;
					}
				}
				.addtype{
					border:1.7rpx dashed #6D6B6B;
					margin-top: 17rpx;
					display: flex;
					align-items: center;
					padding: 8.5rpx;
					border-radius: 17rpx;
					img{
						width: 54.4rpx;
						height: 54.4rpx;
						margin-right: 170rpx;
					}
				}
			}
		}
		.remark{
			textarea{
				width: 100%;
				height: 85rpx;
				font-size: 40.8rpx;
				border: 1.7rpx solid #DDDDDD;
				border-radius: 17rpx;
				padding: 8.5rpx 17rpx;
			}
		}
		.image{
			.addimg{
				border:1.7rpx dashed #6D6B6B;
				margin-top: 17rpx;
				display: flex;
				align-items: center;
				padding: 8.5rpx;
				border-radius: 17rpx;
				img{
					width: 54.4rpx;
					height: 54.4rpx;
					margin-right: 170rpx;
				}
			}
		}
		button{
			width: 418.2rpx;
			height: 85rpx;
			border-radius: 17rpx;
			background-color: #A6E4A5;
			margin-top: 102rpx;
			font-size: 47.6rpx;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
</style>