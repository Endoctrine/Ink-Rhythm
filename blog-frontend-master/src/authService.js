import {store} from "@/store";
import {router} from "@/router";

export default {
    async fail() {
        store.logout();
        await router.push('/signup');
    }
}