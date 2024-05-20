<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import ArticleShowerSection from "@/components/home_components/ArticleShowerSection.vue";
import axios from "axios";
import {store} from "@/store";

export default {
  name: "Home",
  components: {TextViewer, ArticleShowerSection},
  data() {
    return {
      announcementIDs: [],
      hotIDs: [],
      recommendIDs: [],
    };
  },
  methods: {
    getData() {
      const self = this;
      axios.get('/api/home?user=' + (store.user === null ? '' : store.user)).then(function (response) {
        self.announcementIDs = response.data.announcementIDs;
        self.hotIDs = response.data.hotIDs;
        self.recommendIDs = response.data.recommendIDs;
      }).catch(function (error) {
        console.log(error);
      });
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
      <div class="home-header-container">
        <TextViewer :style="{textAlign:'center'}"
                    :m-width="640"
                    :m-content="'Rewrite The Stars'"
                    :m-font-size="64"
                    :m-font-family="'Saira Extra Condensed'"/>
      </div>
      <div class="placeholder"></div>
      <div class="label-container">
        <TextViewer :m-content="'**公告**'"
                    :m-font-size="30"
                    :m-font-family="'HYWenHei-45W'"/>
      </div>
      <ArticleShowerSection :m-article-i-ds="announcementIDs"/>
      <div class="placeholder"></div>
      <div class="label-container">
        <TextViewer :m-content="'**热门文章**'"
                    :m-font-size="30"
                    :m-font-family="'HYWenHei-45W'"/>
      </div>
      <ArticleShowerSection :m-article-i-ds="hotIDs"/>
      <div class="placeholder"></div>
      <div class="label-container">
        <TextViewer :m-content="'**猜你喜欢**'"
                    :m-font-size="30"
                    :m-font-family="'HYWenHei-45W'"/>
      </div>
      <ArticleShowerSection :m-article-i-ds="recommendIDs"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  margin-top: 50px;
}

.home-header-container {
  display: flex;
  align-items: center;
  justify-content: center;

  border-bottom: rgba(128, 128, 128, 100%) double 5px;
  border-top: rgba(128, 128, 128, 100%) double 5px;
}

.placeholder {
  height: 20px;
}

.label-container {
  margin-top: 20px;
  margin-bottom: 20px;
  padding: 10px;

  border-bottom: rgba(64, 64, 64, 100%) solid 3px;

}

</style>