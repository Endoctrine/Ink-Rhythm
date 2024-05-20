<script>
import SelectButton from "@/components/utils/SelectButton.vue";
import ToggleButton from "@/components/utils/ToggleButton.vue";
import ArticleShower from "@/components/home_components/ArticleShower.vue";
import ArticleShowerSection from "@/components/home_components/ArticleShowerSection.vue";
import UserShowerSection from "@/components/search_components/UserShowerSection.vue";

export default {
  name: "SearchResult",
  components: {UserShowerSection, ArticleShowerSection, ToggleButton, SelectButton, ArticleShower},
  props: {
    mArticleResult: {
      type: Array,
      required: true,
    },
    mUserResult: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      toggleState: 0,
      toggleTexts: ['**文章**', '**用户**'],
    };
  },
}
</script>

<template>
  <div>
    <div class="container">
      <div class="toggle-container">
        <ToggleButton v-model="toggleState" :m-texts="toggleTexts"/>
      </div>
      <div class="result-container">
        <ArticleShowerSection v-show="toggleState===0"
                              :m-article-i-ds="mArticleResult"
                              :m-article-per-page="3"/>
        <UserShowerSection v-show="toggleState===1"
                           :m-shown-users="mUserResult"
                           :m-user-per-page="5"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 800px;

  margin-top: 50px;
  margin-bottom: 20px;
}

.toggle-container {
  width: 800px;
  display: flex;
  border-bottom: rgba(64, 64, 64, 100%) solid 5px;
  margin-bottom: 20px;
}

.result-container {
  width: 800px;
}
</style>