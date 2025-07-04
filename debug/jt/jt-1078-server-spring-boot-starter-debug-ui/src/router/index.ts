import {createRouter, createWebHistory} from 'vue-router'
import Home from "../pages/home.vue";
import FlvPlayerDemo from "../pages/flv-player-demo.vue";

const routes = [
    {path: '/home', component: Home},
    {path: '/flv-player', component: FlvPlayerDemo},
]

export const router = createRouter({
    history: createWebHistory(),
    routes,
})
