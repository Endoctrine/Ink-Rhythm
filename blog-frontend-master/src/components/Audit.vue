<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import ArticleShowerSection from "@/components/home_components/ArticleShowerSection.vue";
import UserShowerSection from "@/components/search_components/UserShowerSection.vue";
import authService from "@/authService";
import axios from "axios";
import {store} from "@/store";

export default {
  name: "Audit",
  components: {UserShowerSection, ArticleShowerSection, TextViewer},
  data() {
    return {
      articleIDs: [],
      users: [],
    }
  },
  methods: {
    getData() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      const self = this;
      axios.get('/api/audit?username=' + store.user).then(function (response) {
        self.articleIDs = response.data.articleIDs;
        self.users = response.data.users;
      }).catch(function (error) {
        console.log(error);
      })
    }
  },
  mounted() {
    this.getData();
  }
}
</script>

<template>
  <div>
    <div class="container">
      <div class="label-container">
        <TextViewer :m-content="'**管理员申请**'"
                    :m-font-size="30"
                    :m-font-family="'HYWenHei-45W'"/>
      </div>
      <UserShowerSection @audit-handled="getData"
                         :m-shown-users="users"
                         :m-user-per-page="5"
                         :m-audit-mode="true"/>
      <div class="label-container">
        <TextViewer :m-content="'**被举报的文章**'"
                    :m-font-size="30"
                    :m-font-family="'HYWenHei-45W'"/>
      </div>
      <ArticleShowerSection @audit-handled="getData"
                            :m-article-i-ds="articleIDs"
                            :m-article-per-page="3"
                            :m-audit-mode="true"/>
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

</style>