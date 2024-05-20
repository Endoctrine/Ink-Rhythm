<script>
import BasicButton from "@/components/utils/BasicButton.vue";
import {store} from "@/store";
import {router} from "@/router";

export default {
  name: "Header",
  components: {BasicButton},
  computed: {
    store() {
      return store;
    }
  },
  methods: {
    navigateToSignup() {
      if (store.user === null) {
        router.push('/signup');
      } else {
        router.push('/users/' + store.user);
      }
    },
    navigateToHome() {
      router.push('/');
    },
    navigateToSearch() {
      router.push('/search');
    },
    navigateToImageHost() {
      router.push('/image-host');
    }
  },
}
</script>

<template>
  <div>
    <div class="container">
      <div class="header-item">
        <BasicButton @click="navigateToHome" class="button" :m-text="'**首页**'"/>
        <BasicButton @click="navigateToSearch" class="button" :m-text="'**搜索**'"/>
        <BasicButton @click="navigateToImageHost" class="button" :m-text="'**图床**'"/>
      </div>
      <div class="header-item">
        <BasicButton @click="navigateToSignup"
                     class="button"
                     :m-text="store.user === null ? '**登录&nbsp;/&nbsp;注册**' : '**欢迎你，' + store.user.toString() + '**'"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
}

.header-item {
  display: inline-block;
}

.button {
  display: inline-block;
}

</style>