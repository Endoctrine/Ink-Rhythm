<script>
import TextEditor from "@/components/utils/TextEditor.vue";
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import authService from "@/authService";
import {router} from "@/router";
import {store} from "@/store";
import axios from "axios";

export default {
  name: "ArticleEditor",
  components: {BasicButton, TextViewer, TextEditor},
  data() {
    return {
      title: '输入标题',
      content: '输入内容',
      admin: false,
    };
  },
  methods: {
    getData() {
      if (store.user === null) {
        authService.fail();
        return;
      }
      const self = this;
      axios.get('/api/user/checkAdmin?username='
          + store.user).then(function (response) {
        self.admin = response.data;
      }).catch(function (error) {
        console.log(error);
      });
    },
    handleRelease(isAnnouncement) {
      if (store.user === null) {
        authService.fail();
        return;
      }
      const self = this;
      axios.post('/api/article/release', {
        username: store.user,
        token: store.token,
        announcement: isAnnouncement,
        title: self.title,
        content: self.content,
      }).then(function (response) {
        if (!response.data) {
          authService.fail();
        } else {
          router.back();
        }
      }).catch(function (error) {
        console.log(error);
      });
    },
  },
  mounted() {
    this.getData();
  }
}
</script>

<template>
  <div>
    <div class="container">
      <TextEditor v-model="title"
                  :m-font-size="50"
                  :m-rows="1"
                  :m-cols="32"
                  :m-maxlength="32"
                  :m-width="640"/>
      <div class="separator"></div>
      <div class="content-editor-container">
        <TextEditor v-model="content"
                    :m-font-size="23"
                    :m-rows="50"
                    :m-cols="32"
                    :m-maxlength="10240"
                    :m-width="300"
                    :m-height="1000"
                    :m-long-text-mode="true"/>
        <div class="content-preview-container">
          <TextViewer :m-content="content"
                      :m-font-size="20"/>
        </div>
      </div>
      <div class="button-container">
        <BasicButton @click="handleRelease(true)"
                     v-if="admin"
                     class="button"
                     :m-text="'**发布为公告**'"/>
        <BasicButton @click="handleRelease(false)"
                     class="button"
                     :m-text="'**发布**'"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 800px;
  margin-top: 50px;
  padding-top: 50px;
  padding-bottom: 50px;

  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;

  box-shadow: 0 0 5px 5px rgba(233, 233, 233, 50%);
}

.content-editor-container {
  display: flex;
  width: 640px;
}

.content-preview-container {
  margin-left: auto;
  padding-left: 10px;
  padding-right: 10px;
  padding-top: 5px;

  width: 300px;
  height: 1000px;

  overflow-y: auto;

  border: rgba(128, 128, 128, 50%) solid 3px;
}

.content-preview-container::-webkit-scrollbar {
  display: none;
}

.separator {
  width: 640px;
  height: 3px;

  margin-top: 20px;
  margin-bottom: 20px;

  background: rgba(64, 64, 64, 100%);
}

.button-container {
  margin-top: 20px;

  width: 640px;
  display: flex;
  justify-content: flex-end;
}

.button {
  box-shadow: 0 3px 3px 3px rgba(233, 233, 233, 50%);
}
</style>