<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
	*{
		margin: 0;
		padding: 0;
		box-sizing: border-box;
	}
	.box{
		width: 100vw;
		height: 100vh;
		background: linear-gradient(rgba(229, 255, 246, 1),rgba(226, 248, 216, 0.5));
	}
	.uni-tabbar-border{
		background-color: transparent !important;
		height: 0 !important;
		border: none !important;
	}
	::-webkit-scrollbar{
		display: none;
	}
</style>
