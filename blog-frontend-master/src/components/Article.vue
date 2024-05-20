<script>
import ArticleHeader from "@/components/article_components/ArticleHeader.vue";
import TextViewer from "@/components/utils/TextViewer.vue";
import ArticleFeedBack from "@/components/article_components/ArticleFeedBack.vue";
import ArticleCommentEditor from "@/components/article_components/ArticleCommentEditor.vue";
import ArticleCommentSection from "@/components/article_components/ArticleCommentSection.vue";
import {store} from "@/store";
import axios from "axios";

export default {
  name: "Article",
  components: {ArticleCommentSection, ArticleCommentEditor, ArticleFeedBack, TextViewer, ArticleHeader},
  data() {
    return {
      articleID: '',
      articleTitle: '',
      author: '',
      releaseTime: '',
      viewCount: 0,
      likeCount: 0,
      bookmarkCount: 0,
      articleContent: '',
      articleCommentRefresher: false,
    };
  },
  methods: {
    getData() {
      this.articleID = this.$route.params.id.toString();
      const self = this;
      axios.get('/api/article?id=' + this.articleID).then(function (response) {
        self.articleTitle = response.data.articleTitle;
        self.author = response.data.author;
        self.releaseTime = response.data.releaseTime;
        self.viewCount = response.data.viewCount;
        self.likeCount = response.data.likeCount;
        self.bookmarkCount = response.data.bookmarkCount;
        self.articleContent = response.data.articleContent;
      }).catch(function (error) {
        console.log(error);
      });
      if (store.user !== null) {
        axios.get('/api/article/read?id='
            + this.articleID
            + '&user='
            + store.user).catch(function (error) {
          console.log(error);
        });
      }
    },
    updateCommentSection() {
      this.articleCommentRefresher = !this.articleCommentRefresher;
    }
  },
  mounted() {
    this.getData();
  },
}
</script>

<template>
  <div>
    <div class="container">
      <ArticleHeader :m-article-title="articleTitle"
                     :m-author="author"
                     :m-release-time="releaseTime"
                     :m-like-count="likeCount"
                     :m-bookmark-count="bookmarkCount"
                     :m-view-count="viewCount"/>
      <div class="placeholder"></div>
      <TextViewer :m-content="articleContent"
                  :m-font-family="'HYWenHei-45W'"
                  :m-font-size="26"/>
      <div class="article-end-container">
        <div></div>
        <TextViewer :m-content="'--END--'"
                    :m-font-family="'HYWenHei-45W'"
                    :m-font-size="16"/>
        <div></div>
      </div>
      <ArticleFeedBack :m-author="author" :m-article-i-d="articleID"/>
      <div class="placeholder"></div>
      <ArticleCommentEditor @submit="updateCommentSection" :m-article-i-d="articleID"/>
      <div class="placeholder"></div>
      <ArticleCommentSection :m-refresher="articleCommentRefresher"
                             :m-article-i-d="articleID"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  margin-top: 50px;
  padding-top: 20px;
  padding-bottom: 50px;


  box-shadow: 0 0 5px 5px rgba(233, 233, 233, 50%);
}

.container > div {
  width: 80%;
  margin-left: 10%;
  margin-right: 10%;
}

.placeholder {
  height: 20px;
}

.article-end-container {
  margin-top: 20px;
  margin-bottom: 20px;
  display: flex;

  align-items: center;
  justify-content: center;
}

</style>