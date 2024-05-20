<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import Statistics from "@/components/Statistics.vue";

export default {
  name: "App",
  components: {
    Statistics,
    Footer,
    Header,
  },
}
</script>

<template>
  <Header/>
  <router-view></router-view>
  <Footer v-show="true"/>
</template>

<style scoped>
</style>