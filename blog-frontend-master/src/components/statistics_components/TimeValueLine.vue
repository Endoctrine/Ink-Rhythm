<script>
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from 'chart.js';
import {Line} from 'vue-chartjs';
import html2canvas from "html2canvas";
import BasicButton from "@/components/utils/BasicButton.vue";

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    Filler,
);

export default {
  name: "TimeValueLine",
  components: {
    BasicButton,
    Line
  },
  props: {
    mChartsData: {
      type: Array,
      default: [{
        values: [],
        labels: [],
        name: '',
      }, {
        values: [],
        labels: [],
        name: '',
      }],
    }
  },
  data() {
    return {
      chartsOptions: [{
        responsive: true,
      }, {
        responsive: true,
      }],
    };
  },
  methods: {
    async exportToPNG() {
      const element = this.$refs.pngContent;
      const canvas = await html2canvas(element);
      const dataURL = canvas.toDataURL('image/png');
      const link = document.createElement('a');
      link.href = dataURL;
      link.download = 'download.png';
      link.click();
    }
  },
  computed:{
    chartsData(){
      return [{
        labels: this.mChartsData[0].labels,
        datasets: [{
          label: this.mChartsData[0].name,
          data: this.mChartsData[0].values,
          fill: true,
          borderColor: 'rgb(128, 128, 128)',
          tension: 0
        }]
      }, {
        labels: this.mChartsData[1].labels,
        datasets: [{
          label: this.mChartsData[1].name,
          data: this.mChartsData[1].values,
          fill: true,
          borderColor: 'rgb(128, 128, 128)',
          tension: 0
        }]
      }];
    }
  }
}
</script>
<template>
  <div>
    <div class="container">
      <div ref="pngContent" class="line-container">
        <Line :data="chartsData[0]"
              :options="chartsOptions[0]"/>
        <div class="placeholder"></div>
        <Line :data="chartsData[1]"
              :options="chartsOptions[1]"/>
      </div>
      <BasicButton @click="exportToPNG"
                   class="button"
                   :m-text="'**导出为PNG**'"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding: 20px 0;
  box-shadow: 0 0 5px 5px rgba(233, 233, 233, 50%);
}

.line-container {
  padding: 20px;
  width: 640px;
}

.placeholder {
  height: 50px;
}
</style>