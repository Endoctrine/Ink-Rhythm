<script>
import BasicButton from "@/components/utils/BasicButton.vue";
import authService from "@/authService";
import {router} from "@/router";
import {store} from "@/store";
import axios from "axios";

export default {
  name: "ArticleFeedBack",
  components: {BasicButton},
  props: {
    mArticleID: {
      type: String,
      required: true,
    },
    mAuthor: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      admin: false,
      liked: false,
      bookmarked: false,
    };
  },
  computed: {
    store() {
      return store;
    },
    loggedIn() {
      return store.user !== null;
    },
  },
  methods: {
    getData() {
      if (store.user === null) {
        this.admin = false;
        this.liked = false;
        this.bookmarked = false;
        return;
      }
      const self = this;
      axios.get('/api/article/feedbackInfo?articleId='
          + this.mArticleID
          + '&user='
          + store.user).then(function (response) {
        self.admin = response.data.admin;
        self.liked = response.data.liked;
        self.bookmarked = response.data.bookmarked;
      }).catch(function (error) {
        console.log(error);
      });
    },
    handleLike() {
      const self = this;
      if (store.user === null) {
        authService.fail();
        return;
      }
      if (this.liked) {
        axios.get('/api/article/dislike?articleId='
            + this.mArticleID
            + '&user='
            + store.user
            + '&token='
            + store.token).then(function (response) {
          if (!response.data) {
            authService.fail();
          } else {
            self.getData();
          }
        }).catch(function (error) {
          console.log(error);
        });
      } else {
        axios.get('/api/article/like?articleId='
            + this.mArticleID
            + '&user='
            + store.user
            + '&token='
            + store.token).then(function (response) {
          if (!response.data) {
            authService.fail();
          } else {
            self.getData();
          }
        }).catch(function (error) {
          console.log(error);
        });
      }
    },
    handleBookMark() {
      const self = this;
      if (store.user === null) {
        authService.fail();
        return;
      }
      if (this.bookmarked) {
        axios.get('/api/article/unBookmark?articleId='
            + this.mArticleID
            + '&user='
            + store.user
            + '&token='
            + store.token).then(function (response) {
          if (!response.data) {
            authService.fail();
          } else {
            self.getData();
          }
        }).catch(function (error) {
          console.log(error);
        });
      } else {
        axios.get('/api/article/bookmark?articleId='
            + this.mArticleID
            + '&user='
            + store.user
            + '&token='
            + store.token).then(function (response) {
          if (!response.data) {
            authService.fail();
          } else {
            self.getData();
          }
        }).catch(function (error) {
          console.log(error);
        });
      }
    },
    handleAuditBan() {
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
          router.back();
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    handleDelete() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/article/delete?articleId='
          + this.mArticleID
          + '&user='
          + store.user
          + '&token='
          + store.token).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          router.back();
        }
      }).catch(function (error) {
        console.log(error);
      })
    },
    handleReport() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/article/report?articleId=' + this.mArticleID).then(
          // do nothing
      ).catch(function (error) {
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
  }
}
</script>

<template>
  <div>
    <div class="container">
      <div class="container-item">
        <BasicButton @click="handleLike"
                     class="button"
                     :m-text="liked ? '**取消点赞**' : '**点赞**'"
                     :m-enable="loggedIn"/>
        <BasicButton @click="handleBookMark"
                     class="button"
                     :m-text="bookmarked ? '**取消收藏**' : '**收藏**'"
                     :m-enable="loggedIn"/>
      </div>
      <div>
        <BasicButton v-if="admin"
                     @click="handleAuditBan"
                     class="button"
                     :m-text="'**封禁**'"
                     :m-enable="loggedIn"/>
        <BasicButton v-else-if="store.user === mAuthor"
                     @click="handleDelete"
                     class="button"
                     :m-text="'**删除**'"
                     :m-enable="loggedIn"/>
        <BasicButton v-else
                     @click="handleReport"
                     class="button"
                     :m-text="'**举报**'"
                     :m-enable="loggedIn"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
}

.container-item {
  display: flex;
}

.button {
  box-shadow: 0 3px 3px 3px rgba(233, 233, 233, 50%);
}
</style>