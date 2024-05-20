<script>
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import TextEditor from "@/components/utils/TextEditor.vue";
import {store} from "@/store";
import axios from "axios";
import authService from "@/authService";

export default {
  name: "UserEditor",
  components: {TextEditor, BasicButton, TextViewer},
  emits: ['saveProfile'],
  props: {
    mShownUser: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      avatar: '',
      bio: '',
    };
  },
  methods: {
    selectAvatar() {
      let inputList = document.getElementsByClassName('avatar-input');
      let input = inputList[0];
      let avatarFile = input.files[0];
      if (avatarFile === null || avatarFile === undefined) {
        this.avatarImg = null;
        return;
      }
      let fileURL = URL.createObjectURL(avatarFile);
      this.avatar = '![' + avatarFile.name + '](' + fileURL + ')';

      let self = this;
      let reader = new FileReader();
      reader.addEventListener(
          "load",
          function () {
            self.avatarImg = reader.result;
          },
          false,
      );
      reader.readAsDataURL(avatarFile);
    },
    saveUserProfile() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      const self = this;
      if (this.avatarImg !== undefined && this.avatarImg !== null) {
        let avatarBase64 = this.avatarImg.toString();
        this.avatarURL = '';
        axios.post('/pb/upload', {
          image: avatarBase64,
        }).then(function (response) {
          self.avatarURL = 'http://localhost:3000/' + response.data.message;
          axios.post('/api/user/edit', {
            username: store.user,
            token: store.token,
            avatarURL: self.avatarURL,
            bio: self.bio,
          }).then(function (response) {
            if (!response.data) {
              authService.fail();
            } else {
              self.$emit('saveProfile');
            }
          }).catch(function (error) {
            console.log(error);
          });
        }).catch(function (error) {
          console.log(error);
        });
      } else {
        axios.post('/api/user/edit', {
          username: store.user,
          token: store.token,
          avatarURL: self.avatar.replace(/!\[.*?]\(/g, "").replace(/\)/g, ""),
          bio: self.bio,
        }).then(function (response) {
          if (!response.data) {
            authService.fail();
          } else {
            self.$emit('saveProfile');
          }
        }).catch(function (error) {
          console.log(error);
        });
      }
    },
    getData() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      const self = this;
      axios.get('/api/user/profile?username='
          + this.mShownUser
          + '&currentUsername='
          + store.user).then(function (response) {
        self.avatar = '![](' + response.data.avatarURL + ')';
        self.bio = response.data.bio === null ? 'null' : response.data.bio;
      }).catch(function (error) {
        console.log(error);
      })
    },
  },
  computed: {
    store() {
      return store;
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
      <div class="avatar-container">
        <TextViewer :m-content="avatar"
                    :m-width="100"
                    :m-height="100"/>
        <form>
          <input name="avatar" class="avatar-input" type="file" @change="selectAvatar">
          <TextViewer class="avatar-edit-hint non-select"
                      :style="{color:'rgba(233, 233, 233, 100%)'}"
                      :m-content="'修改头像'"
                      :m-font-size="18"/>
        </form>
      </div>
      <div class="info-container">
        <TextViewer :m-content="'**'+store.user+'**'"
                    :m-font-size="30"
                    :m-font-family="'HYWenHei-45W'"/>
        <TextEditor v-model="bio"
                    :m-cols="32"
                    :m-rows="1"
                    :m-font-size="20"
                    :m-maxlength="64"/>
      </div>
      <BasicButton class="follow-button"
                   @click="saveUserProfile"
                   :m-text="'**编辑完成**'"/>
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

.avatar-container {
  position: relative;
  border: rgba(64, 64, 64, 50%) double 10px;

  border-radius: 50%;
  overflow: hidden;
}

.avatar-edit-hint {
  display: flex;
  align-items: center;
  justify-content: center;

  width: 100px;
  height: 100px;

  position: absolute;
  top: 0;
  left: 0;

  opacity: 0;
  transition: 0.3s;
  pointer-events: none;
}

.avatar-input:hover + .avatar-edit-hint {
  opacity: 50%;
  background: rgba(64, 64, 64, 100%);
}

.avatar-input {
  position: absolute;
  top: 0;
  left: 0;
  font-size: 100px;
  opacity: 0;
}

.info-container {
  padding-left: 20px;
  padding-right: 20px;
}

.follow-button {
  margin-left: auto;
}

.non-select {
  -moz-user-select: none; /*火狐*/
  -webkit-user-select: none; /*webkit浏览器*/
  -ms-user-select: none; /*IE10*/
  user-select: none;
}
</style>