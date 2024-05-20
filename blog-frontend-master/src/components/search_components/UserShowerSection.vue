<script>
import PaginationButton from "@/components/utils/PaginationButton.vue";
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import UserShower from "@/components/user_profile_components/UserShower.vue";

export default {
  name: "UserShowerSection",
  components: {PaginationButton, BasicButton, TextViewer, UserShower},
  emits:['auditHandled'],
  props: {
    mShownUsers: {
      type: Array,
      default: null,
    },
    mUserPerPage: {
      type: Number,
      default: 1,
    },
    mAuditMode: {
      type: Boolean,
      default: false,
    }
  },
  data() {
    return {
      currentPage: 1,
    }
  },
  computed: {
    currentIndex() {
      return (this.currentPage - 1) * this.mUserPerPage;
    }
  },
  watch:{
    mShownUsers(){
      this.currentPage = 1;
    },
    mUserPerPage(){
      this.currentPage = 1;
    },
  },
}
</script>

<template>
  <div>
    <div class="container" v-if="mShownUsers !== null && mShownUsers.length > 0">
      <UserShower @audit-handled="$emit('auditHandled')"
                  class="container-item"
                  v-for="user in mShownUsers.slice(currentIndex, currentIndex + mUserPerPage)"
                  :m-shown-user="user"
                  :m-button-like="true"
                  :m-audit-mode="mAuditMode"/>
      <PaginationButton v-model="currentPage"
                        :m-begin="1"
                        :m-total="Math.floor((mShownUsers.length - 1) / mUserPerPage) + 1"/>
    </div>
    <div class="non-container" v-if="mShownUsers === null || mShownUsers.length === 0">
      <TextViewer :m-content="'==NULL=='"
                  :m-font-size="36"
                  :m-font-family="'HYWenHei-45W'"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  padding-top: 20px;
  padding-bottom: 20px;

  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  overflow: auto;
  box-shadow: 0 0 5px 5px rgba(233, 233, 233, 50%);
}

.container-item {
  width: 80%;
}

.non-container {
  width: 100%;
  padding-top: 20px;
  padding-bottom: 20px;

  display: flex;
  align-items: center;
  justify-content: center;

  box-shadow: 0 0 5px 5px rgba(233, 233, 233, 50%);
}
</style>