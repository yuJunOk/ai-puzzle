<template>
	<view class="container">
		<scroll-view scroll-y="true" class="chat-view">
			<view v-for="(chat, index) in chatList">
				<view class="chat left-chat" :id="'chat-' + index" v-if="chat.isAI">
					<image :src="aiAvatar" mode="scaleToFill"></image>
					<text selectable="true" user-select="true">{{chat.content}}</text>
				</view>
				<view class="chat right-chat" :id="'chat-' + index" v-else>
					<text selectable="true" user-select="true">{{chat.content}}</text>
					<image :src="meAvatar" mode="scaleToFill"></image>
				</view>
			</view>
			<view class="chat left-chat" v-if="sendingFlag">
				<image :src="aiAvatar" mode="scaleToFill"></image>
				<l-loading class="loading" size="26rpx" />
			</view>
		</scroll-view>
		<view class="operate-view" v-if="startFlag">
			<textarea auto-height placeholder="请输入发送消息" v-model="inputMessage" />
			<button type="primary" @click="sendMessage" :disabled="sendingFlag">
				<uni-icons type="paperplane" size="36" color="#fff"></uni-icons>
			</button>
		</view>
		<view class="operate-view" v-else>
			<button type="primary" @click="startChat">
				开始游戏
			</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				aiAvatar: '/static/aiAvatar.png',
				meAvatar: '/static/meAvatar.png',
				sendingFlag: false,
				startFlag: false,
				chatList: [],
				inputMessage: "",
				scrollToView: '',
			}
		},
		methods: {
			startChat(){
				this.inputMessage = "开始";
				this.sendMessage();
			},
			getHistoryMessages() {
				let scope = this;
				this.chatList = [];
				this.startFlag = false;
				uni.request({
					url: 'http://localhost:8080/api/ai/123/history',
					success: (res) => {
						res = res.data;
						if (res.code == 23200) {
							if (!Array.isArray(res.data) || res.data.length == 0){
								return;
							}
							scope.startFlag = true;
							for (let i = 0; i < res.data.length; i++) {
								let message = res.data[i];
								if (message.role === 'system') {
									continue;
								}
								let isAI = false;
								if (message.role === 'user') {
									isAI = false;
								}
								if (message.role === 'assistant') {
									isAI = true;
								}
								scope.chatList.push({
									isAI: isAI,
									content: message.content
								})
							}
							scope.scrollToBottom();
						} else {
							uni.showToast({
								title: res.message,
								icon:'none'
							});
						}
					},
					fail() {
						uni.showToast({
							title: '网络异常，请稍后再试',
							icon: 'error',
						});
					}
				});
			},
			sendMessage() {
				if (this.inputMessage.trim().length == 0) {
					uni.showToast({
						title: '请输入提问内容',
						icon: 'error',
					});
					return;
				}
				let scope = this;
				this.sendingFlag = true;
				this.chatList.push({
					content: this.inputMessage,
					isAI: false
				})
				uni.request({
					url: 'http://localhost:8080/api/ai/123/send',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						message: scope.inputMessage
					},
					success: (res) => {
						res = res.data;
						if (res.code == 23200) {
							scope.chatList.push({
								content: res.data,
								isAI: true
							});
							scope.inputMessage = "";
							scope.startFlag = !res.data.includes('游戏已结束');
							scope.scrollToBottom();
						} else {
							uni.showToast({
								title: res.message,
								icon:'none'
							});
						}
					},
					fail() {
						uni.showToast({
							title: '网络异常，请稍后再试',
							icon: 'error',
						});
					},
					complete() {
						scope.sendingFlag = false;
					}
				});
			},
			scrollToBottom() {
				this.scrollToView = 'chat' + (this.chatList.length - 1)
			}
		},
		mounted() {
			this.getHistoryMessages();
		}
	}
</script>

<style>
	.chat-view {
		flex: auto;
		box-sizing: border-box;
		padding: 10rpx;
		background-color: #eee;
	}

	.chat-view ::-webkit-scrollbar {
		width: 0;
		height: 0;
		background-color: transparent;
	}

	.operate-view {
		display: flex;
		box-sizing: border-box;
		padding: 10rpx;
		background-color: rgb(248, 248, 248);
	}

	.operate-view textarea {
		height: 100%;
		width: 88%;
		box-sizing: border-box;
		padding: 10rpx;
		border: 2rpx solid #ccc;
		border-radius: 6rpx;
		background-color: #fff;
	}

	.operate-view button {
		flex: auto;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-left: 10rpx;
		border-radius: 6rpx;
	}

	.chat {
		display: flex;
		align-items: flex-start;
		margin-bottom: 22rpx;
	}

	.chat image {
		height: 66rpx;
		width: 66rpx;
		border-radius: 6rpx;
	}

	.chat text,
	.loading {
		max-width: 60%;
		padding: 16rpx;
		border-radius: 6rpx;
		background-color: #fff;
		font-size: large;
		word-break: break-all;
		word-wrap: break-word;
	}

	.left-chat text,
	.loading {
		margin-left: 10rpx;
	}

	.right-chat {
		justify-content: end;
	}

	.right-chat text {
		margin-right: 10rpx;
		background-color: lawngreen;
	}
</style>