<script>
import SignUp0 from "@/components/sign_up_components/SignUp0.vue";
import SignUp1 from "@/components/sign_up_components/SignUp1.vue";
import SignUp2 from "@/components/sign_up_components/SignUp2.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import {router} from "@/router";
import axios from "axios";
import sha256 from "crypto-js/sha256";

export default {
  name: "SignUp",
  data() {
    return {
      currentState: 0,
      userName: null,
      userPassword: null,
      userFollow: [],
      registered: false,
    }
  },
  components: {
    SignUp0,
    SignUp1,
    SignUp2,
    BasicButton,
  },
  methods: {
    async nextStep(eventValue) {
      if (this.currentState === 0) {
        this.registered = await this.checkRegistered(eventValue);
        if (this.registered) {
          await router.push('/login/' + eventValue);
        } else {
          this.userName = eventValue;
          this.currentState++;
        }
      } else if (this.currentState === 1) {
        this.userPassword = eventValue;
        this.currentState++;
      } else if (this.currentState === 2) {
        await this.signUpUser(eventValue);
        await router.push('/');
      } else {
        console.warn('SingUp:unexpected state!');
      }
    },
    async checkRegistered(userName) {
      try {
        const response = await axios.get('/api/signup/exists?user=' + userName);
        return response.data;
      } catch (error) {
        console.log(error);
      }
    },
    signUpUser(userFollowInfo) {
      for (let i = 0; i < userFollowInfo.users.length; i++) {
        if (userFollowInfo.selection[i]) {
          this.userFollow.push(userFollowInfo.users[i]);
        }
      }
      const self = this;
      axios.post('/api/signup', {
        username: self.userName,
        password: sha256(self.userPassword.toString() + self.userName.toString()).toString(),
        userFollow: self.userFollow,
      }).then(function (response) {
        // do nothing
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
      <SignUp0 v-if="currentState === 0" @next-step="nextStep"/>
      <SignUp1 v-if="currentState === 1" @next-step="nextStep"/>
      <SignUp2 v-if="currentState === 2" @next-step="nextStep"/>
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
</style>