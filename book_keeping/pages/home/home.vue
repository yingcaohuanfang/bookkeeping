<template>
	<view class="home box">
		<view class="header">
			<scroll-view scroll-x="true">
				<view class="screen-lists">
					<p v-for="item in screenlist" :class="item.style">{{item.typename}}</p>
				</view>
			</scroll-view>
			<img src="@/static/image/screen-icon.svg" @click="showDialog = !showDialog" />
			<!-- 筛选弹窗 -->
			<view class="screen-dialog" v-if="showDialog">
				<view class="input">
					<input type="text" v-model="screeninput" placeholder="请输入关键字" />
					<img src="@/static/image/screen.svg" @click="screen" />
				</view>
				<view class="date">
					<h3>日期：</h3>
					<view class="datepicker">
						<picker class="startDate" mode="date" :value="startDate" @change="startDatechange">
							<view class="picker">{{timedate(startDate)}}</view>
						</picker>
						<view class="link"></view>
						<picker class="endDate" mode="date" :value="endDate" @change="endDatechange">
							<view class="picker">{{timedate(endDate)}}</view>
						</picker>
					</view>
				</view>
				<view class="screen-state">
					<h3>状态：</h3>
					<view class="screen-state-list">
						<!-- <p class="state" :class="{'pass':screenState == '0'}" @click="screenstate('0')">
							已通过
						</p>
						<p class="state" :class="{'reject':screenState == '1'}" @click="screenstate('1')">
							驳回
						</p>
						<p class="state" :class="{'review':screenState == '2'}" @click="screenstate('2')">
							待审核
						</p> -->
						<p class="state" v-for="item in statedata" :class="item.select ? item.style : ''" @click="screenstate(item)">
							{{item.typename}}
						</p>
					</view>
				</view>
				<view class="kind-box">
					<h3>种类：</h3>
					<scroll-view scroll-y="true">
						<view class="kind-list">
							<view class="kind" :class="{'kind-select':item.select}" 
							v-for="item in typedata" :key="item.id" @click="selectType(item)">
								{{item.typename}}
							</view>
						</view>
					</scroll-view>
				</view>
			</view>
		</view>
		
		<scroll-view class="bill-lists" scroll-y="true">
			<view class="isnull" v-if="billdata == ''">暂无数据</view>
			<view class="bill" v-for="(item,index) in billdata" :key="item.id" @click="billDetails">
				<view class="bill-top">
					<h3>
						{{getTypeName(item.typeid)}}
					</h3>
					<view class="price-state">
						<text class="bill-price">{{item.amount}}元</text>
						<text :class="item.status == 0 ? 'bill-review' : item.status == 1 ? 'bill-pass' : 'bill-reject'">
							{{item.status == 0 ? '待审核' : item.status == 1 ? '已通过' : '驳回'}}
						</text>
					</view>
				</view>
				<view class="bill-bottom">
					<text>{{item.remark}}</text>
					<text>{{timedate(item.updatedTime)}}</text>
				</view>
			</view>
		</scroll-view>
		<view class="add-button" @click="addbill">
			<img src="@/static/image/add.svg" />
		</view>
	</view>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { timedate } from '../../utils/timedate';
import { getBills,getQueryBills } from '../../api/home';
import { getAllType,getTypeById } from '../../api/type';

// 0审核,1通过,2驳回
const showDialog = ref(false);	//筛选弹窗的出现或隐藏
const screenlist = computed(() => {
	const a = statedata.value.filter(item => item.select == true);
	const b = typedata.value.filter(item => item.select == true);
	return [...a,...b]
	// return statedata.value.filter(item => item.select == true);
});
const startDate = ref('');
const endDate = ref('');
const billdata = ref('');	//账单数据
const statedata = ref([
	{id:0,typename:'已通过',style:'pass',select:false},
	{id:1,typename:'驳回',style:'reject',select:false},
	{id:2,typename:'待审核',style:'review',select:false},
])
const typedata = ref([]);	//种类数据
const screeninput = ref('');
const typemap = ref({});	//种类id-->名称的映射

// 筛选条件
const screendata = ref({
	keywords:'',
	pageNum: 1,
	pageSize: 10,
	statusList: [],
	typeIdList: [],
	startTime:'',
	endTime:''
})

// 获取用户账单数据
function getBillsApi(){
	getBills().then(res => {
		billdata.value = res.data;
	})
}
// 获取所有种类
function getAllTypeApi(){
	getAllType().then(res => {
		typedata.value = res.data;
		res.data.forEach(item => typemap.value[item.id] = item.typename);
		typedata.value.map((item) => item.select = false);
	})
}
// 根据id获取种类
function getTypeName(id){
	return typemap.value[id];
}
// 起始时间
function startDatechange(e){
	startDate.value = e.detail.value;
}
// 结束时间
function endDatechange(e){
	endDate.value = e.detail.value;
}
// 筛选
function screen(){
	screendata.value.keywords = screeninput.value;
	screendata.value.startTime = startDate.value;
	screendata.value.endTime = endDate.value;
	if(screenlist.value.length == 0 && !screeninput.value && !startDate.value && !endDate.value){
		getBillsApi();
	}else{
		getQueryBills(screendata.value).then(res => {
			billdata.value = res.data.records;
			screeninput.value = '';
		})
	}
	showDialog.value = false;
}
// 选中筛选弹窗里的状态
function screenstate(item){
	item.select = !item.select;
	
	screendata.value.statusList = statedata.value.filter(item => item.select == true).map(item => item.id);
}
// 选中筛选弹窗里的种类
function selectType(item){
	item.select  = !item.select;
	screendata.value.typeIdList = typedata.value.filter(item => item.select == true).map(item => item.id)
}
// 转到账单详情
function billDetails(){
	uni.navigateTo({
		url:'/pages/billDetails/billdetails'
	})
}
// 添加账单
function addbill(){
	uni.navigateTo({
		url:'/pages/addbill/addbill'
	})
}

