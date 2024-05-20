<script>
export default {
  name: "TextEditor",
  props: {
    modelValue: {},
    mRows: {
      type: Number,
      default: null,
    },
    mCols: {
      type: Number,
      default: null,
    },
    mWidth: {
      type: Number,
      default: null
    },
    mHeight: {
      type: Number,
      default: null
    },
    mMaxlength: {
      type: Number,
      default: 16,
    },
    mPlaceholder: {
      type: String,
      default: '',
    },
    mPrivacyMode: {
      type: Boolean,
      default: false,
    },
    mFontSize: {
      type: Number,
      default: 26,
    },
    mLongTextMode: {
      type: Boolean,
      default: false,
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      rows: this.mRows === null ? 1 : this.mRows,
      cols: this.mCols === null ? 100 : this.mCols,
      width: this.mWidth === null ? 'auto' : this.mWidth + 'px',
      height: this.mHeight === null ? 'auto' : this.mHeight + 'px',
      maxlength: this.mMaxlength,
      placeholder: this.mPlaceholder,
      fontSize: this.mFontSize + 'px',
    }
  },
}
</script>

<template>
  <div>
    <div class="container">
      <textarea class="text-input" name="text"
                :class="{'text-input-security':mPrivacyMode}"
                :style="{width:width,
                height:height,
                fontSize:fontSize,
                whiteSpace:mLongTextMode?'normal':'nowrap'}"
                :placeholder="placeholder"
                :value="modelValue"
                :rows="rows"
                :cols="cols"
                :maxlength="maxlength"
                @input="$emit('update:modelValue', $event.target.value)"/>
      <span class="counter">{{ modelValue.length + '/' + maxlength }}</span>
    </div>
  </div>
</template>

<style scoped>
.container {
  position: relative;
  height: fit-content;
  width: fit-content;
}

.counter {
  position: absolute;
  bottom: 10px;
  right: 10px;

  font-size: 16px;
  color: rgba(128, 128, 128, 50%);

  pointer-events: none;
}

.text-input {
  padding: 5px;
  resize: none;

  width: 100%;
  outline: none;
  border-right: rgba(233, 233, 233, 50%) solid 2px;
  border-top: rgba(233, 233, 233, 50%) solid 2px;
  border-bottom: rgba(233, 233, 233, 50%) solid 2px;
  border-left: rgba(128, 128, 128, 50%) solid 10px;

  background: rgba(233, 233, 233, 50%);
  color: rgba(128, 128, 128, 100%);

  overflow-x: auto;

  transition: 0.3s;
}

.text-input-security {
  -webkit-text-security: disc;
}

.text-input::-webkit-scrollbar {
  display: none;
}

.text-input:focus {
  border-right: rgba(128, 128, 128, 50%) solid 2px;
  border-top: rgba(128, 128, 128, 50%) solid 2px;
  border-bottom: rgba(128, 128, 128, 50%) solid 2px;
  border-left: rgba(0, 0, 0, 50%) solid 10px;

  background: none;
  color: rgba(0, 0, 0, 100%);
}

.text-input:focus + .counter {
  color: rgba(128, 128, 128, 25%);
}
</style>