<script>
import axios from "axios";
import TextViewer from "@/components/utils/TextViewer.vue";
import BasicButton from "@/components/utils/BasicButton.vue";
import {store} from "@/store";
import authService from "@/authService";

export default {
  name: "ImageHost",
  computed: {
    store() {
      return store
    }
  },
  components: {BasicButton, TextViewer},
  data() {
    return {
      images: [],
      uploadImg: null,
      imgURL: null,
    }
  },
  methods: {
    getData() {
      const self = this;
      axios.get('/pb/all').then(function (response) {
        self.images = response.data.message;
      }).catch(function (error) {
        console.log(error);
      })
    },
    selectImage() {
      let inputList = document.getElementsByClassName('img-input');
      let input = inputList[0];
      let imgFile = input.files[0];
      if (imgFile === null || imgFile === undefined) {
        this.uploadImg = null;
        this.imgURL = null;
        return;
      }
      let fileURL = URL.createObjectURL(imgFile);
      this.imgURL = '![' + imgFile.name + '](' + fileURL + ')';

      let self = this;
      let reader = new FileReader();
      reader.addEventListener(
          "load",
          function () {
            self.uploadImg = reader.result;
          },
          false,
      );
      reader.readAsDataURL(imgFile);
    },
    uploadImage() {
      if (store.user === null) {
        authService.fail();
      }
      let self = this;
      axios.get('/api/user/checkValid?username='
          + store.user
          + '&token='
          + store.token).then(function (response) {
        if (response.data) {
          let imgBase64 = self.uploadImg.toString();
          axios.post('/pb/upload', {
            image: imgBase64,
          }).then(function () {
            window.location.reload();
          }).catch(function (error) {
            console.log(error);
          });
        } else {
          authService.fail();
        }
      }).catch(function (error) {
        console.log(error);
      })
    },
    handleUpload() {
      if (this.uploadImg === null) {
        let inputList = document.getElementsByClassName('img-input');
        let input = inputList[0];
        input.click();
      } else {
        this.uploadImage();
      }
    },
    handleNonSelect() {
      this.uploadImg = null;
      this.imgURL = null;
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
      <div class="img-selector">
        <input name="inputImg" class="img-input" type="file" @change="selectImage">
        <TextViewer :m-content="imgURL === null ? '未选择图片' : imgURL"/>
        <div class="button-container">
          <BasicButton @click="handleUpload"
                       :m-enable="store.user !== null"
                       :m-text="uploadImg === null ? '**选择图片上传**' : '**上传图片**'"/>
          <BasicButton @click="handleNonSelect"
                       v-show="uploadImg !== null"
                       :m-text="'**取消选择**'"
                       :m-enable="store.user !== null"/>
        </div>
      </div>
      <div v-for="img of images" class="img-container">
        <TextViewer :m-content="'![' + img + '](http://localhost:3000/images/' + img + ')'"/>
        <div class="placeholder"></div>
        <TextViewer style="overflow-wrap: normal" :m-content="img"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  margin-top: 50px;
  padding-top: 20px;
  padding-bottom: 50px;
}

.img-input {
  display: none;
}

.img-selector {
  width: 640px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;

  margin-bottom: 50px;
}

.button-container {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.img-container {
  width: 400px;
  margin-bottom: 50px;
  padding: 20px;

  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;

  transition: 0.3s;
}

.img-container:hover {
  background: rgba(233, 233, 233, 50%);
}

.placeholder {
  height: 10px;
}
</style>