onMounted(() => {
	getBillsApi();
	getAllTypeApi();
})
</script>

<style scoped lang="scss">
	.home{
		height: calc(100vh - 102rpx);
		padding: 51rpx;
		position: relative;
		display: flex;
		flex-direction: column;
	}
	
	.header{
		display: flex;
		margin-bottom: 68rpx;
		position: relative;
		scroll-view{
			overflow: hidden;
			.screen-lists{
				display: flex;
				gap: 8.5rpx;
				white-space: nowrap;
				p{
					display: flex;
					align-items: center;
					justify-content: center;
					height: 85rpx;
					padding:17rpx 34rpx;
					font-size: 34rpx;
					color: white;
					border-radius: 99px;
					background-color: #FFBD74;
				}
				.pass{
					background-color: #00A86B;
				}
				.reject{
					background-color: #FD3C4A;
				}
				.review{
					background-color: #00E5ED;
				}
			}
		}
		img{
			width: 85rpx;
			height: 85rpx;
		}
	}
	.screen-dialog{
		width: 100%;
		background-color: white;
		position: absolute;
		top: 100%;
		z-index: 1;
		border-radius: 34rpx;
		margin-top: 17rpx;
		padding: 17rpx 34rpx;
		display: flex;
		flex-direction: column;
		gap: 17rpx;
		box-shadow: 0 0 17rpx 8.5rpx rgba(0, 0, 0, 0.1);
		h3{
			font-size: 40.8rpx;
			font-weight: 500;
			margin-bottom: 25.5rpx;
		}
		.input{
			display: flex;
			align-items: center;
			gap: 17rpx;
			background-color: #EEEEEE;
			border-radius: 17rpx;
			position: relative;
			input{
				font-size: 34rpx;
				height: 85rpx;
				padding-left: 34rpx;
			}
			icon{
				width:34rpx;
				height: 34rpx;
				font-size: 34rpx;
				position: absolute;
				right: 34rpx;
			}
			img{
				width:34rpx;
				height: 34rpx;
				position: absolute;
				right: 34rpx;
			}
		}
		.date{
			width: 100%;
			.datepicker{
				width: 100%;
				display: flex;
				align-items: center;
				justify-content:space-between;
				gap: 17rpx;
				.link{
					width: 34rpx;
					height: 1px;
					background-color: black;
				}
				.picker{
					display: flex;
					align-items:center;
					justify-content: center;
					width:239.25rpx;
					padding: 0 17rpx;
					height: 59.4rpx;
					font-size: 25.45rpx;
					color: #666666;
					border: 1.7rpx solid #DDDDDD;
				}
			}
		}
		.screen-state{
			.screen-state-list{
				display: flex;
				gap: 27.2rpx;
				.state{
					display: flex;
					align-items: center;
					justify-content: center;
					width: 170rpx;
					height: 85rpx;
					font-size: 34rpx;
					padding: 8.5rpx 17rpx;
					background-color: #8D8D8D;
					color: white;
					border-radius: 99px;
				}
				.pass{
					background-color: #00A86B;
				}
				.reject{
					background-color: #FD3C4A;
				}
				.review{
					background-color: #00E5ED;
				}
			}
		}
		.kind-box{
			scroll-view{
				height: 204rpx;
				overflow: hidden;
			}
			.kind-list{
				display: flex;
				gap: 17rpx;
				flex-wrap: wrap;
				.kind{
					display: flex;
					align-items: center;
					justify-content: center;
					width: fit-content;
					padding: 8.5rpx 25.5rpx;
					height: 85rpx;
					font-size: 34rpx;
					background-color: #B3B3B3;
					border-radius: 99px;
					color: white;
				}
				.kind-select{
					background-color: #FFBD74;
				}
			}
		}
	}
	
	.bill-lists{
		overflow: hidden;
		.isnull{
			text-align: center;
			font-size: 34rpx;
			color: #DDDDDD;
		}
		.bill,.bill-top,.bill-bottom{
			display: flex;
			justify-content: space-between;
		}
		.bill{
			flex-direction: column;
			width: 100%;
			height: 170rpx;
			border-radius: 34rpx;
			padding: 34rpx 25.5rpx;
			background-color: white;
			margin-bottom: 34rpx;
			h3{
				font-size: 34rpx;
			}
			.bill-top{
				font-size: 27.2rpx;
				.bill-price{
					margin-right: 34rpx;
					color: #FD3C4A;
				}
			}
			.bill-bottom{
				font-size: 22.1rpx;
				color: #91919F;
			}
		}
		.bill-pass{
			color: #00A86B;
		}
		.bill-reject{
			color: #FD3C4A;
		}
		.bill-review{
			color: #00E5ED;
		}
	}
	
	.add-button{
		width: 170rpx;
		height: 170rpx;
		position:absolute;
		right: 34rpx;
		bottom: 85rpx;
		background-color: white;
		padding: 42.5rpx;
		border-radius: 999px;
		display: flex;
		align-items: center;
		justify-content: center;
		img{
			width: 85rpx;
			height: 85rpx;
		}
	}
</style>