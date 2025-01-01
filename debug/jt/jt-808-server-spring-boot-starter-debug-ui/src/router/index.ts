import {createRouter, createWebHistory} from 'vue-router'

import Debug from '../components/debug.vue'
import Debug1 from '../components/debug1.vue'

const routes = [
    { path: '/debug', component: Debug },
    { path: '/debug1', component: Debug1 },
]

export const router = createRouter({
    history: createWebHistory(),
    routes,
})