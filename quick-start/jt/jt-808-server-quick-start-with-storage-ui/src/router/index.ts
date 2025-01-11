import {createRouter, createWebHashHistory} from 'vue-router'
import AlarmInfo from "../pages/alarm-info.vue";
import TraceLog from "../pages/trace-log.vue";
import HomePage from "../pages/home-page.vue";
import TerminalList from "../pages/terminal-list.vue";

const routes = [
    {path: '/', component: HomePage},
    {path: '/terminals', component: TerminalList},
    {path: '/alarm-info', component: AlarmInfo},
    {path: '/trace-log', component: TraceLog},
]

export const router = createRouter({
    history: createWebHashHistory(),
    routes,
})
