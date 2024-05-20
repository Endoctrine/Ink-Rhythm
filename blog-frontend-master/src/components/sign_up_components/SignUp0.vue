<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import TextEditor from "@/components/utils/TextEditor.vue";
import BasicButton from "@/components/utils/BasicButton.vue";

export default {
  name: "SignUp0",
  emits: ['nextStep', 'update:modelValue'],
  components: {
    TextViewer,
    TextEditor,
    BasicButton,
  },
  data() {
    return {
      userName: '',
    };
  },
}
</script>

<template>
  <div>
    <div class="container">
      <TextViewer
          :m-font-size="60"
          :m-content="'Hello World'"
          :m-font-family="'Saira Extra Condensed'"/>
      <TextEditor
          @keydown.enter.prevent="$emit('nextStep', userName)"
          :m-cols="32"
          :m-rows="1"
          :m-maxlength="32"
          :m-width="300"
          :m-placeholder="'请输入用户名'"
          v-model="userName"/>
      <div class="button-container">
        <BasicButton @click="$emit('nextStep', userName)"
                     :m-text="'**下一步**'"
                     :m-enable="userName !== null && userName.length > 0"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 500px;
  height: 400px;
  display: grid;

  border-bottom: rgba(128, 128, 128, 50%) solid 5px;
  border-top: rgba(233, 233, 233, 50%) solid 5px;
  border-left: rgba(233, 233, 233, 0%) solid 2px;
  border-right: rgba(233, 233, 233, 0%) solid 2px;

  background: rgba(233, 233, 233, 20%);

  align-items: center;
  justify-content: center;

  transition: 0.3s;
}

.container:hover {
  border-bottom: rgba(128, 128, 128, 100%) solid 5px;
  border-top: rgba(233, 233, 233, 100%) solid 5px;
  border-left: rgba(200, 200, 200, 100%) solid 2px;
  border-right: rgba(200, 200, 200, 100%) solid 2px;

  background: rgba(233, 233, 233, 50%);
}

.button-container {
  display: flex;
  justify-content: flex-end;
}
</style>