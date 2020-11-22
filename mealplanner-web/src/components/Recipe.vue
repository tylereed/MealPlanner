 @@ -1,58 +0,0 @@
<template>
  <section id="recipe">
    <p id="name">
      {{ item.name }}
    </p>
    <p id="price">
      <span class="emoji">{{ priceText }}</span>
    </p>
    <p id="speed">
      <span class="emoji">{{ speedText }}</span>
    </p>
    <p id="difficulty">
      <span class="emoji">{{ difficultText }}</span>
    </p>
  </section>
</template>

<script lang="ts">
import { defineComponent, PropType } from "vue";
import { Recipe } from "@/scripts/recipe-api.ts";

export default defineComponent({
  name: "Recipe",
  props: {
    item: {
      type: Object as PropType<Recipe>,
      required: true
    }
  },
  computed: {
    priceText(): string {
      switch (this.item.price) {
        case 1: return "👛";
        case 2: return "💵";
        case 3: return "💰";
        default: return "💸";
      }
    },
    speedText(): string {
      switch (this.item.speed) {
        case 1: return "🐇";
        case 2: return "🏃‍♀";
        case 3: return "🐢";
        default: return "🦄";
      }
    },
    difficultText(): string {
      switch (this.item.speed) {
        case 1: return "😀";
        case 2: return "😐";
        case 3: return "🙁";
        default: return "🤯";
      }
    }
  }
});
</script>

<style scoped>
#recipe {
  display: grid;
  grid-template-areas:
    "name  name   name"
    "price speed difficulty";
  padding: 1em;
  margin: 7px;
  outline: inset;
  box-shadow: 5px 10px 5px black;
  height: 100%;
  background-color: #D0FCB3;
}

#name {
  grid-area: name;
}

#price {
  grid-area: price;
}

#speed {
  grid-area: speed;
}

#difficulty {
  grid-area: difficulty;
}

.emoji {
  font-size: 1.5em;
}
</style>
