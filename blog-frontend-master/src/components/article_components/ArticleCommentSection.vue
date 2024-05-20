<script>
import ArticleCommentItem from "@/components/article_components/ArticleCommentItem.vue";
import TextViewer from "@/components/utils/TextViewer.vue";
import axios from "axios";

export default {
  name: "ArticleCommentSection",
  components: {TextViewer, ArticleCommentItem},
  props: {
    mArticleID: {
      type: String,
      required: true,
    },
    mRefresher: {
      type: Boolean,
      required: true,
    }
  },
  data() {
    return {
      commentIDs: [],
    }
  },
  methods: {
    getData() {
      const self = this;
      axios.get('/api/articleCommentSection?id=' + this.mArticleID).then(function (response) {
        self.commentIDs = response.data;
      }).catch(function (error) {
        console.log(error);
      });
    }
  },
  mounted() {
    this.getData();
  },
  watch: {
    mArticleID() {
      this.getData();
    },
    mRefresher() {
      this.getData();
    },
  }
}
</script>

<template>
  <div>
    <div class="container">
      <div class="comment-header">
        <TextViewer :m-content="'**评论区**'"
                    :m-font-size="26"/>
      </div>
      <ArticleCommentItem v-for="id in commentIDs"
                          @submit="getData"
                          :m-refresher="mRefresher"
                          :m-article-i-d="mArticleID"
                          :m-comment-i-d="id"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  border-top: rgba(128, 128, 128, 100%) 5px solid;
}

.comment-header {
  margin-top: 20px;
  padding: 10px;
  border-left: rgba(128, 128, 128, 50%) 5px solid;
  border-top: rgba(233, 233, 233, 50%) 5px solid;
  border-bottom: rgba(233, 233, 233, 50%) 5px solid;
  border-right: rgba(233, 233, 233, 50%) 5px solid;
  background: rgba(233, 233, 233, 30%);
}
</style>