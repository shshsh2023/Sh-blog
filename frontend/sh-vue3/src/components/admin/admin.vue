<template>
  <div>
    <myHeader></myHeader>
    <sidebar></sidebar>
    <div class="content-box" style="z-index: 10">
      <div class="content">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<script setup>
import myHeader from "./common/myHeader.vue";
import sidebar from "./common/sidebar.vue";
import {getCurrentInstance, onMounted} from "vue";
import {useStore} from "vuex";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const common = globalProperties.$common

const store = useStore()


onMounted(() => {
  let sysConfig = store.state.sysConfig;
  if (!common.isEmpty(sysConfig) && !common.isEmpty(sysConfig['webStaticResourcePrefix'])) {
    let root = document.querySelector(":root");
    let webStaticResourcePrefix = sysConfig['webStaticResourcePrefix'];
    root.style.setProperty("--backgroundPicture", "url(" + webStaticResourcePrefix + "assets/backgroundPicture.jpg)");

    // const font_path = new URL("@/assets/webfont/font.ttf", import.meta.url).href.toString();
    // const font = new FontFace("poetize-font", `url(${font_path})`);
    // font.load();
    // document.fonts.add(font);
  }
})

</script>

<style scoped>

.content-box {
  position: absolute;
  left: 130px;
  right: 0;
  top: 70px;
  bottom: 0;
  transition: left .3s ease-in-out;
}

.content {
  width: auto;
  height: 100%;
  padding: 30px;
  overflow-y: scroll;
}

</style>
