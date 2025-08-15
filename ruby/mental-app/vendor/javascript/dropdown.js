import * as Vue from "vue";

const Dropdown = Vue.createApp({
  data(){
      return {
        isOpen: false,
        backgroundColor: '#aef5a3',
      }
  },
  methods: {
    open() {
      this.isOpen = !this.isOpen;
    },
    mouseOverAction(){
      this.backgroundColor = "#96dc78";
    },
    mouseLeaveAction(){
      this.backgroundColor = "#aef5a3";
    }
  },
  mounted() {
    // windowにイベントリスナーをセットする
    window.addEventListener('click', this._onBlurHandler = (event) => {
      // targetがコンポーネントの中に含まれているものなら何もしない
      if (this.$refs.elRoot.contains(event.target)) {
        return;
      }
      this.$data.isOpen = false;
    });
  },
  beforeDestroy() {
    // コンポーネントが破棄されるタイミングにイベントリスナーも消す
    window.removeEventListener('click', this._onBlurHandler);
  }
})

Dropdown.mount("#vue-navbar");



const Dropdown02 = Vue.createApp({
  data(){
      return {
        isOpen: false,
        backgroundColor: '#f5f5f5',
      }
  },
  methods: {
    open() {
      this.isOpen = !this.isOpen;
    },
    mouseOverAction(){
      this.backgroundColor = "#e5e5e5";
    },
    mouseLeaveAction(){
      this.backgroundColor = "#f5f5f5";
    }
  },
  mounted() {
    // windowにイベントリスナーをセットする
    window.addEventListener('click', this._onBlurHandler = (event) => {
      // targetがコンポーネントの中に含まれているものなら何もしない
      if (this.$refs.elRoot.contains(event.target)) {
        return;
      }
      this.$data.isOpen = false;
    });
  },
  beforeDestroy() {
    // コンポーネントが破棄されるタイミングにイベントリスナーも消す
    window.removeEventListener('click', this._onBlurHandler);
  }
})

Dropdown02.mount("#vue-navbar02");