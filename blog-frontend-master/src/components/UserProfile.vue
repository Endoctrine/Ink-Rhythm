<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import UserShower from "@/components/user_profile_components/UserShower.vue";
import UserEditor from "@/components/user_profile_components/UserEditor.vue";
import ArticleShowerSection from "@/components/home_components/ArticleShowerSection.vue";
import UserShowerSection from "@/components/search_components/UserShowerSection.vue";
import authService from "@/authService";
import {router} from "@/router";
import {store} from "@/store";
import axios from "axios";

export default {
  name: "UserProfile",
  components: {UserShowerSection, UserEditor, BasicButton, ArticleShowerSection, TextViewer, UserShower},
  data() {
    return {
      admin: false,
      profileBeingEdited: false,
      followingUsers: [],
      authoredArticleIDs: [],
      bookmarkedArticleIDs: [],
    };
  },
  computed: {
    store() {
      return store;
    },
    shownUser() {
      return this.$route.params.id.toString();
    },
  },
  methods: {
    getData() {
      const self = this;
      axios.get('/api/user?username=' + this.shownUser).then(function (response) {
        self.admin = response.data.admin;
        self.followingUsers = response.data.followingUsers;
        self.authoredArticleIDs = response.data.authoredArticleIDs;
        self.bookmarkedArticleIDs = response.data.bookmarkedArticleIDs;
      }).catch(function (error) {
        console.log(error);
      })
    },
    handleSaveEdit() {
      this.profileBeingEdited = false;
    },
    handlePostArticle() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/user/checkValid?username='
          + store.user
          + '&token='
          + store.token).then(function (response) {
        if (response.data) {
          router.push('/article-editor');
        } else {
          authService.fail();
        }
      }).catch(function (error) {
        console.log(error);
      })
    },
    handleAudit() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/user/checkValid?username='
          + store.user
          + '&token='
          + store.token).then(function (response) {
        if (response.data) {
          router.push('/audit');
        } else {
          authService.fail();
        }
      }).catch(function (error) {
        console.log(error);
      })
    },
    handleApplyAdmin() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/user/applyAdmin?username='
          + store.user
          + '&token='
          + store.token).then(function (response) {
        if (!response.data) {
          authService.fail();
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    handleLogout() {
      if (store.user === null) {
        store.logout();
        router.push('/');
      } else {
        axios.get('/api/logout?user=' + store.user).then(function (response) {
          store.logout();
          router.push('/');
        }).catch(function (error) {
          console.log(error);
        });
      }
    },
    navigateToStatistics() {
      router.push('/statistics');
    }
  },
  mounted() {
    this.getData();
  },
  watch:{
    shownUser(){
      this.getData();
    }
  }
}
</script>

<template>
  <div>
    <div class="container">
      <UserShower @edit-profile="profileBeingEdited = true"
                  v-if="!profileBeingEdited"
                  :m-shown-user="shownUser"/>
      <UserEditor @save-profile="handleSaveEdit"
                  v-if="profileBeingEdited"
                  :m-shown-user="shownUser"/>
      <div class="profile-content-container">
        <div class="profile-main-content-container">
          <div class="label-container">
            <TextViewer :m-content="'**文章**'"
                        :m-font-size="30"
                        :m-font-family="'HYWenHei-45W'"/>
          </div>
          <ArticleShowerSection :m-article-per-page="2"
                                :m-article-i-ds="authoredArticleIDs"/>
        </div>
        <div v-if="store.user===shownUser"
             class="user-operation-container">
          <div class="label-container">
            <TextViewer :m-content="'&nbsp;'"
                        :m-font-size="30"
                        :m-font-family="'HYWenHei-45W'"/>
          </div>
          <div class="user-operation-button-container">
            <BasicButton @click="handlePostArticle" :m-text="'**发布文章**'"/>
            <BasicButton @click="handleLogout" :m-text="'**退出登录**'"/>
            <BasicButton @click="navigateToStatistics" v-show="admin" :m-text="'**系统数据统计**'"/>
            <BasicButton @click="handleAudit" v-show="admin" :m-text="'**审核管理**'"/>
            <BasicButton @click="handleApplyAdmin" v-show="!admin" :m-text="'**申请成为管理员**'"/>
          </div>
        </div>
      </div>

      <div class="profile-content-container">
        <div class="profile-main-content-container">
          <div class="label-container">
            <TextViewer :m-content="'**收藏夹**'"
                        :m-font-size="30"
                        :m-font-family="'HYWenHei-45W'"/>
          </div>
          <ArticleShowerSection :m-article-per-page="2"
                                :m-article-i-ds="bookmarkedArticleIDs"/>
        </div>
      </div>

      <div class="profile-content-container">
        <div class="profile-main-content-container">
          <div class="label-container">
            <TextViewer :m-content="'**关注的人**'"
                        :m-font-size="30"
                        :m-font-family="'HYWenHei-45W'"/>
          </div>
          <UserShowerSection :m-shown-users="followingUsers"
                             :m-user-per-page="5"/>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
.container {
  width: 800px;
  margin-top: 50px;
}

.label-container {
  margin-top: 20px;
  margin-bottom: 20px;
  padding: 10px;

  border-bottom: rgba(64, 64, 64, 100%) solid 3px;
}

.profile-content-container {
  display: flex;
}

.profile-main-content-container {
  flex: 1;
}

.user-operation-container {
  margin-left: 50px;
}
</style>