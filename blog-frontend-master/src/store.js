import {reactive} from "vue";

export const store = reactive({
    user: localStorage.getItem('user'),
    token: localStorage.getItem('token'),

    login(u, t) {
        this.user = u;
        this.token = t;
        localStorage.setItem('user', u);
        localStorage.setItem('token', t);
    },

    logout() {
        this.user = null;
        this.token = null;
        localStorage.removeItem('user');
        localStorage.removeItem('token');
    },
});
