<script>
import BasicButton from "@/components/utils/BasicButton.vue";
import TextViewer from "@/components/utils/TextViewer.vue";
import TextEditor from "@/components/utils/TextEditor.vue";
import {store} from "@/store";
import {router} from "@/router";
import axios from "axios";
import sha256 from "crypto-js/sha256"

export default {
  name: "LogIn",
  components: {TextEditor, TextViewer, BasicButton},
  data() {
    return {
      userPassword: '',
      user: this.$route.params.id,
      success: false,
      token: '',
    }
  },
  methods: {
    logIn() {
      const self = this;
      axios.get('/api/login?user='
          + this.user
          + '&password='
          + sha256(this.userPassword.toString() + this.user.toString()).toString()).then(function (response) {
        self.success = response.data.success;
        if (self.success) {
          self.token = response.data.token;
          store.login(self.user, self.token);
          router.push('/');
        } else {
          self.userPassword = '';
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
  },
}
</script>

<template>
  <div>
    <div class="container">
      <div class="login-container">
        <TextViewer
            :m-font-size="60"
            :m-content="'Welcome'"
            :m-font-family="'Saira Extra Condensed'"/>
        <TextEditor
            @keydown.enter.prevent="logIn"
            :m-cols="32"
            :m-rows="1"
            :m-maxlength="32"
            :m-width="300"
            :m-placeholder="'请输入密码'"
            :m-privacy-mode="true"
            v-model="userPassword"/>
        <div class="button-container">
          <BasicButton @click="logIn" :m-text="'**下一步**'"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;

  margin-top: 130px;
}

.login-container {
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

.login-container:hover {
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