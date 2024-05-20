<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import {store} from "@/store";
import {router} from "@/router";
import authService from "@/authService";
import axios from "axios";

export default {
  name: "UserShower",
  components: {BasicButton, TextViewer},
  emits: ['editProfile', 'auditHandled'],
  props: {
    mShownUser: {
      type: String,
      required: true,
    },
    mButtonLike: {
      type: Boolean,
      default: false,
    },
    mAuditMode: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      avatar: '',
      readCount: 0,
      fanCount: 0,
      bio: '',
      following: false,
    };
  },
  methods: {
    getData() {
      const self = this;
      axios.get('/api/user/profile?username='
          + this.mShownUser
          + '&currentUsername='
          + (store.user === null ? '' : store.user)).then(function (response) {
        self.avatar = '![](' + response.data.avatarURL + ')';
        self.readCount = response.data.readCount;
        self.fanCount = response.data.fanCount;
        self.bio = response.data.bio;
        self.following = response.data.following;
      }).catch(function (error) {
        console.log(error);
      })
    },
    navigateToUser() {
      if (this.mButtonLike) {
        router.push('/users/' + this.mShownUser);
      }
    },
    handleFollow() {
      if (this.following) {
        this.unfollow();
      } else {
        this.follow();
      }
    },
    handleAuditAgree() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      const self = this;
      axios.get('/api/audit/agreeUser?admin='
          + store.user
          + '&token='
          + store.token
          + '&user='
          + this.mShownUser).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          self.$emit('auditHandled');
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    handleAuditRefuse() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      const self = this;
      axios.get('/api/audit/refuseUser?admin='
          + store.user
          + '&token='
          + store.token
          + '&user='
          + this.mShownUser).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          self.$emit('auditHandled');
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    follow() {
      const self = this;
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/user/follow?user='
          + store.user
          + '&token='
          + store.token
          + '&followUser='
          + this.mShownUser).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          self.getData();
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
    unfollow() {
      const self = this;
      if (store.user === null) {
        authService.fail();
        return;
      }
      axios.get('/api/user/unfollow?user='
          + store.user
          + '&token='
          + store.token
          + '&followUser='
          + this.mShownUser).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          self.getData();
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
  },
  computed: {
    isSelf() {
      return this.mShownUser === store.user;
    },
    loggedIn() {
      return store.user !== null;
    }
  },
  mounted() {
    this.getData();
  },
  watch: {
    mShownUser() {
      this.getData();
    }
  },
}
</script>

<template>
  <div>
    <div :class="mButtonLike?'container-button-like':'container'">
      <div @click="navigateToUser"
           class="avatar-container"
           :class="{'avatar-button-like':mButtonLike}">
        <TextViewer :m-content="avatar"
                    :m-width="100"
                    :m-height="100"/>
      </div>
      <div class="info-container">
        <TextViewer :m-content="'**'+mShownUser+'**'"
                    :m-font-size="30"
                    :m-font-family="'HYWenHei-45W'"/>
        <TextViewer :style="{color:'rgba(128,128,128,100%)'}"
                    :m-content="'&nbsp;&nbsp;&quot;&nbsp;'+bio+'&nbsp;&quot;'"
                    :m-font-size="20"
                    :m-font-family="'HYWenHei-45W'"/>
        <TextViewer :style="{color:'rgba(128,128,128,100%)'}"
                    :m-content="'&nbsp;'+readCount+'&nbsp;阅读&nbsp;·&nbsp;'+fanCount+'&nbsp;粉丝'"
                    :m-font-size="20"/>
      </div>
      <div class="operation-button-container" v-if="mAuditMode">
        <BasicButton @click="handleAuditAgree" :m-text="'**同意**'"/>
        <BasicButton @click="handleAuditRefuse" :m-text="'**拒绝**'"/>
      </div>
      <div class="operation-button-container" v-else>
        <BasicButton v-if="!isSelf"
                     @click="handleFollow"
                     :m-enable="loggedIn"
                     :m-text="following ? '**取消关注**':'**+&nbsp;关注**'"/>
        <BasicButton v-if="isSelf"
                     @click="$emit('editProfile')"
                     :m-enable="loggedIn && !mButtonLike"
                     :m-text="'**编辑个人资料**'"/>
      </div>

    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  align-items: center;
  padding: 20px;

  box-shadow: 0 0 5px 5px rgba(233, 233, 233, 50%);
}

.container-button-like {
  display: flex;
  align-items: center;
  padding: 20px;
}

.avatar-button-like {
  transition: 0.3s;
}

.avatar-button-like:hover {
  border: rgba(64, 64, 64, 80%) double 10px;
  cursor: pointer;
}

.avatar-container {
  border: rgba(64, 64, 64, 50%) double 10px;
  flex-shrink: 0;

  border-radius: 50%;
  overflow: hidden;
}


.info-container {
  padding-left: 20px;
  padding-right: 20px;
}

.operation-button-container {
  margin-left: auto;
}
</style>