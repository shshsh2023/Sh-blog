<template>
  <div class="myCenter verify-container">
    <div class="verify-content">
      <div>
        <el-avatar :size="50" :src="$store.state.webInfo.avatar"></el-avatar>
      </div>
      <div>
        <el-input v-model="account">
          <template v-slot:prepend>账号</template>
        </el-input>
      </div>
      <div>
        <el-input v-model="password" type="password">
          <template v-slot:prepend>密码</template>
        </el-input>
      </div>
      <div>
        <proButton :info="'提交'"
                   @click="login()"
                   :before="$constant.before_color_2"
                   :after="$constant.after_color_2">
        </proButton>
      </div>
    </div>
  </div>
</template>

<script setup>
import {defineAsyncComponent, getCurrentInstance, ref} from "vue";
import {ElMessage} from "element-plus";
import {useRoute, useRouter} from "vue-router";
import {useStore} from "vuex";

const proButton = defineAsyncComponent(() => import( "../common/proButton.vue"));

const router = useRouter();
const route = useRoute();
const store = useStore();


const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const common = globalProperties.$common
const constant = globalProperties.$constant
const http = globalProperties.$http

// const redirect = ref(route.query.redirect)
const account = ref("")
const password = ref("")

// console.log(route.query)

const login = () => {
  if (common.isEmpty(account.value) || common.isEmpty(password.value)) {
    ElMessage({
      message: "请输入账号或密码！",
      type: "error"
    })
    return;
  }

  let user = {
    account: account.value.trim(),
    password: common.encrypt(password.value.trim()),
    isAdmin: true
  };

  http.post(constant.baseURL + "/user/login", user, true, false)
      .then((res) => {
        if (!common.isEmpty(res.data)) {
          localStorage.setItem("adminToken", res.data.accessToken);
          store.commit("loadCurrentAdmin", res.data);
          account.value = "";
          password.value = "";
          router.push({path: "/admin"});
        }
      })
      .catch((error) => {
        ElMessage({
          message: error.message,
          type: "error"
        })
      });
}
</script>

<style scoped>

.verify-container {
  height: 100vh;
  background: var(--backgroundPicture) center center / cover repeat;
}

.verify-content {
  background: var(--maxWhiteMask);
  padding: 30px 40px 5px;
  position: relative;
}

.verify-content > div:first-child {
  position: absolute;
  left: 50%;
  transform: translate(-50%);
  top: -25px;
}

.verify-content > div:not(:first-child) {
  margin: 25px 0;
}

.verify-content > div:last-child > div {
  margin: 0 auto;
}

</style>
