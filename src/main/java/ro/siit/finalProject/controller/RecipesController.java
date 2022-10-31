package ro.siit.finalProject.controller;

import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.model.Recipe;
import ro.siit.finalProject.model.RecipeItem;
import ro.siit.finalProject.model.ShoppingListItem;
import ro.siit.finalProject.service.IngredientsService;
import ro.siit.finalProject.service.RecipeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("recipes")
public class RecipesController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private IngredientsService ingredientsService;


    @GetMapping("/")
    public String getRecipes(Model model) {
        List<Recipe> recipes = recipeService.getRecipesList();
        model.addAttribute("recipes", recipes);
        return "recipes/list";
    }

    @GetMapping("/add")
    public String addItemForm(Model model) {
        return "recipes/addRecipeForm";
    }

    @PostMapping("/add")
    public RedirectView addRecipeName(Model model,
                                      @RequestParam("recipe_name") String name) {
        Recipe recipe = new Recipe();
        recipe.setId(UUID.randomUUID());
        recipe.setName(name);
        recipeService.addRecipe(recipe);
        return new RedirectView("/recipes/");
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteRecipe(Model model, @PathVariable("id") UUID recipeId) {
        recipeService.deleteRecipe(recipeId);
        return new RedirectView("/recipes/");
    }

    @GetMapping("/edit/{id}")
    public String editRecipe(Model model, @PathVariable("id") UUID recipeId) {
        Optional<Recipe> recipeToEdit = recipeService.findById(recipeId);
        model.addAttribute("recipe", recipeToEdit.get());
        return "recipes/editRecipeForm";
    }


    @GetMapping("/edit/{id}/addRecipeItem")
    public String addIngredient(Model model, @PathVariable("id") UUID recipeId) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        Optional<Recipe> recipeToEdit = recipeService.findById(recipeId);
        model.addAttribute("recipe", recipeToEdit.get());
        return "recipes/addRecipeItemForm";
    }

    @PostMapping("/edit/addRecipeItem")
    public RedirectView addItem(Model model,
                                @RequestParam("ingredient_id") UUID ingredientId,
                                @RequestParam("recipeItem_quantity") Integer itemQuantity,
                                @RequestParam("recipe_id") UUID recipeId) {
        Ingredient ingredientById = ingredientsService.findIngredientById(ingredientId).get();
        Recipe recipeById = recipeService.findById(recipeId).get();
        RecipeItem recipeItem = new RecipeItem();
        recipeItem.setId(UUID.randomUUID());
        recipeItem.setIngredient(ingredientById);
        recipeItem.setQuantity(itemQuantity);
        recipeItem.setRecipe(recipeById);
        recipeService.addRecipeItem(recipeItem);
        return new RedirectView("/recipes/edit/" + recipeId);
    }

    @GetMapping("items/delete/{recipe_item_id}")
    public RedirectView deleteIngredient(Model model, @PathVariable("recipe_item_id") UUID recipeItemId) {
        Optional<RecipeItem> recipeItem = recipeService.findItem(recipeItemId);
        recipeService.deleteRecipeItem(recipeItemId);
        return new RedirectView("/recipes/edit/" + recipeItem.get().getRecipe().getId());
    }
}
