 @@ -1,58 +0,0 @@
<template>
  <div>
      <h1>Recipes</h1>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Recipes",
  props: {
    msg: String
  },
  async mounted() {
      const recipes = await this.axios.get("http://localhost:8081/api/recipes/random/7");
      console.log(recipes);
  }
}
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
