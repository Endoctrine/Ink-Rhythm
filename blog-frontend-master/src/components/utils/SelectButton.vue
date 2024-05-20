<script>
import TextViewer from "@/components/utils/TextViewer.vue";

export default {
  name: "SelectButton",
  emits: ['update:modelValue'],
  components: {TextViewer},
  props: {
    modelValue: {},
    mText: {
      type: String,
      default: 'default',
    },
    mEnable: {
      type: Boolean,
      default: true,
    },
    mSize: {
      type: Number,
      default: null,
    },
  },
  data() {
    return {
      size: this.mSize === null ? 'auto' : this.mSize + 'px',
    }
  },
  computed: {
    selected() {
      return this.modelValue;
    }
  },
}
</script>

<template>
  <div>
    <div :class="!mEnable ? 'container-disabled' : selected ? 'container-selected' : 'container'"
         class="non-select"
         :style="{width:size, height:size}"
         @click="$emit('update:modelValue', !selected)">
      <TextViewer :m-content="mText" :m-font-size="20"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 0;
  padding: 5px;

  display: flex;
  justify-content: center;
  align-items: center;

  border: rgba(166, 166, 166, 30%) solid 5px;
  transition: 0.3s;
}

.container:hover {
  background: rgba(233, 233, 233, 50%);
  border: rgba(166, 166, 166, 50%) solid 5px;
  cursor: pointer;
}

.container-selected {
  margin: 0;
  padding: 5px;

  display: flex;
  justify-content: center;
  align-items: center;

  border: rgba(0, 0, 0, 100%) solid 5px;
  transition: 0.3s;
}

.container-selected:hover {
  background: rgba(233, 233, 233, 50%);
  cursor: pointer;
}

.container-disabled {
  margin: 0;
  padding: 10px;
  height: 50px;
  width: max-content;

  display: flex;
  justify-content: center;
  align-items: center;

  border: rgba(166, 166, 166, 30%) solid 5px;
  transition: 0.3s;

  color: rgba(128, 128, 128, 100%);
}

.container-disabled:hover {
  background: rgba(200, 200, 200, 100%);
  cursor: not-allowed;
}

.non-select {
  -moz-user-select: none; /*火狐*/
  -webkit-user-select: none; /*webkit浏览器*/
  -ms-user-select: none; /*IE10*/
  user-select: none;
}
</style>