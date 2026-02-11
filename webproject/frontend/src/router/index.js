import { createRouter, createWebHistory } from 'vue-router'
import HomeView from "@/views/HomeView.vue";
import ShoppingCartView from "@/views/ShoppingCartView.vue";
import ShopView from "@/views/ShopView.vue";
import SigninView from "@/views/SigninView.vue";
import AdminView from "@/views/AdminView.vue";
import LoginView from "@/views/LoginView.vue"
import ProfileView from "@/views/ProfileView.vue";


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "home",
            component: HomeView
        },
        {
            path: "/cart",
            name: "cart",
            component: ShoppingCartView
        },
        {
            path: "/shop",
            name: "Shop",
            component: ShopView
        },
        {
            path: "/signin",
            name: "signin",
            component: SigninView
        },
        {
            path: "/admin",
            name: "Admin",
            component: AdminView
        },
        {
            path: "/login",
            name: "login",
            component: LoginView
        },
        {
            path: "/profile",
            name: "profile",
            component: ProfileView
        }
    ]
})

export default router
