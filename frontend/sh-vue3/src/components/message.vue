<template>
  <div>
    <div>
      <el-image style="animation: header-effect 2s"
                class="background-image"
                v-once
                lazy
                :src="$store.state.webInfo.randomCover[Math.floor(Math.random() * $store.state.webInfo.randomCover.length)]"
                fit="cover">
        <template v-slot:error>
          <div class="image-slot background-image-error"></div>
        </template>
      </el-image>
      <!-- 输入框 -->
      <div class="message-in" style="text-align: center">
        <h2 class="message-title">树洞</h2>
        <div>
          <input class="message-input"
                 type="text"
                 style="outline: none;width: 70%"
                 placeholder="留下点什么啦~"
                 v-model="messageContent"
                 @click="show = true"
                 maxlength="60"/>
          <button v-show="show"
                  style="margin-left: 12px;cursor: pointer;width: 20%"
                  @click="submitMessage"
                  class="message-input">发射
          </button>
        </div>
      </div>
      <!-- 弹幕 -->
      <div class="barrage-container">
        <vue-danmaku ref="danmakuRef" v-model:danmus="danmus" loop :style="danmaku_style"></vue-danmaku>
      </div>
    </div>
    <div class="comment-wrap">
      <div class="comment-content">
        <comment :source="$constant.source" :type="'message'" :userId="$constant.userId"></comment>
      </div>
      <myFooter></myFooter>
    </div>
  </div>
</template>

<script setup>
import {defineAsyncComponent, getCurrentInstance, onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {useStore} from "vuex";
import vueDanmaku from 'vue3-danmaku'


const danmus = ref(['danmu1', 'danmu2', 'danmu3', '...'])

const comment = defineAsyncComponent(() => import( "./comment/comment.vue"));
const myFooter = defineAsyncComponent(() => import( "./common/myFooter.vue"));

const store = useStore();


const show = ref(false)
const messageContent = ref("")
// background: {"background": "url(" + store.state.webInfo.backgroundImage + ") center center / cover no-repeat"},
// const barrageList = ref([])

const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const constant = globalProperties.$constant;
const http = globalProperties.$http;
const common = globalProperties.$common;


const danmaku_style = ref({
  color: 'red',
  height: '200px',
  width: window.innerWidth
})



onMounted(() => {
  // getTreeHole();
})

// const getTreeHole = () => {
//   http.get(constant.baseURL + "/webInfo/listTreeHole")
//       .then((res) => {
//         if (!common.isEmpty(res.data)) {
//           res.data.forEach(m => {
//             barrageList.value.push({
//               id: m.id,
//               avatar: m.avatar,
//               msg: m.message,
//               time: Math.floor(Math.random() * 5 + 10),
//               type: MESSAGE_TYPE.NORMAL,
//             });
//           });
//         }
//       })
//       .catch((error) => {
//         ElMessage.error(error.message);
//       });
// }

const submitMessage = () => {
  if (messageContent.value.trim() === "") {
    ElMessage.error("你还没写呢~");
    return;
  }

  let treeHole = {
    message: messageContent.value.trim()
  };

  if (!common.isEmpty(store.state.currentUser) && !common.isEmpty(store.state.currentUser.avatar)) {
    treeHole.avatar = store.state.currentUser.avatar;
  }

  //
  // http.post(constant.baseURL + "/webInfo/saveTreeHole", treeHole)
  //     .then((res) => {
  //       if (!common.isEmpty(res.data)) {
  //         barrageList.value.push({
  //           id: res.data.id,
  //           avatar: res.data.avatar,
  //           msg: res.data.message,
  //           time: Math.floor(Math.random() * 5 + 10)
  //         });
  //       }
  //     })
  //     .catch((error) => {
  //       ElMessage.error(error.message)
  //     });

  messageContent.value = "";
  show.value = false;
}

</script>

<style scoped>

.message-in {
  position: absolute;
  left: 50%;
  top: 40%;
  transform: translate(-50%, -50%);
  color: var(--white);
  animation: hideToShow 2.5s;
  width: 360px;
  z-index: 10;
}

.message-title {
  user-select: none;
  text-align: center;
}

.message-input {
  border-radius: 1.2rem;
  border: var(--white) 1px solid;
  color: var(--white);
  background: var(--transparent);
  padding: 10px 10px;
}

.message-input::-webkit-input-placeholder {
  color: var(--white);
}

.barrage-container {
  position: absolute;
  top: 50px;
  left: 0;
  right: 0;
  bottom: 0;
  height: calc(100% - 50px);
  width: 100%;
  user-select: none;
  overflow: hidden;
}

.comment-wrap {
  background: var(--background);
  position: absolute;
  top: 100vh;
  width: 100%;
}

.comment-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}
</style>
