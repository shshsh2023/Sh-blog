<template>
  <div>
    <!-- 两句诗 -->
    <div class="my-animation-slide-top">
      <twoPoem></twoPoem>
    </div>
    <div style="background: var(--background);padding-top: 40px;" class="my-animation-slide-bottom">
      <!-- 标签 -->
      <div class="sort-warp shadow-box" v-if="!common.isEmpty(sort) && !common.isEmpty(sort.labels)">
        <div v-for="(label, index) in sort.labels" :key="index"
             :class="{isActive: !common.isEmpty(labelId) && parseInt(labelId) === label.id}"
             @click="listArticle(label)">
          <proTag :info="label.labelName+' '+label.countOfLabel"
                  :color="constant.before_color_list[Math.floor(Math.random() * 6)]"
                  style="margin: 12px">
          </proTag>
        </div>
      </div>

      <!-- 文章 -->
      <div class="article-wrap">
        <articleList :articleList="articles"></articleList>
        <div class="pagination-wrap">
          <div @click="pageArticles()" class="pagination" v-if="pagination.total !== articles.length">
            下一页
          </div>
          <div v-else style="user-select: none">
            ~~到底啦~~
          </div>
        </div>
      </div>
      <!-- 页脚 -->
      <myFooter></myFooter>
    </div>
  </div>
</template>

<script setup>

import {defineAsyncComponent, getCurrentInstance, nextTick, onMounted, ref, watch} from "vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {ElMessage} from "element-plus";

const twoPoem = defineAsyncComponent(() => import( "./common/twoPoem.vue"));
const proTag = defineAsyncComponent(() => import( "./common/proTag.vue"));
const articleList = defineAsyncComponent(() => import( "./articleList.vue"));
const myFooter = defineAsyncComponent(() => import( "./common/myFooter.vue"));

const store = useStore()
const route = useRoute()

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const common = globalProperties.$common
const http = globalProperties.$http
const constant = globalProperties.$constant


let sortId = ref(route.query.sortId)
let labelId = ref(route.query.labelId)
let sort = ref(null)
let pagination = ref({
  current: 1,
  size: 10,
  total: 0,
  searchKey: "",
  sortId: route.query.sortId,
  labelId: route.query.labelId
})
let articles = ref([])


watch(route, () => {
  pagination.value = {
    current: 1,
    size: 10,
    total: 0,
    searchKey: "",
    sortId: route.query.sortId,
    labelId: route.query.labelId
  };
  articles.value.splice(0, articles.value.length);
  sortId.value = route.query.sortId;
  labelId.value = route.query.labelId;
  getSort();
  getArticles();
})

onMounted(() => {
  getSort();
  getArticles();
})


const pageArticles = () => {
  pagination.value.current = pagination.value.current + 1;
  getArticles();
}

const getSort = () => {
  let sortInfo = store.state.sortInfo;
  if (!common.isEmpty(sortInfo)) {
    let sortArray = sortInfo.filter(f => {
      return f.id === parseInt(sortId.value);
    });
    if (!common.isEmpty(sortArray)) {
      sort.value = sortArray[0];
    }
  }
}

const listArticle = (label) => {
  labelId.value = label.id;
  pagination.value = {
    current: 1,
    size: 10,
    total: 0,
    searchKey: "",
    sortId: route.query.sortId,
    labelId: label.id
  };
  articles.value.splice(0, articles.value.length);
  nextTick(() => {
    getArticles();
  });
}

const getArticles = () => {
  http.post(constant.baseURL + "/article/listArticle", pagination)
      .then((res) => {
        if (!common.isEmpty(res.data)) {
          articles.value = articles.value.concat(res.data.records);
          pagination.value.total = res.data.total;
        }
      })
      .catch((error) => {
        ElMessage.error(error.message)
      });
}
</script>

<style scoped>

.sort-warp {
  width: 70%;
  max-width: 780px;
  margin: 0 auto;
  padding: 20px;
  border-radius: 10px;
  display: flex;
  flex-wrap: wrap;
}

.article-wrap {
  width: 70%;
  margin: 40px auto;
  min-height: 600px;
}

.isActive {
  animation: scale 1.5s ease-in-out infinite;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.pagination {
  padding: 13px 15px;
  border: 1px solid var(--lightGray);
  border-radius: 3rem;
  color: var(--greyFont);
  width: 100px;
  user-select: none;
  cursor: pointer;
  text-align: center;
}

.pagination:hover {
  border: 1px solid var(--themeBackground);
  color: var(--themeBackground);
  box-shadow: 0 0 5px var(--themeBackground);
}


@media screen and (max-width: 900px) {
  .sort-warp {
    width: 90%;
  }

  .article-wrap {
    width: 90%;
  }
}
</style>
