<script>
import TextViewer from "@/components/utils/TextViewer.vue";

export default {
  name: "ToggleButton",
  emits: ['update:modelValue'],
  components: {TextViewer},
  props: {
    modelValue: {},
    mTexts: {
      type: Array,
      default: []
    },
  },
}
</script>

<template>
  <div>
    <div class="container">
      <div class="button-container non-select"
           :class="modelValue===index ? 'container-selected' : 'container-unselected'"
           v-for="(item, index) in mTexts"
           @click="$emit('update:modelValue', index)">
        <TextViewer :m-content="item"
                    :m-font-size="20"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
}

.button-container {
  padding-left: 20px;
  padding-right: 20px;
  margin-left: 5px;
  margin-right: 5px;

  display: flex;
  align-items: center;
  justify-content: center;
  transition: 0.3s;
}

.container-unselected {
  border-top: rgba(166, 166, 166, 30%) solid 5px;
  border-left: rgba(166, 166, 166, 30%) solid 5px;
  border-right: rgba(166, 166, 166, 30%) solid 5px;
}

.container-unselected:hover {
  background: rgba(233, 233, 233, 50%);
  border-top: rgba(166, 166, 166, 50%) solid 5px;
  border-left: rgba(166, 166, 166, 50%) solid 5px;
  border-right: rgba(166, 166, 166, 50%) solid 5px;
}

.container-selected {
  border-top: rgba(0, 0, 0, 100%) solid 5px;
  border-left: rgba(0, 0, 0, 100%) solid 5px;
  border-right: rgba(0, 0, 0, 100%) solid 5px;
}

.container-selected:hover {
  background: rgba(233, 233, 233, 50%);
}

.non-select {
  -moz-user-select: none; /*火狐*/
  -webkit-user-select: none; /*webkit浏览器*/
  -ms-user-select: none; /*IE10*/
  user-select: none;
}
</style>