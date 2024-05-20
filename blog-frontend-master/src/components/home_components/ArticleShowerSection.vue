<script>
import ArticleShower from "@/components/home_components/ArticleShower.vue";
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import PaginationButton from "@/components/utils/PaginationButton.vue";

export default {
  name: "ArticleShowerSection",
  components: {PaginationButton, BasicButton, TextViewer, ArticleShower},
  emits: ['auditHandled'],
  props: {
    mArticleIDs: {
      type: Array,
      required: true,
    },
    mArticlePerPage: {
      type: Number,
      default: 1,
    },
    mAuditMode: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      currentPage: 1,
    }
  },
  computed: {
    currentArticle() {
      return (this.currentPage - 1) * this.mArticlePerPage;
    }
  },
  watch: {
    mArticleIDs() {
      this.currentPage = 1;
    },
    mArticlePerPage() {
      this.currentPage = 1;
    }
  },
}
</script>

<template>
  <div>
    <div class="container" v-if="mArticleIDs !== null && mArticleIDs.length > 0">
      <ArticleShower @audit-handled="$emit('auditHandled')"
                     class="container-item"
                     v-for="id in mArticleIDs.slice(currentArticle, currentArticle + mArticlePerPage)"
                     :m-article-i-d="id"
                     :m-audit-mode="mAuditMode"/>
      <PaginationButton v-model="currentPage"
                        :m-begin="1"
                        :m-total="Math.floor((mArticleIDs.length - 1) / mArticlePerPage) + 1"/>
    </div>
    <div class="non-container" v-if="mArticleIDs === null || mArticleIDs.length === 0">
      <TextViewer :m-content="'==NULL=='"
                  :m-font-size="36"
                  :m-font-family="'HYWenHei-45W'"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  padding-top: 20px;
  padding-bottom: 20px;

  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  overflow: auto;
  box-shadow: 0 0 5px 5px rgba(233, 233, 233, 50%);
}

.container-item {
  width: 80%;
}

.non-container {
  width: 100%;
  padding-top: 20px;
  padding-bottom: 20px;

  display: flex;
  align-items: center;
  justify-content: center;

  box-shadow: 0 0 5px 5px rgba(233, 233, 233, 50%);
}

</style>