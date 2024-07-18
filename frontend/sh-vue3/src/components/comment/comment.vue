<template>
  <div>
    <!-- 评论框 -->
    <div style="margin-bottom: 40px">
      <div class="comment-head">
        <el-icon style="font-weight: bold;font-size: 22px;"><Edit /></el-icon>留言
      </div>
      <div>
        <!-- 文字评论 -->
        <div v-show="!isGraffiti">
          <commentBox @showGraffiti="isGraffiti = !isGraffiti"
                      @submitComment="submitComment">
          </commentBox>
        </div>
        <!-- 画笔 -->
        <!--        <div v-show="isGraffiti">-->
        <!--          <graffiti @showComment="isGraffiti = !isGraffiti"-->
        <!--                    @addGraffitiComment="addGraffitiComment">-->
        <!--          </graffiti>-->
        <!--        </div>-->
      </div>
    </div>

    <!-- 评论内容 -->
    <div v-if="comments.length > 0">
      <!-- 评论数量 -->
      <div class="commentInfo-title">
        <span style="font-size: 1.15rem">Comments | </span>
        <span>{{ total }} 条留言</span>
      </div>
      <!-- 评论详情 -->
      <div id="comment-content" class="commentInfo-detail"
           v-for="(item, index) in comments"
           :key="index">
        <!-- 头像 -->
        <el-avatar shape="square" class="commentInfo-avatar" :size="35" :src="item.avatar"></el-avatar>

        <div style="flex: 1;padding-left: 12px">
          <!-- 评论信息 -->
          <div style="display: flex;justify-content: space-between">
            <div>
              <span class="commentInfo-username">{{ item.username }}</span>
              <span class="commentInfo-master" v-if="item.userId === userId">主人翁</span>
              <span class="commentInfo-other">{{ $common.getDateDiff(item.createTime) }}</span>
            </div>
            <div class="commentInfo-reply" @click="replyDialog(item, item)">
              <span v-if="item.childComments.total > 0">{{ item.childComments.total }} </span><span>回复</span>
            </div>
          </div>
          <!-- 评论内容 -->
          <div class="commentInfo-content">
            <span v-html="item.commentContent"></span>
          </div>
          <!-- 回复模块 -->
          <div v-if="!$common.isEmpty(item.childComments) && !$common.isEmpty(item.childComments.records)">
            <div class="commentInfo-detail" v-for="(childItem, i) in item.childComments.records" :key="i">
              <!-- 头像 -->
              <el-avatar shape="square" class="commentInfo-avatar" :size="30" :src="childItem.avatar"></el-avatar>

              <div style="flex: 1;padding-left: 12px">
                <!-- 评论信息 -->
                <div style="display: flex;justify-content: space-between">
                  <div>
                    <span class="commentInfo-username-small">{{ childItem.username }}</span>
                    <span class="commentInfo-master" v-if="childItem.userId === userId">主人翁</span>
                    <span class="commentInfo-other">{{ $common.getDateDiff(childItem.createTime) }}</span>
                  </div>
                  <div>
                    <span class="commentInfo-reply" @click="replyDialog(childItem, item)">回复</span>
                  </div>
                </div>
                <!-- 评论内容 -->
                <div class="commentInfo-content">
                  <template v-if="childItem.parentCommentId !== item.id &&
                                  childItem.parentUserId !== childItem.userId">
                    <span style="color: var(--blue)">@{{ childItem.parentUsername }} </span>:
                  </template>
                  <span v-html="childItem.commentContent"></span>
                </div>
              </div>
            </div>
            <!-- 分页 -->
            <div class="pagination-wrap" v-if="item.childComments.records.length < item.childComments.total">
              <div class="pagination"
                   @click="toChildPage(item)">
                展开
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 分页 -->
      <proPage :current="pagination.current"
               :size="pagination.size"
               :total="pagination.total"
               :buttonSize="6"
               :color="$constant.commentPageColor"
               @toPage="toPage">
      </proPage>
    </div>

    <div v-else class="myCenter" style="color: var(--greyFont)">
      <i>来发第一个留言啦~</i>
    </div>

    <el-dialog title="留言"
               v-model="replyDialogVisible"
               width="30%"
               :before-close="handleClose"
               :append-to-body="true"
               :close-on-click-modal="false"
               destroy-on-close
               center>
      <div>
        <commentBox :disableGraffiti="true"
                    @submitComment="submitReply">
        </commentBox>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
