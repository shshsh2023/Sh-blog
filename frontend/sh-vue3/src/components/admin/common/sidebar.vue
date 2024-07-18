<template>
  <div class="sidebar">
    <el-radio-group v-model="isCollapse.statu" style="margin-bottom: 20px">
      <el-radio-button :value="!isCollapse.statu" @click="isCollapse.next === '展开'? isCollapse.next = '收缩': isCollapse.next = '展开'" >
        <el-icon><Open/></el-icon>{{isCollapse.next}}
      </el-radio-button>
    </el-radio-group>
    <el-menu class="sidebar-el-menu"
             background-color="#ebf1f6"
             text-color="#606266"
             active-text-color="#20a0ff"
             unique-opened
             :collapse="isCollapse.statu"
             :default-active="$router.currentRoute.path"
             router>
      <template v-for="item in items">
        <template v-if="isBoss || !item.isBoss">
          <template v-if="item.subs">
            <el-sub-menu :index="item.index" :key="item.index">
              <template v-slot:title>
                <i :class="item.icon"></i>
                <span>{{ item.title }}</span>
              </template>
              <template v-for="subItem in item.subs">
                <el-sub-menu v-if="subItem.subs" :index="subItem.index" :key="subItem.index + 'if'">
                  <template v-slot:title>
                    {{ subItem.title }}
                  </template>
                  <el-menu-item v-for="threeItem in subItem.subs" :key="threeItem.index" :index="threeItem.index">
                    {{ threeItem.title }}
                  </el-menu-item>
                </el-sub-menu>
                <el-menu-item v-else :index="subItem.index" :key="subItem.index + 'else'">
                  {{ subItem.title }}
                </el-menu-item>
              </template>
            </el-sub-menu>
          </template>
          <template v-else>
            <el-menu-item :index="item.index" :key="item.index">
              <el-icon :class="iconClass">
                <component :is="item.icon"/>
              </el-icon>
              <span>{{ item.title }}</span>
            </el-menu-item>
          </template>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script setup>
import {ref} from "vue";
import {useStore} from "vuex";
import {
  Open,
  HomeFilled,
  Setting,
  UserFilled,
  Postcard,
  Notebook,
  DocumentAdd,
  Comment,
  Paperclip, Phone, DocumentCopy, Sugar
} from "@element-plus/icons-vue";

const store = useStore();

const iconClass = ref({
  color: "red"
})


const isCollapse = ref({
  statu: true,
  next: "展开"
})

const isBoss = ref(store.state.currentAdmin.isBoss)

const items = ref([{
  icon: HomeFilled,
  index: "/main",
  title: "系统首页",
  isBoss: true
}, {
  icon: Setting,
  index: "/webEdit",
  title: "网站设置",
  isBoss: true
}, {
  icon: UserFilled,
  index: "/userList",
  title: "用户管理",
  isBoss: true
}, {
  icon: Postcard,
  index: "/postList",
  title: "文章管理",
  isBoss: false
}, {
  icon: Notebook,
  index: "/sortList",
  title: "分类管理",
  isBoss: true
}, {
  icon: DocumentAdd,
  index: "/configList",
  title: "配置管理",
  isBoss: true
}, {
  icon: Comment,
  index: "/commentList",
  title: "评论管理",
  isBoss: false
}, {
  icon: Phone,
  index: "/treeHoleList",
  title: "留言管理",
  isBoss: true
}, {
  icon: Paperclip,
  index: "/resourceList",
  title: "资源管理",
  isBoss: true
}, {
  icon: DocumentCopy,
  index: "/resourcePathList",
  title: "资源聚合",
  isBoss: true
}, {
  icon: Sugar,
  index: "/loveList",
  title: "表白墙",
  isBoss: true,
}])


// const collapse = () => {
//   if (isCollapse.value) {
//     $(".sidebar").css("width", "45px");
//     $(".content-box").css("left", "45px");
//   } else {
//     $(".sidebar").css("width", "130px");
//     $(".content-box").css("left", "130px");
//   }
//   isCollapse.value = !isCollapse.value;
// }

</script>

<style scoped>

.sidebar {
  z-index: 100;
  display: block;
  position: absolute;
  left: 0;
  top: 80px;
  bottom: 0;
  overflow-y: scroll;
  width: 130px;
  user-select: none;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar > ul {
  height: 100%;
}

.sidebar-el-menu .el-menu-item {

}
</style>
