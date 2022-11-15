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

/**
 * The controller for the recipes.
 */
@Controller
@RequestMapping("recipes")
public class RecipesController {
    @Autowired
    private RecipesService recipesService;
    @Autowired
    private IngredientsService ingredientsService;
    @Autowired
    private ShoppingListsService shoppingListsService;

    /**
     * Controller for viewing the recipes.
     *
     * @param model - the mvc model
     * @return the path to the view
     */
    @GetMapping("/")
    public String getRecipes(Model model) {
        model.addAttribute("recipes", recipesService.getRecipes());
        model.addAttribute("user", getCurrentUser());
        return "recipes/list";
    }

    /**
     * The controller for viewing the recipe add form.
     *
     * @param model- the mvc model
     * @return the recipe add form path
     */
    @GetMapping("/add")
    public String getRecipeAddForm(Model model) {
        return "recipes/addRecipeForm";
    }

    /**
     * Adds a recipe to the db.
     *
     * @param model- the mvc model
     * @param name   - the name of the recipe
     * @return the redirect view to the recipes view
     */
    @PostMapping("/add")
    public RedirectView addRecipe(Model model, @RequestParam("recipeName") String name) {
        recipesService.saveRecipe(new Recipe(UUID.randomUUID(), name));
        return new RedirectView("/recipes/");
    }

    /**
     * Deletes a recipe from db.
     *
     * @param model-   the mvc model
     * @param recipeId - the recipe id
     * @return a redirect view to the recipes view
     */
    @GetMapping("/delete/{id}")
    public RedirectView deleteRecipe(Model model, @PathVariable("id") UUID recipeId) {
        recipesService.deleteRecipe(recipeId);
        return new RedirectView("/recipes/");
    }

    /**
     * The controller for viewing the recipe edit form.
     *
     * @param model-   the mvc model
     * @param recipeId - the recipe id
     * @return the recipe edit form path
     */
    @GetMapping("/edit/{id}")
    public String getRecipeEditForm(Model model, @PathVariable("id") UUID recipeId) {
        model.addAttribute("recipe", recipesService.getRecipeById(recipeId));
        return "recipes/editRecipeForm";
    }

    /**
     * The controller for viewing the recipe item add form.
     *
     * @param model-   the mvc model
     * @param recipeId - the recipe id
     * @return the recipe item add form path
     */
    @GetMapping("/edit/{id}/addRecipeItem")
    public String getRecipeItemAddForm(Model model, @PathVariable("id") UUID recipeId) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        model.addAttribute("recipe", recipesService.getRecipeById(recipeId));
        return "recipes/addRecipeItemForm";
    }

    /**
     * Adds an item to the recipe.
     *
     * @param model-       the mvc model
     * @param recipeId     - the recipe id
     * @param ingredientId - the item's ingredient id
     * @param itemQuantity - the items quantity
     * @return a redirect view to the recipe
     */
    @PostMapping("/edit/addRecipeItem")
    public RedirectView addRecipeItem(Model model,
                                      @RequestParam("recipeId") UUID recipeId,
                                      @RequestParam("ingredientId") UUID ingredientId,
                                      @RequestParam("recipeItemQuantity") Integer itemQuantity) {
        recipesService.addRecipeItemsToRecipe(recipeId, ingredientId, itemQuantity);
        return new RedirectView("/recipes/edit/" + recipeId);
    }

    /**
     * Deletes a recipe item from a recipe.
     *
     * @param model-       the mvc model
     * @param recipeItemId - the recipe item id
     * @return a redirect view to the recipe
     */
    @GetMapping("items/delete/{recipe_Item_Id}")
    public RedirectView deleteRecipeItem(Model model, @PathVariable("recipe_Item_Id") UUID recipeItemId) {
        RecipeItem recipeItem = recipesService.getRecipeItem(recipeItemId);
        recipesService.deleteRecipeItem(recipeItemId);
        return new RedirectView("/recipes/edit/" + recipeItem.getRecipe().getId());
    }

    /**
     * Updates the recipe name.
     *
     * @param model-   the mvc model
     * @param recipeId - the recipe id
     * @param name     - the recipe name
     * @return a redirect view to the edit recipe
     */
    @PostMapping("/edit/{id}")
    public RedirectView editRecipeName(Model model,
                                       @RequestParam("recipeId") UUID recipeId,
                                       @RequestParam("recipeName") String name) {
        recipesService.updateRecipe(recipeId, new Recipe(recipeId, name));
        return new RedirectView("/recipes/edit/" + recipeId);
    }

    /**
     * Updates the recipe items.
     *
     * @param model-         the mvc model
     * @param recipeId       - the recipe id
     * @param recipeItemIds  - the recipe items ids
     * @param itemQuantities - the items quantities
     * @return a redirect view to the edit recipe
     */
    @PostMapping("/edit/{id}/updateItems")
    public RedirectView saveRecipeItem(Model model,
                                       @PathVariable("id") UUID recipeId,
                                       @RequestParam(name = "recipeItemId", required = false) UUID[] recipeItemIds,
                                       @RequestParam(name = "recipeItemQuantity", required = false) Integer[] itemQuantities) {
        if (recipeItemIds == null || itemQuantities == null) {
            return new RedirectView("/recipes/edit/" + recipeId);
        }
        recipesService.updateItemQuantities(recipeItemIds, itemQuantities);
        return new RedirectView("/recipes/edit/" + recipeId);
    }


    /**
     * The controller for viewing  the add recipes items to a shopping list form.
     *
     * @param model-   the mvc model
     * @param recipeId - the recipe id
     * @return the path to the view
     */
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
