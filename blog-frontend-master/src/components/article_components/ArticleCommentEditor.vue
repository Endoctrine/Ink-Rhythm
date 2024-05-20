<script>
import TextEditor from "@/components/utils/TextEditor.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import {store} from "@/store";
import authService from "@/authService";
import axios from "axios";

export default {
  name: "ArticleCommentEditor",
  components: {BasicButton, TextEditor},
  emits: ['submit'],
  props: {
    mArticleID: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      commentContent: ''
    };
  },
  methods: {
    submitComment() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      let self = this;
      axios.post('/api/article/commentArticle', {
        user: store.user,
        token: store.token,
        articleId: self.mArticleID,
        commentContent: self.commentContent,
      }).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          self.$emit('submit');
          self.commentContent = '';
        }
      });
    },
  },
  computed: {
    loggedIn() {
      return store.user !== null;
    },
  },
}
</script>

<template>
  <div>
    <div class="container">
      <TextEditor v-model="commentContent"
                  :m-rows="5"
                  :m-cols="35"
                  :m-width="640"
                  :m-maxlength="256"
                  :m-font-size="23"
                  :m-placeholder="'来一发评论吧~'"/>
      <div class="button-container">
        <BasicButton @click="submitComment"
                     class="button"
                     :m-text="'**发布评论**'"
                     :m-enable="loggedIn && commentContent !== null && commentContent.length > 0"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.button-container {
  display: flex;
  justify-content: right;
}

.button {
  box-shadow: 0 3px 3px 3px rgba(233, 233, 233, 50%);
}
</style>