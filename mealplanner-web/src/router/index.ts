import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import Recipes from "@/views/Recipes.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "Recipes",
    component: Recipes
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;
