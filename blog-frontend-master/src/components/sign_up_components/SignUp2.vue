<script>
import BasicButton from "@/components/utils/BasicButton.vue";
import TextViewer from "@/components/utils/TextViewer.vue";
import SelectFavorite from "@/components/sign_up_components/SelectFavorite.vue";
import axios from "axios";

export default {
  name: "SignUp2",
  components: {SelectFavorite, TextViewer, BasicButton},
  emits: ['nextStep'],
  data() {
    return {
      recommendedUsers: [],
      recommendedUserAvatars: [],
      selection: [],
    }
  },
  methods: {
    getData() {
      this.selection = [];
      const self = this;
      axios.get('/api/signup/recommend').then(function (response) {
        self.recommendedUsers = response.data.recommendedUsers;
        let avatars = response.data.recommendedUserAvatars;
        for (let i = 0; i < avatars.length; i++) {
          avatars[i] = '![](' + avatars[i] + ')';
        }
        self.recommendedUserAvatars = avatars;
      }).catch(function (error) {
        console.log(error);
      });
    },
    changeSelection(eventValue) {
      this.selection = eventValue;
    },
    nextStep() {
      this.$emit('nextStep', {users: this.recommendedUsers, selection: this.selection});
    },
  },
  mounted() {
    this.getData();
  },
}
</script>

<template>
  <div>
    <div class="container">
      <div class="button-container">
        <BasicButton @click="getData" :m-text="'**换一批**'"/>
      </div>
      <SelectFavorite @change-selection="changeSelection"
                      :m-user-avatars="recommendedUserAvatars"/>
      <div class="button-container">
        <BasicButton @click="nextStep"
                     :m-text="'**下一步**'"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 800px;
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