<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import {store} from "@/store";
import {router} from "@/router";
import authService from "@/authService";
import axios from "axios";

export default {
  name: "ArticleShower",
  components: {BasicButton, TextViewer},
  emits: ['auditHandled'],
  props: {
    mArticleID: {
      type: String,
      required: true,
    },
    mAuditMode: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      title: '',
      author: '',
      partialContent: '',
    };
  },
  methods: {
    getData() {
      const self = this;
      axios.get('/api/article?id=' + this.mArticleID).then(function (response) {
        self.title = response.data.articleTitle;
        self.author = response.data.author;
        self.partialContent = response.data.articleContent.slice(0, 128);
      }).catch(function (error) {
        console.log(error);
      });
    },
    navigateToArticle() {
      router.push('/article/' + this.mArticleID);
    },
    handleAuditPass() {
      const self = this;
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/audit/passArticle?admin='
          + store.user
          + '&token='
          + store.token
          + '&articleId='
          + this.mArticleID).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          self.$emit('auditHandled');
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    handleAuditBan() {
      const self = this;
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/audit/banArticle?admin='
          + store.user
          + '&token='
          + store.token
          + '&articleId='
          + this.mArticleID).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          self.$emit('auditHandled');
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
  },
  mounted() {
    this.getData();
  },
  watch: {
    mArticleID() {
      this.getData();
    }
  },
}
</script>

<template>
  <div>
    <div class="container">
      <div class="article-container" @click="navigateToArticle">
        <div class="title-container">
          <TextViewer :style="{width:'max-content'}"
                      :m-content="title"
                      :m-font-size="26"
                      :m-font-family="'HYWenHei-45W'"/>
        </div>
        <div class="info-container">
          <TextViewer :style="{color:'rgba(64, 64, 64, 100%)',width:'max-content'}"
                      :m-content="'**发布者**&nbsp'+author"
                      :m-font-size="20"
                      :m-font-family="'HYWenHei-45W'"/>
        </div>
        <div class="content-container">
          <TextViewer :style="{color:'rgba(128, 128, 128, 100%)',width:'100%'}"
                      :m-content="partialContent"
                      :m-font-size="20"
                      :m-font-family="'HYWenHei-45W'"/>
        </div>
      </div>
      <div v-if="mAuditMode" class="feedback-container">
        <BasicButton @click="handleAuditBan" m-text="**封禁**"/>
        <BasicButton @click="handleAuditPass" m-text="**不封禁**"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.article-container {
  height: 300px;
  padding: 5px 15px;
  overflow: auto;

  transition: 0.3s;
}

.article-container:hover {
  background: rgba(233, 233, 233, 50%);
  cursor: pointer;
}

.title-container {
  border-bottom: rgba(128, 128, 128, 100%) solid 3px;
  overflow: auto;
}

.info-container {
  overflow: auto;
}

.content-container {
  max-height: 70%;

  border-left: rgba(128, 128, 128, 100%) solid 5px;
  border-bottom: rgba(233, 233, 233, 100%) solid 3px;
  background: rgba(233, 233, 233, 50%);
  padding: 0 10px;

  overflow: auto;
}

.container > div::-webkit-scrollbar {
  display: none;
}

.feedback-container {
  padding: 5px 15px;

  display: flex;
  justify-content: flex-end;
}

</style>