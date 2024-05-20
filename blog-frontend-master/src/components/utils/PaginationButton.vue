<script>
import BasicButton from "@/components/utils/BasicButton.vue";
import TextViewer from "@/components/utils/TextViewer.vue";

export default {
  name: "PaginationButton",
  components: {TextViewer, BasicButton},
  emits: ['update:modelValue'],
  props: {
    modelValue: {},
    mBegin: {
      type: Number,
      default: 0,
    },
    mTotal: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      iconLeft: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-left-fill" viewBox="0 0 16 16">\n' +
          '  <path d="m3.86 8.753 5.482 4.796c.646.566 1.658.106 1.658-.753V3.204a1 1 0 0 0-1.659-.753l-5.48 4.796a1 1 0 0 0 0 1.506z"/>\n' +
          '</svg>',
      iconRight: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-right-fill" viewBox="0 0 16 16">\n' +
          '  <path d="m12.14 8.753-5.482 4.796c-.646.566-1.658.106-1.658-.753V3.204a1 1 0 0 1 1.659-.753l5.48 4.796a1 1 0 0 1 0 1.506z"/>\n' +
          '</svg>',
    };
  },
}
</script>

<template>
  <div>
    <div class="container">
      <BasicButton @click="$emit('update:modelValue', modelValue - 1)"
                   :m-text="iconLeft"
                   :m-enable="modelValue > mBegin"/>
      <TextViewer class="page-viewer"
                  :m-content="modelValue.toString()+'&nbsp/&nbsp'+mTotal"
                  :m-font-size="20"/>
      <BasicButton @click="$emit('update:modelValue', modelValue + 1)"
                   :m-text="iconRight"
                   :m-enable="modelValue < mTotal"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: center;

  width: max-content;
}

.page-viewer {
  margin-left: 10px;
  margin-right: 10px;
}
</style>