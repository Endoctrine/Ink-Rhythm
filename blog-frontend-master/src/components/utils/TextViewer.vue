<script>
import {marked} from "marked";
import DOMPurify from "isomorphic-dompurify";

export default {
  name: "TextViewer",
  methods: {marked},
  created() {
    marked.use({
      gfm: true,
      breaks: true,
    });
  },
  props: {
    mContent: {
      type: String,
      default: 'default',
    },
    mFontFamily: {
      type: String,
      default: 'HYWenHei-45W',
    },
    mFontSize: {
      type: Number,
      default: 16,
    },
    mFontStyle: {
      type: String,
      default: '',
    },
    mWidth: {
      type: Number,
      default: null,
    },
    mHeight: {
      type: Number,
      default: null,
    },
  },
  data() {
    return {
      fontFamily: this.mFontFamily,
      fontSize: this.mFontSize,
      fontStyle: this.mFontStyle,
      width: this.mWidth,
      height: this.mHeight,
    }
  },
  computed: {
    htmlContent() {
      return DOMPurify.sanitize(marked.parse(this.content), {ALLOW_UNKNOWN_PROTOCOLS: true});
    },
    content() {
      return this.mContent;
    }
  },
}
</script>

<template>
  <div>
    <div class="html-container text-viewer-html-container"
         :style="{width: width == null ? 'auto' : width+'px',
         height: height == null ? 'auto' : height+'px',
         fontSize: fontSize+'px',
         fontFamily: fontFamily,
         fontStyle: fontStyle}" v-html="htmlContent">
    </div>
  </div>
</template>

<style scoped>

</style>

<style>
.text-viewer-html-container img {
  width: 100%;
  display: block;
}
</style>