import {createRouter, createWebHashHistory} from 'vue-router'
import Home from "../pages/home.vue";
import FlvPlayerDemo from "../pages/flv-player-demo.vue";
import FlvPlayerDemoWith808Sessions from '../pages/flv-player-demo-with-808-sessions.vue';

const routes = [
    {path: '/home', component: Home},
    {path: '/flv-player', component: FlvPlayerDemo},
    {path: '/flv-player-with-808-sessions', component: FlvPlayerDemoWith808Sessions},
]

export const router = createRouter({
    history: createWebHashHistory(),
    routes,
})
