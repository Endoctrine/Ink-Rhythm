<script>
import TextEditor from "@/components/utils/TextEditor.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import SearchResult from "@/components/search_components/SearchResult.vue";
import axios from "axios";

export default {
  name: "Search",
  components: {SearchResult, BasicButton, TextEditor},
  data() {
    return {
      searchContent: '',
      isFirstSearch: true,
      articleResult: [],
      userResult: [],
    };
  },
  methods: {
    handleSearch() {
      if (this.isFirstSearch) {
        this.isFirstSearch = false;
      }
      const self = this;
      axios.get('/api/search?content=' + this.searchContent).then(function (response) {
        self.articleResult = response.data.articleResult;
        self.userResult = response.data.userResult;
      }).catch(function (error) {
        console.log(error);
      })
    }
  }
}
</script>

<template>
  <div>
    <div class="container">
      <div class="search-head-container" :class="{'full-search-head':isFirstSearch}">
        <div class="search-container">
          <TextEditor v-model="searchContent"
                      @keydown.enter.prevent="handleSearch"
                      :m-cols="32"
                      :m-rows="1"
                      :m-maxlength="32"
                      :m-width="640" :m-font-size="28"/>
          <div class="search-button-container">
            <BasicButton @click="handleSearch"
                         :style="{borderRight:'rgba(233, 233, 233, 50%) solid 3px',
                                borderLeft:'rgba(233, 233, 233, 50%) solid 3px'}"
                         :m-text="'**搜索**'"/>
          </div>
        </div>
      </div>
      <SearchResult v-show="!isFirstSearch"
                    :m-article-result="articleResult"
                    :m-user-result="userResult"/>
    </div>
  </div>
</template>

<style scoped>
.search-head-container {
  margin-top: 50px;

  display: flex;
  align-items: center;
  justify-content: center;
}

.full-search-head {
  height: 500px;
}

.search-container {
  padding: 50px 50px;

  display: flex;
  align-items: center;
  flex-direction: column;

  width: 800px;

  border-bottom: rgba(128, 128, 128, 50%) solid 5px;
  border-top: rgba(233, 233, 233, 50%) solid 5px;
  border-left: rgba(233, 233, 233, 0%) solid 2px;
  border-right: rgba(233, 233, 233, 0%) solid 2px;

  background: rgba(233, 233, 233, 20%);

  transition: 0.3s;
}

.search-container:hover {
  border-bottom: rgba(128, 128, 128, 100%) solid 5px;
  border-top: rgba(233, 233, 233, 100%) solid 5px;
  border-left: rgba(200, 200, 200, 100%) solid 2px;
  border-right: rgba(200, 200, 200, 100%) solid 2px;

  background: rgba(233, 233, 233, 50%);
}

.search-button-container {
  width: 640px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
</style>