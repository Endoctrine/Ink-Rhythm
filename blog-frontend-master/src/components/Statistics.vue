<script>
import TimeValueLine from "@/components/statistics_components/TimeValueLine.vue";
import TextViewer from "@/components/utils/TextViewer.vue";
import axios from "axios";

export default {
  name: "Statistics",
  components: {TextViewer, TimeValueLine},
  data() {
    return {
      newArticleCount: [0, 0, 0, 0, 0],
      newCommentCount: [0, 0, 0, 0, 0],
      labels: ['01-01', '01-02', '01-03', '01-04', '01-05'],
    };
  },
  methods: {
    getData() {
      const self = this;
      axios.get('/api/statistics').then(function (response) {
        self.newArticleCount = response.data.newArticleCount;
        self.newCommentCount = response.data.newCommentCount;
        self.labels = self.getLastFiveDays();
      }).catch(function (error) {
        console.log(error);
      })
    },
    getLastFiveDays() {
      let dates = [];
      for (let i = 4; i >= 0; i--) {
        let date = new Date();
        date.setDate(date.getDate() - i);
        let formattedDate = ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2);
        dates.push(formattedDate);
      }
      return dates;
    }
  },
  mounted() {
    this.getData();
  }
}
</script>

<template>
  <div>
    <div class="container">
      <div class="label-container">
        <TextViewer :m-content="'**系统数据统计**'"
                    :m-font-size="30"
                    :m-font-family="'HYWenHei-45W'"/>
      </div>
      <TimeValueLine :m-charts-data="[
          {values: newArticleCount, labels: labels, name: '新增文章'},
          {values: newCommentCount, labels: labels, name: '新增评论'},
      ]"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 800px;
  margin-top: 50px;
}

.label-container {
  margin-top: 20px;
  margin-bottom: 20px;
  padding: 10px;

  border-bottom: rgba(64, 64, 64, 100%) solid 3px;
}
</style>