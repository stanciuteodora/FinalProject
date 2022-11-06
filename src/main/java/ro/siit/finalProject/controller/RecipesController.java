package ro.siit.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ro.siit.finalProject.model.*;
import ro.siit.finalProject.service.IngredientsService;
import ro.siit.finalProject.service.RecipeService;
import ro.siit.finalProject.service.ShoppingListsService;

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
    @Autowired
    private ShoppingListsService shoppingListsService;

    @GetMapping("/")
    public String getRecipes(Model model) {
        List<Recipe> recipes = recipeService.getRecipesList();
        model.addAttribute("recipes", recipes);
        model.addAttribute("user", getCurrentUser());
        return "recipes/list";
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        } else {
            return null;
        }
    }

    @GetMapping("/add")
    public String getRecipeAddForm(Model model) {
        return "recipes/addRecipeForm";
    }

    @PostMapping("/add")
    public RedirectView addRecipe(Model model,
                                  @RequestParam("recipeName") String name) {
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
    public String getRecipeEditForm(Model model, @PathVariable("id") UUID recipeId) {
        Recipe recipeToEdit = recipeService.findById(recipeId);
        model.addAttribute("recipe", recipeToEdit);
        return "recipes/editRecipeForm";
    }

    @GetMapping("/edit/{id}/addRecipeItem")
    public String getRecipeItemAddForm(Model model, @PathVariable("id") UUID recipeId) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        Recipe recipeToEdit = recipeService.findById(recipeId);
        model.addAttribute("recipe", recipeToEdit);
        return "recipes/addRecipeItemForm";
    }

    @PostMapping("/edit/addRecipeItem")
    public RedirectView addRecipeItem(Model model,
                                      @RequestParam("ingredientId") UUID ingredientId,
                                      @RequestParam("recipeItemQuantity") Integer itemQuantity,
                                      @RequestParam("recipeId") UUID recipeId) {
        Ingredient ingredientById = ingredientsService.findIngredientById(ingredientId).get();
        Recipe recipeById = recipeService.findById(recipeId);
        recipeService.addRecipeItem(ingredientById, itemQuantity, recipeById);
        return new RedirectView("/recipes/edit/" + recipeId);
    }

    @GetMapping("items/delete/{recipe_Item_Id}")
    public RedirectView deleteRecipeItem(Model model,
                                         @PathVariable("recipe_Item_Id") UUID recipeItemId) {
        Optional<RecipeItem> recipeItem = recipeService.findItem(recipeItemId);
        recipeService.deleteRecipeItem(recipeItemId);
        return new RedirectView("/recipes/edit/" + recipeItem.get().getRecipe().getId());
    }

    @PostMapping("/edit/{id}")
    public RedirectView editRecipeName(Model model,
                                       @RequestParam("recipeId") UUID recipeId,
                                       @RequestParam("recipeName") String name) {
        Recipe recipe = recipeService.findById(recipeId);
        recipe.setName(name);
        recipeService.save(recipe);
        return new RedirectView("/recipes/edit/" + recipeId);
    }

    @PostMapping("/")
    public RedirectView saveRecipe(Model model,
                                   @RequestParam("recipeItemId") UUID recipeItemId,
                                   @RequestParam("recipeItemQuantity") Integer itemQuantity) {
        Optional<RecipeItem> recipeItem = recipeService.findItem(recipeItemId);
        recipeItem.get().setQuantity(itemQuantity);
        recipeService.save(recipeItem.get().getRecipe());
        return new RedirectView("/recipes/");
    }

    @GetMapping("/{id}/addToShoppingList")
    public String addItemsToShoppingListAddForm(Model model, @PathVariable("id") UUID recipeId) {
        model.addAttribute("shoppingLists", shoppingListsService.getShoppingListsForCurrentUser());
        Recipe recipeToEdit = recipeService.findById(recipeId);
        model.addAttribute("recipe", recipeToEdit);
        return "recipes/addRecipeItemsToShoppingListForm";
    }


}
