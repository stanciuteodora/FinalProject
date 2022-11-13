package ro.siit.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ro.siit.finalProject.model.*;
import ro.siit.finalProject.service.IngredientsService;
import ro.siit.finalProject.service.RecipesService;
import ro.siit.finalProject.service.ShoppingListsService;

import java.util.UUID;

@Controller
@RequestMapping("recipes")
public class RecipesController {
    @Autowired
    private RecipesService recipesService;
    @Autowired
    private IngredientsService ingredientsService;
    @Autowired
    private ShoppingListsService shoppingListsService;

    @GetMapping("/")
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipesService.getRecipes());
        model.addAttribute("user", getCurrentUser());
        return "recipes/list";
    }

    @GetMapping("/add")
    public String getRecipeAddForm(Model model) {
        return "recipes/addRecipeForm";
    }

    @PostMapping("/add")
    public RedirectView addRecipe(Model model, @RequestParam("recipeName") String name) {
        recipesService.saveRecipe(new Recipe(UUID.randomUUID(), name));
        return new RedirectView("/recipes/");
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteRecipe(Model model, @PathVariable("id") UUID recipeId) {
        recipesService.deleteRecipe(recipeId);
        return new RedirectView("/recipes/");
    }

    @GetMapping("/edit/{id}")
    public String getRecipeEditForm(Model model, @PathVariable("id") UUID recipeId) {
        model.addAttribute("recipe", recipesService.getRecipeById(recipeId));
        return "recipes/editRecipeForm";
    }

    @GetMapping("/edit/{id}/addRecipeItem")
    public String getRecipeItemAddForm(Model model, @PathVariable("id") UUID recipeId) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        model.addAttribute("recipe", recipesService.getRecipeById(recipeId));
        return "recipes/addRecipeItemForm";
    }

    @PostMapping("/edit/addRecipeItem")
    public RedirectView addRecipeItem(Model model,
                                      @RequestParam("recipeId") UUID recipeId,
                                      @RequestParam("ingredientId") UUID ingredientId,
                                      @RequestParam("recipeItemQuantity") Integer itemQuantity) {
        recipesService.saveRecipeItem(recipeId, ingredientId, itemQuantity);
        return new RedirectView("/recipes/edit/" + recipeId);
    }

    @GetMapping("items/delete/{recipe_Item_Id}")
    public RedirectView deleteRecipeItem(Model model, @PathVariable("recipe_Item_Id") UUID recipeItemId) {
        RecipeItem recipeItem = recipesService.getRecipeItem(recipeItemId);
        recipesService.deleteRecipeItem(recipeItemId);
        return new RedirectView("/recipes/edit/" + recipeItem.getRecipe().getId());
    }

    @PostMapping("/edit/{id}")
    public RedirectView editRecipeName(Model model,
                                       @RequestParam("recipeId") UUID recipeId,
                                       @RequestParam("recipeName") String name) {
        recipesService.updateRecipe(recipeId, new Recipe(recipeId, name));
        return new RedirectView("/recipes/edit/" + recipeId);
    }

    @PostMapping("/")
    // todo review
    public RedirectView saveRecipeItem(Model model,
                                       @RequestParam("recipeItemId") UUID recipeItemId,
                                       @RequestParam("recipeItemQuantity") Integer itemQuantity) {
        RecipeItem recipeItem = recipesService.getRecipeItem(recipeItemId);
        recipeItem.setQuantity(itemQuantity);
        recipesService.saveRecipe(recipeItem.getRecipe());
        return new RedirectView("/recipes/");
    }

    @GetMapping("/{id}/addToShoppingList")
    public String addItemsToShoppingListAddForm(Model model, @PathVariable("id") UUID recipeId) {
        model.addAttribute("shoppingLists", shoppingListsService.getShoppingListsForCurrentUser(null));
        model.addAttribute("recipe", recipesService.getRecipeById(recipeId));
        return "recipes/addRecipeItemsToShoppingListForm";
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        } else {
            return null;
        }
    }
}
