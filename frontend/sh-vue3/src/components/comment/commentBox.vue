<template>
  <div>
    <!-- 框 -->
    <textarea
        class="comment-textarea"
        v-model="commentContent"
        placeholder="写下点什么..."
        maxlength="1000"/>
    <!-- 按钮 -->
    <div class="myBetween" style="margin-bottom: 10px">
      <div style="display: flex">
        <div :class="{'emoji-active':showEmoji}"
             @click="showEmoji = !showEmoji">
          <el-icon class="myEmoji">
            <Orange/>
          </el-icon>
        </div>
        <div @click="openPicture()">
          <el-icon class="myPicture">
            <PictureRounded/>
          </el-icon>
        </div>
      </div>

      <div style="display: flex">
        <proButton :info="'涂鸦'"
                   v-show="!common.mobile() && !disableGraffiti"
                   @click="showGraffiti()"
                   :before="$constant.before_color_1"
                   :after="$constant.after_color_1"
                   style="margin-right: 6px">
        </proButton>
        <proButton :info="'提交'"
                   @click="submitComment()"
                   :before="$constant.before_color_2"
                   :after="$constant.after_color_2">
        </proButton>
      </div>
    </div>
    <!-- 表情 -->
    <emoji @addEmoji="addEmoji" :showEmoji="showEmoji"></emoji>

    <el-dialog title="图片"
               v-model="showPicture"
               width="25%"
               :append-to-body="true"
               :close-on-click-modal="false"
               destroy-on-close
               center>
      <div>
        <uploadPicture :prefix="'commentPicture'" @addPicture="addPicture" :maxSize="2"
                       :maxNumber="1"></uploadPicture>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {defineAsyncComponent, getCurrentInstance, ref} from "vue";
import {ElMessage} from "element-plus";
import {useStore} from "vuex";
import {Orange, PictureFilled, PictureRounded, Plus} from "@element-plus/icons-vue";

const emoji = defineAsyncComponent(() => import( "../common/emoji.vue"));
const proButton = defineAsyncComponent(() => import( "../common/proButton.vue"));
const uploadPicture = defineAsyncComponent(() => import( "../common/uploadPicture.vue"));

const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const common = globalProperties.$common


const store = useStore()

defineProps({
  disableGraffiti: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['submitComment'])


let commentContent = ref("")
const showEmoji = ref(false)
let showPicture = false

const picture = {
  name: store.state.currentUser.username,
  url: ""
}

const openPicture = () => {
  if (common.isEmpty(store.state.currentUser)) {
    ElMessage.error("请先登录！");
    return;
  }

  showPicture = true;
}


const addPicture = (res) => {
  this.picture.url = res;
  savePicture();
}

const savePicture = () => {
  let img = "[" + this.picture.name + "," + this.picture.url + "]";
  commentContent.value += img;
  picture.url = "";
  showPicture = false;
}

const addEmoji = (key) => {
  commentContent.value += key;
}

const showGraffiti = () => {
  if (common.isEmpty(store.state.currentUser)) {
    ElMessage.error("请先登录！");
    return;
  }
  commentContent.value = "";
  this.$emit("showGraffiti");
}

const submitComment = () => {
  if (common.isEmpty(store.state.currentUser)) {
    ElMessage.error("请先登录！");
    return;
  }

  if (commentContent.value.trim() === "") {
    ElMessage.warning("你还没写呢~");
    return;
  }
  emit("submitComment", commentContent.value.trim())
  // this.$emit("submitComment", commentContent.value.trim());
  commentContent.value = "";
}
</script>

<style scoped>
.comment-textarea {
  border: 1px solid var(--lightGray);
  width: 100%;
  font-size: 14px;
  padding: 15px;
  min-height: 180px;
  /* 不改变大小 */
  resize: none;
  /* 不改变边框 */
  outline: none;
  border-radius: 4px;
  background-image: var(--commentURL);
  background-size: contain;
  background-repeat: no-repeat;
  background-position: 100%;
  margin-bottom: 10px;
}

.comment-textarea:focus {
  border-color: var(--themeBackground);
}

.myEmoji {
  font-size: 18px;
  cursor: pointer;
  transition: all 0.5s;
  margin-right: 12px;
}

.myEmoji:hover {
  transform: rotate(360deg);
  font-size: 22px;
}

.myPicture {
  font-size: 18px;
  cursor: pointer;
  transition: all 0.5s;
}

.myPicture:hover {
  transform: rotate(360deg);
  font-size: 22px;
}

.emoji-active {
  color: var(--red);
}
</style>
