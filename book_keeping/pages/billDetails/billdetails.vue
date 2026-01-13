<template>
	<view class="billdetails box">
		<view class="header">
			<rollback></rollback>
			<h3>账单详情</h3>
			<p class="pass">已通过</p>
		</view>
		<scroll-view class="content" scroll-y="true">
			<ul>
				<li class="personnel">
					<text>人员</text>
					<view class="personnel-name">
						<view class="image"><img src="" /></view>
						<p>xxxxx</p>
					</view>
				</li>
				<li class="amount">
					<text>金额（元）</text>
					<p class="number">123</p>
					<p class="amount-state income">收入</p>
				</li>
				<li class="billtime">
					<text>账单日期</text>
					<p>1111年1月1日</p>
				</li>
				<li class="type">
					<text>种类</text>
					<p>食品饮料</p>
				</li>
				<li class="date">
					<text>日期</text>
					<p>1111年11月1日 1:1:1</p>
				</li>
				<li class="remark">
					<text>备注</text>
					<textarea auto-height></textarea>
				</li>
				<li class="billimg">
					<text>发票</text>
					<view></view>
				</li>
			</ul>
		</scroll-view>
	</view>
</template>

<script setup>
	import rollback from '@/components/rollback.vue'
</script>

<style scoped lang="scss">
	.billdetails{
		padding: 85rpx;
		display: flex;
		flex-direction: column;
	}
	
	.header{
		height: 85rpx;
		display: flex;
		justify-content: space-between;
		text-wrap: nowrap;
		margin-bottom: 58px;
		
		h3{
			font-size: 57.8rpx;
			font-weight: 400;
		}
		p{
			display: flex;
			align-items: center;
			justify-content: center;
			height: 100%;
			padding: 8.5rpx 15px;
			font-size: 40.8rpx;
			border-radius: 999rpx;
			color: white;
		}
		.pass{
			background-color: #00A86B;
		}
		.review{
			background-color: #00E5ED;
		}
	}
	.content{
		flex: 1;
		background-color: white;
		border-radius: 34rpx;
		overflow: hidden;
		font-size: 34rpx;
		text{
			font-size: 34rpx;
		}
		ul{
			display: flex;
			flex-direction: column;
			gap: 34rpx;
			padding: 17rpx;
		}
		p{
			color: #6D6B6B;
		}
		.personnel,.personnel-name,.amount,.billtime,.type,.date{
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
		.personnel-name{
			gap: 34rpx;
			.image{
				width: 64.6rpx;
				height: 64.6rpx;
				background-color: #C4C4C4;
				border-radius: 999rpx;
				overflow: hidden;
				img{
					width: 100%;
					height: 100%;
				}
				
			}
		}
		.amount{
			.amount-state{
				font-size: 23.8rpx;
				padding: 8.5rpx 25.5rpx;
				color: white;
				border-radius: 999rpx;
			}
			.income{
				background-color: #00A86B;
			}
			.expenditure{
				background-color: #FD3C4A;
			}
		}
		.type{
			p{
				color: black;
			}
		}
		.date{
			color: #6D6B6B;
		}
		.remark,.billimg{
			display: flex;
			flex-direction: column;
			gap: 17rpx;
		}
		.remark{
			textarea{
				width: 100%;
				color: #6D6B6B;
				padding: 8.5rpx 17rpx;
				border: 1.7rpx solid #DDDDDD;
			}
		}
		.billimg{
			view{
				width:100%;
				min-height: 204rpx;
				border: 1.7rpx dashed #DDDDDD;
			}
		}
	}
</style>