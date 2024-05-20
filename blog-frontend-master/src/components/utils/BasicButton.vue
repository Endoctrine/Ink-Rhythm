<script>
import TextViewer from "@/components/utils/TextViewer.vue";

export default {
  name: "BasicButton",
  emits: ['click'],
  components: {TextViewer},
  props: {
    mText: {
      type: String,
      default: 'default',
    },
    mEnable: {
      type: Boolean,
      default: true,
    },
    mHeight: {
      type: Number,
      default: null,
    },
  },
  data() {
    return {
      fontSize: 20,
      height: this.mHeight === null ? '50px' : this.mHeight + 'px',
    }
  },
  computed: {
    enable() {
      return this.mEnable;
    },
    text() {
      return this.mText;
    },
  },
}
</script>

<template>
  <div>
    <div class="non-select"
         @click="()=>{
           if(enable){
             $emit('click');
           }
         }"
         :style="{height:height}"
         :class="enable ? 'container' : 'container-disabled'">
      <TextViewer :m-content="text.toString()" :m-font-size="fontSize"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  margin: 0;
  padding-left: 20px;
  padding-right: 20px;
  width: max-content;

  display: flex;
  justify-content: center;
  align-items: center;

  border-top: rgba(0, 0, 0, 0) solid 5px;
  border-bottom: rgba(0, 0, 0, 0) solid 5px;
  transition: 0.3s;
}

.container:hover {
  background: rgba(233, 233, 233, 50%);
  border-bottom: rgba(166, 166, 166, 50%) solid 5px;
  cursor: pointer;
}

.container-disabled {
  margin: 0;
  padding-left: 20px;
  padding-right: 20px;
  width: max-content;

  display: flex;
  justify-content: center;
  align-items: center;

  border-top: rgba(0, 0, 0, 0) solid 5px;
  border-bottom: rgba(0, 0, 0, 0) solid 5px;
  transition: 0.3s;

  color: rgba(128, 128, 128, 100%);
}

.container-disabled:hover {
  background: rgba(233, 233, 233, 100%);
  cursor: not-allowed;
}

.non-select {
  -moz-user-select: none; /*火狐*/
  -webkit-user-select: none; /*webkit浏览器*/
  -ms-user-select: none; /*IE10*/
  user-select: none;
}
</style>