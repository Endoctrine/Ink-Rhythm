<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import {router} from "@/router";

export default {
  name: "ArticleHeader",
  components: {BasicButton, TextViewer},
  props: {
    mArticleTitle: {
      type: String,
      required: true,
    },
    mAuthor: {
      type: String,
      required: true,
    },
    mReleaseTime: {
      type: String,
      required: true,
    },
    mLikeCount: {
      type: Number,
      required: true,
    },
    mBookmarkCount: {
      type: Number,
      required: true,
    },
    mViewCount: {
      type: Number,
      required: true,
    },
  },
  methods: {
    navigateToUser() {
      router.push('/users/' + this.mAuthor);
    }
  }
}
</script>

<template>
  <div>
    <div class="container">
      <TextViewer :m-content="mArticleTitle"
                  :m-font-family="'HYWenHei-45W'"
                  :m-font-size="60"/>
      <div class="separator"></div>
      <div class="info-container">
        <TextViewer :m-content="'|'"
                    :m-font-size="20"
                    :style="{color:'rgba(128,128,128,100%)'}"/>
        <BasicButton :m-text="mAuthor" @click="navigateToUser"/>
        <TextViewer :m-content="'|&nbsp;&nbsp;'
                                + mReleaseTime
                                +'&nbsp;&nbsp;|&nbsp;👀'
                                + mViewCount
                                +'&nbsp;&nbsp;|&nbsp;👍'
                                + mLikeCount
                                +'&nbsp;&nbsp;|&nbsp;⭐'
                                + mBookmarkCount +'&nbsp;&nbsp;|'"
                    :m-font-size="20"
                    :style="{color:'rgba(128,128,128,100%)'}"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
}

.separator {
  width: 100%;
  height: 3px;
  background: rgba(64, 64, 64, 100%);
}

.info-container {
  display: flex;

  align-items: center;
}
</style>