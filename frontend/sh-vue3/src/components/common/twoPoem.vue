<template>
  <div class="poem-container myCenter my-animation-hideToShow"
       v-if="!common.isEmpty(guShi.origin) || !common.isEmpty(hitokoto.hitokoto)">
    <!-- 背景图片 -->
    <el-image class="my-el-image poem-image"
              style="position: absolute;margin-top: -50px"
              v-once
              lazy
              :src="store.state.webInfo.randomCover[Math.floor(Math.random() * store.state.webInfo.randomCover.length)]"
              fit="cover">
      <template v-slot:error>
        <div class="image-slot"></div>
      </template>
    </el-image>
    <div class="poem-wrap">
      <div v-if="props.isShehui"><span>鬼畜全明星</span></div>
      <div v-else><span>{{ props.isHitokoto ? hitokoto.from : guShi.origin }}</span></div>
      <p class="poem">{{ props.isHitokoto ? hitokoto.hitokoto : guShi.content }}</p>
      <p class="info" v-if="!props.isShehui && (!props.isHitokoto || (props.isHitokoto && !common.isEmpty(hitokoto.from_who)))">
        {{ props.isHitokoto ? hitokoto.from_who : guShi.author }}
      </p>
    </div>
  </div>
</template>
<script setup>

import {getCurrentInstance, onMounted, ref} from "vue";
import {useStore} from "vuex";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const common = globalProperties.$common
const constant = globalProperties.$constant

const store = useStore()

const props = defineProps({
  isHitokoto: {
    type: Boolean,
    default: true
  },
  isShehui: {
    type: Boolean,
    default: false
  }
})

let guShi = ref({
  "content": "...",
  "origin": "...",
  "author": "...",
  "category": "..."
})

let hitokoto = ref( {
  "hitokoto": "...",
  "from": "...",
  "from_who": "..."
})

onMounted(() => {
  if (!props.isShehui) {
    if (props.isHitokoto) {
      getHitokoto();
    } else {
      getGuShi();
    }
  } else {
    hitokoto.value.from = "";
    hitokoto.value.from_who = "";
    sendShehui();
  }
})


const sendShehui = () => {
  let xhr = new XMLHttpRequest();
  xhr.open('get', constant.shehui);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      let shehui = xhr.responseText;
      hitokoto.value.hitokoto = shehui.substring(1, shehui.length - 1);
    }
  };
  xhr.send();
}

const getGuShi = () => {
  let xhr = new XMLHttpRequest();
  xhr.open('get', constant.jinrishici);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      guShi.value = JSON.parse(xhr.responseText);
    }
  };
  xhr.send();
}

const getHitokoto = () => {
  let xhr = new XMLHttpRequest();
  xhr.open('get', constant.hitokoto);
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      hitokoto.value = JSON.parse(xhr.responseText);
    }
  }
  xhr.send();
}
</script>
<style scoped>

.poem-container {
  padding: 90px 0 40px;
  position: relative;
}

.poem-wrap {
  border-radius: 10px;
  z-index: 10;
  text-align: center;
  letter-spacing: 4px;
  font-weight: 300;
  width: 100%;
  max-width: 800px;
}

.poem-wrap div span {
  padding: 5px 10px;
  color: var(--white);
  font-size: 2em;
  border-radius: 5px;
}

.poem-wrap p {
  width: 100%;
  max-width: 800px;
  color: var(--white);
}

.poem-wrap p.poem {
  margin: 40px auto;
  font-size: 1.5em;
}

.poem-wrap p.info {
  margin: 20px auto 40px;
  font-size: 1.1em;
}
</style>
