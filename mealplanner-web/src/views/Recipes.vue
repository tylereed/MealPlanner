 @@ -1,58 +0,0 @@
<template>
  <div>
    <h1>Recipes</h1>
    <div id="recipes">
      <div v-for="item of items" :key="item.id">
        <recipe-control :item="item" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import RecipeControl from "@/components/Recipe.vue";
import { defineComponent } from "vue";
import recipeService, { Recipe } from "@/scripts/recipe-api.ts";

export default defineComponent({
  name: "Recipes",
  components: {
    RecipeControl
  },
  data() {
    return {
      items: [] as Recipe[]
    };
  },
  async mounted() {
    const response = await recipeService.getRandomRecipes();
    const recipes = response.data;
    this.items = recipes;
  }
});
</script>

<style scoped>
#recipes {
  display: grid;
  grid-template-rows: 1fr;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr 1fr;
}
</style>
