import Home from "@/components/Home.vue";
import SignUp from "@/components/SignUp.vue";
import LogIn from "@/components/LogIn.vue";
import UserProfile from "@/components/UserProfile.vue";
import Article from "@/components/Article.vue";
import Audit from "@/components/Audit.vue";
import Search from "@/components/Search.vue";
import Statistics from "@/components/Statistics.vue";
import {createRouter} from "vue-router";
import {createWebHashHistory} from "vue-router";
import ArticleEditor from "@/components/ArticleEditor.vue";
import ImageHost from "@/components/ImageHost.vue";

const routes = [
    {path: '/', component: Home},
    {path: '/signup', component: SignUp},
    {path: '/login/:id', component: LogIn},
    {path: '/users/:id', component: UserProfile},
    {path: '/article/:id', component: Article},
    {path: '/statistics', component: Statistics},
    {path: '/audit', component: Audit},
    {path: '/search', component: Search},
    {path: '/article-editor', component: ArticleEditor},
    {path: '/image-host', component: ImageHost},
];

export const router = createRouter({
    history: createWebHashHistory(),
    routes, // `routes: routes` 的缩写
});
