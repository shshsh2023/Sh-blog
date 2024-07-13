import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import http from './utils/request.js'
import common from './utils/common.js'
import constant from './utils/constant.js'
import mavonEditor from 'mavon-editor'
import { createApp } from 'vue'
import router from './router'
import store from './store'

//引入js
// import './utils/live2d'
import './utils/title'

//引入css
import './assets/css/animation.css'
import './assets/css/index.css'
import './assets/css/tocbot.css'
import './assets/css/color.css'
import './assets/css/markdown-highlight.css'
import './assets/css/font-awesome.min.css'
import 'mavon-editor/dist/css/index.css'

import {vueBaberrage} from 'vue-baberrage'
import App from './App.vue'

const app = createApp(App)


app.config.globalProperties.$http = http
app.config.globalProperties.$common = common
app.config.globalProperties.$constant = constant


app.use(ElementPlus)
app.use(vueBaberrage)
app.use(mavonEditor)
app.use(router)
app.use(store)

app.mount('#app')
