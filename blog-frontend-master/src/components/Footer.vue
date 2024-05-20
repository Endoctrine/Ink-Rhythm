<script>
import TextViewer from '@/components/utils/TextViewer.vue'
export default {
  name: "Footer",
  components: {TextViewer}
}
</script>

<template>
<div>
  <div class="footer-container">
    <TextViewer
        class="footer-text"
        :m-font-size="20"
        :m-content="'© 2023 YouZheyin\'s blog'"
        :m-font-family="'HYWenHei-45W'"/>
  </div>
</div>
</template>

<style scoped>
  .footer-container{
    display: flex;
    align-items: center;
    justify-content: center;

    height: 100px;
    width: 100%;
  }

  .footer-text{
    color: rgba(128, 128, 128, 100%);
  }
</style>