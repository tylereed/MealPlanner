import axios, { AxiosResponse } from "axios";

export interface Recipe {
  id: number;
  name: string;
  directions: string;
  location: string;
  price: number;
  speed: number;
  difficulty: number;
};

export default {
  getRandomRecipes(): Promise<AxiosResponse<Recipe[]>> {
    return axios.get<Recipe[]>("http://localhost:8081/api/recipes/random/7");
  }
};
