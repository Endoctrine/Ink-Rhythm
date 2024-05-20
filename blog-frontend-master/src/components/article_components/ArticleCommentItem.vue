<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import TextEditor from "@/components/utils/TextEditor.vue";
import {store} from "@/store";
import authService from "@/authService";
import axios from "axios";

export default {
  name: "ArticleCommentItem",
  components: {BasicButton, TextViewer, TextEditor},
  props: {
    mCommentID: {
      type: String,
      required: true
    },
    mArticleID: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      commentObj: {subCommentObjList: []},
      subCommentContent: '',
      subCommentActive: false,
      subCommentButtonText: '**评论**',
    };
  },
  methods: {
    getData() {
      const self = this;
      axios.get('/api/articleComment?id=' + this.mCommentID).then(function (response) {
        self.commentObj = response.data;
      }).catch(function (error) {
        console.log(error);
      });
    },
    handleSubComment() {
      if (!this.subCommentActive) {
        this.subCommentActive = true;
        this.subCommentButtonText = '**取消**';
      } else if (this.subCommentContent.length === 0) {
        this.subCommentActive = false;
        this.subCommentButtonText = '**评论**';
      } else {
        if (store.user === null) {
          authService.fail();
          return;
        }
        const self = this;
        axios.post('/api/article/commentComment', {
          user: store.user,
          token: store.token,
          commentId: self.mCommentID,
          commentContent: self.subCommentContent,
        }).then(function (response) {
          if (!response.data) {
            authService.fail();
          } else {
            self.getData();
            self.subCommentContent = '';
            self.subCommentActive = false;
            self.subCommentButtonText = '**评论**';
          }
        });
      }
    },
  },
  watch: {
    subCommentContent(newVal, oldVal) {
      if (this.subCommentActive && newVal.length > 0 && oldVal.length === 0) {
        this.subCommentButtonText = '**发布**';
      } else if (this.subCommentActive && newVal.length === 0) {
        this.subCommentButtonText = '**取消**';
      }
    },
  },
  computed: {
    loggedIn() {
      return store.user !== null;
    },
  },
  mounted() {
    this.getData();
  }
}
</script>

<template>
  <div>
    <div class="container">
      <TextViewer
          class="comment"
          :m-content="'**'+commentObj.user+'**\n'+commentObj.commentContent"
          :m-font-family="'HYWenHei-45W'"
          :m-font-size="20"
          :m-width="640"/>
      <div class="placeholder"></div>
      <TextEditor class="sub-comment-editor"
                  v-if="subCommentActive"
                  v-model="subCommentContent"
                  :key="1"
                  :m-rows="2"
                  :m-cols="35"
                  :m-width="640"
                  :m-maxlength="128"
                  :m-font-size="18"
                  :m-placeholder="'互动一下吧~'"/>
      <div class="button-container" :key="2">
        <BasicButton class="button"
                     @click="handleSubComment"
                     :m-text="subCommentButtonText"
                     :m-enable="loggedIn"/>
      </div>
      <div class="sub-comment-container">
        <TextViewer v-for="item in commentObj.subCommentObjList"
                    class="sub-comment"
                    :m-content="'**'+item.user+'**\n'+item.commentContent"
                    :m-font-family="'HYWenHei-45W'"
                    :m-font-size="18"
                    :m-width="560"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding-bottom: 20px;
  border-bottom: rgba(233, 233, 233, 50%) solid 5px;
}

.comment {
  margin-top: 20px;
  padding: 10px;
  border-left: rgba(128, 128, 128, 50%) 5px solid;
  background: rgba(233, 233, 233, 50%);
}

.sub-comment-container {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.sub-comment {
  margin-top: 10px;
  padding: 10px;
  border-left: rgba(233, 233, 233, 50%) 5px solid;
  background: rgba(233, 233, 233, 30%);
  color: rgba(128, 128, 128, 100%);
}

.button-container {
  display: flex;
  justify-content: right;
}

.button {
  box-shadow: 0 3px 3px 3px rgba(233, 233, 233, 50%);
}

.placeholder {
  height: 10px;
}

</style>