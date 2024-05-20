<script>
import SelectButton from "@/components/utils/SelectButton.vue";

export default {
  name: "SelectFavorite",
  components: {SelectButton},
  emits: ['changeSelection'],
  props: {
    mUserAvatars: {
      type: Array,
      default: [],
    }
  },
  data() {
    return {
      selectStates: Array(this.mUserAvatars.length).fill(false),
    }
  },
  methods: {
    range(from, to) {
      let result = [];
      for (let i = from; i < to; i++) {
        result.push(i);
      }
      return result;
    }
  },
  watch: {
    selectStates: {
      handler(val) {
        this.$emit('changeSelection', val);
      },
      deep: true,
    },
    mUserAvatars: {
      handler(val) {
        this.selectStates = Array(this.mUserAvatars.length).fill(false)
      },
      deep: true,
    }
  },
}
</script>

<template>
  <div>
    <div class="container">
      <SelectButton v-for="i in range(0, Math.floor((mUserAvatars.length + 1) / 2))"
                    v-model="selectStates[i]"
                    :m-text="mUserAvatars[i]"
                    :m-enable="true"
                    :m-size="80"
                    :style="{marginLeft:'10px', marginRight:'10px'}"/>
    </div>
    <div class="container">
      <SelectButton v-for="i in range(Math.floor((mUserAvatars.length + 1) / 2), mUserAvatars.length)"
                    v-model="selectStates[i]"
                    :m-text="mUserAvatars[i]"
                    :m-enable="true"
                    :m-size="80"
                    :style="{marginLeft:'10px', marginRight:'10px'}"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  margin-top: 10px;
  margin-bottom: 10px;

  justify-content: center;
}
</style>