// const graffiti = () => import( "./graffiti");
import {defineAsyncComponent, getCurrentInstance, nextTick, onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {Edit} from "@element-plus/icons-vue";


const globalProperties = getCurrentInstance().appContext.config.globalProperties;
const common = globalProperties.$common
const http = globalProperties.$http
const constant = globalProperties.$constant


const commentBox = defineAsyncComponent(() => import( "./commentBox.vue"));
const proPage = defineAsyncComponent(() => import( "../common/proPage.vue"));

const props = defineProps({
  source: {
    type: Number
  },
  type: {
    type: String
  },
  userId: {
    type: Number
  }
})

// const emit = defineEmits({
//
// })

const isGraffiti = ref(false)
const total = ref(0)
let replyDialogVisible = ref(false)
let floorComment = {}
let replyComment = {}
let comments = ref([])

const pagination = ref({
  current: 1,  //第一页
  size: 10,  //每页展示的评论数
  total: 0,  //总页数
  source: props.source,  //来源
  commentType: props.type,  //类型
  floorCommentId: null   //评论回答
})

onMounted(() => {
  getComments(pagination.value);
  getTotal();
})


const toPage = (page) => {
  pagination.value.current = page;
  window.scrollTo({
    top: document.getElementById('comment-content').offsetTop
  });
  getComments(pagination.value);
}

const getTotal = () => {
  http.get(constant.baseURL + "/comment/getCommentCount", {source: props.source, type: props.type})
      .then((res) => {
        if (!common.isEmpty(res.data)) {
          total.value = res.data;
        }
      })
      .catch((error) => {
        ElMessage.error(error.message);
      });
}

const toChildPage = (floorComment) => {
  floorComment.childComments.current += 1;
  pagination.value = {
    current: floorComment.childComments.current,
    size: 5,
    total: 0,
    source: props.source,
    commentType: props.type,
    floorCommentId: floorComment.id
  }
  getComments(pagination.value, floorComment, true);
}

const emoji = (comments, flag) => {
  comments.value.forEach(c => {
    c.commentContent = c.commentContent.replace(/\n/g, '<br/>');
    c.commentContent = common.faceReg(c.commentContent);
    c.commentContent = common.pictureReg(c.commentContent);
    if (flag) {
      if (!common.isEmpty(c.childComments) && !common.isEmpty(c.childComments.records)) {
        c.childComments.records.forEach(cc => {
          c.commentContent = c.commentContent.replace(/\n/g, '<br/>');
          cc.commentContent = common.faceReg(cc.commentContent);
          cc.commentContent = common.pictureReg(cc.commentContent);
        });
      }
    }
  });
}

const getComments = (pagination_temp, floorComment = {}, isToPage = false) => {
  http.post(constant.baseURL + "/comment/listComment", pagination.value)
      .then((res) => {
        if (!common.isEmpty(res.data) && !common.isEmpty(res.data.records)) {
          if (common.isEmpty(floorComment)) {
            comments.value = res.data.records;
            pagination.value.total = res.data.total;  //赋值一下分页的总数
            emoji(comments, true);
          } else {
            if (isToPage === false) {
              floorComment.childComments = res.data;
            } else {
              floorComment.childComments.total = res.data.total;
              floorComment.childComments.records = floorComment.childComments.records.concat(res.data.records);
            }
            emoji(floorComment.childComments.records, false);
          }
          nextTick(() => {
            common.imgShow("#comment-content .pictureReg");
          })
        }
      })
      .catch((error) => {
        ElMessage.error(error.message);
      });
}

// const addGraffitiComment = (graffitiComment) => {
//   submitComment(graffitiComment);
// }

const submitComment = (commentContent) => {
  let comment = {
    source: props.source,
    type: props.type,
    commentContent: commentContent
  };

  http.post(constant.baseURL + "/comment/saveComment", comment)
      .then(() => {
        ElMessage.success('保存成功！');
        pagination.value = {
          current: 1,
          size: 10,
          total: 0,
          source: props.source,
          commentType: props.type,
          floorCommentId: null
        }
        getComments(pagination.value);
        getTotal();
      })
      .catch((error) => {
        ElMessage.error(error.message);
      });
}

const submitReply = (commentContent) => {
  let comment = {
    source: props.source,
    type: props.type,
    floorCommentId: floorComment.id,
    commentContent: commentContent,
    parentCommentId: replyComment.id,
    parentUserId: replyComment.userId
  };

  http.post(constant.baseURL + "/comment/saveComment", comment)
      .then(() => {
        let pagination_temp = {
          current: 1,
          size: 5,
          total: 0,
          source: props.source,
          commentType: props.type,
          floorCommentId: floorComment.id
        }
        getComments(pagination_temp, floorComment);
        getTotal();
      })
      .catch((error) => {
        ElMessage.error(error.message)
      });
  handleClose();
}

const replyDialog = (comment, floorComment_) => {
  replyComment = comment;
  floorComment = floorComment_;
  replyDialogVisible.value = true;
}

const handleClose = () => {
  replyDialogVisible.value = false;
  floorComment = {};
  replyComment = {};
}

</script>

<style scoped>

.comment-head {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  margin: 40px 0 20px 0;
  user-select: none;
  color: var(--themeBackground);
}

.commentInfo-title {
  margin-bottom: 20px;
  color: var(--greyFont);
  user-select: none;
}

#comment-content {
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.commentInfo-detail {
  display: flex;
}

.commentInfo-avatar {
  border-radius: 5px;
}

.commentInfo-username {
  color: var(--orangeRed);
  font-size: 16px;
  font-weight: 600;
  margin-right: 5px;
}

.commentInfo-username-small {
  color: var(--orangeRed);
  font-size: 14px;
  font-weight: 600;
  margin-right: 5px;
}

.commentInfo-master {
  color: var(--green);
  border: 1px solid var(--green);
  border-radius: 0.2rem;
  font-size: 12px;
  padding: 2px 4px;
  margin-right: 5px;
}

.commentInfo-other {
  font-size: 12px;
  color: var(--greyFont);
  user-select: none;
}

.commentInfo-reply {
  font-size: 12px;
  cursor: pointer;
  user-select: none;
  color: var(--white);
  background: var(--themeBackground);
  border-radius: 0.2rem;
  padding: 3px 6px;
}

.commentInfo-content {
  margin: 15px 0 25px;
  padding: 18px 20px;
  background: var(--commentContent);
  border-radius: 12px;
  color: var(--black);
  word-break: break-word;
}

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.pagination {
  padding: 6px 20px;
  border: 1px solid var(--lightGray);
  border-radius: 3rem;
  color: var(--greyFont);
  user-select: none;
  cursor: pointer;
  text-align: center;
  font-size: 12px;
}

.pagination:hover {
  border: 1px solid var(--themeBackground);
  color: var(--themeBackground);
  box-shadow: 0 0 5px var(--themeBackground);
}
</style>
