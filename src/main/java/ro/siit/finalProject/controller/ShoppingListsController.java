package ro.siit.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ro.siit.finalProject.model.*;
import ro.siit.finalProject.service.IngredientsService;
import ro.siit.finalProject.service.ShoppingListsService;
import ro.siit.finalProject.service.SortMethod;

import java.util.UUID;

/**
 * This is a controller for shopping lists.
 */
@Controller
@RequestMapping("shoppingLists")
public class ShoppingListsController {
    @Autowired
    private ShoppingListsService shoppingListsService;

    @Autowired
    private IngredientsService ingredientsService;

    /**
     * Controller for viewing the shopping lists.
     *
     * @param model      - the mvc model
     * @param sortMethod - the sorting method for the list
     * @return the path to the view
     */
    @GetMapping("/")
    public String getShoppingLists(Model model,
                                   @RequestParam(name = "sort", required = false) String sortMethod) {
        model.addAttribute("shopping_lists", shoppingListsService.getShoppingListsForCurrentUser(translateSortMethod(sortMethod)));
        return "shoppingLists/list";
    }

    /**
     * The controller for viewing the shopping list add form.
     *
     * @param model- the mvc model
     * @return the shopping list add form path
     */
    @GetMapping("/add")
    public String getShoppingListAddForm(Model model) {
        return "shoppingLists/addShoppingListForm";
    }

    /**
     * Adds a shopping list to the db.
     *
     * @param model- the mvc model
     * @param name   - the name of the shopping list
     * @return the redirect view to the shopping lists view
     */
    @PostMapping("/add")
    public RedirectView addShoppingList(Model model, @RequestParam("name") String name) {
        shoppingListsService.saveShoppingList(name);
        return new RedirectView("/shoppingLists/");
    }

    /**
     * Deletes a shopping list from db.
     *
     * @param model-         the mvc model
     * @param shoppingListId - the shopping list id
     * @return the redirect view to the shopping lists view
     */
    @GetMapping("/delete/{id}")
    public RedirectView deleteShoppingList(Model model, @PathVariable("id") UUID shoppingListId) {
        shoppingListsService.deleteShoppingList(shoppingListId);
        return new RedirectView("/shoppingLists/");
    }

    /**
     * The controller for viewing the shopping list edit form.
     *
     * @param model          - the mvc model
     * @param shoppingListId - the shopping list id
     * @return the shopping list edit form path
     */
    @GetMapping("/edit/{id}")
    public String getShoppingListEditForm(Model model, @PathVariable("id") UUID shoppingListId) {
        model.addAttribute("shopping_list", shoppingListsService.getShoppingListById(shoppingListId));
        return "shoppingLists/editShoppingListForm";
    }

    /**
     * The controller for viewing the shopping list item add form.
     *
     * @param model          - the mvc model
     * @param shoppingListId - the shopping list id
     * @return the shopping list item add form path
     */
    @GetMapping("/edit/{id}/addShoppingListItem")
    public String getShoppingListItemAddForm(Model model, @PathVariable("id") UUID shoppingListId) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        model.addAttribute("shoppingList", shoppingListsService.getShoppingListById(shoppingListId));
        return "/shoppingLists/addShoppingListItemForm";
    }

    /**
     * Adds an item to the shopping list.
     *
     * @param model                    - the mvc model
     * @param shoppingListId           - the shopping list id
     * @param ingredientId             - the item's ingredient id
     * @param shoppingListItemQuantity - the item's quantity
     * @return a redirect view to the shopping list
     */
    @PostMapping("/edit/addShoppingListItem")
    public RedirectView addShoppingListItem(Model model,
                                            @RequestParam("shoppingListId") UUID shoppingListId,
                                            @RequestParam("ingredientId") UUID ingredientId,
                                            @RequestParam("shoppingListItemQuantity") String shoppingListItemQuantity) {
        shoppingListsService.addShoppingListItemsToShoppingList(shoppingListId, ingredientId, Integer.valueOf(shoppingListItemQuantity));
        return new RedirectView("/shoppingLists/edit/" + shoppingListId);
    }

    /**
     * Deletes a shopping list item from a shopping list.
     *
     * @param model-             the mvc model
     * @param shoppingListItemId - the shopping list item id
     * @return a redirect view to the shopping list
     */
    @GetMapping("items/delete/{shopping_list_Item_Id}")
    public RedirectView deleteShoppingListItem(Model model, @PathVariable("shopping_list_Item_Id") UUID shoppingListItemId) {
        ShoppingListItem shoppingListItem = shoppingListsService.getShoppingListItemById(shoppingListItemId);
        shoppingListsService.deleteShoppingListItemById(shoppingListItemId);
        return new RedirectView("/shoppingLists/edit/" + shoppingListItem.getShoppingList().getId());
    }

    /**
     * Updates the shopping list name and changes the favorite status.
     *
     * @param model-         the mvc model
     * @param shoppingListId - the shopping list id
     * @param name           - the name of the shopping list
     * @param favorite       - the status (true/false) of the shopping list favorite option
     * @return redirect view to the edit shopping list
     */
    @PostMapping("/edit/{id}")
    public RedirectView editShoppingListName(Model model,
                                             @RequestParam("shoppingListId") UUID shoppingListId,
                                             @RequestParam("shoppingListName") String name,
                                             @RequestParam(name = "favorite", required = false) String favorite) {
        shoppingListsService.updateShoppingList(
                shoppingListId,
                new ShoppingList(shoppingListId, name, translateCheckboxValue(favorite))
        );
        return new RedirectView("/shoppingLists/edit/" + shoppingListId);
    }

    /**
     * Updates the shopping list items.
     *
     * @param model-               the mvc model
     * @param shoppingListId       - the shopping list id
     * @param shoppingListItemIds- the shopping list items ids
     * @param itemQuantities       - the items quantities
     * @return redirect view to the edit shopping list
     */
    @PostMapping("/edit/{id}/updateItems")
    public RedirectView saveShoppingList(Model model,
                                         @PathVariable("id") UUID shoppingListId,
                                         @RequestParam(name = "shoppingListItemId", required = false) UUID[] shoppingListItemIds,
                                         @RequestParam(name = "shoppingListItemQuantity", required = false) Integer[] itemQuantities) {
        if (shoppingListItemIds == null || itemQuantities == null) {
            return new RedirectView("/shoppingLists/edit/" + shoppingListId);
        }

        shoppingListsService.updateShoppingListItemQuantities(shoppingListItemIds, itemQuantities);
        return new RedirectView("/shoppingLists/edit/" + shoppingListId);
    }

    /**
     * Adds recipe items to the shopping list.
     *
     * @param model-         the mvc model
     * @param shoppingListId - - the shopping list id
     * @param recipeId       - the recipe id
     * @return a redirect view to the shopping lists
     */
    @PostMapping("/{id}/addToShoppingList")
    public RedirectView addItemsToShoppingList(Model model,
                                               @RequestParam("shoppingListId") UUID shoppingListId,
                                               @RequestParam("recipeId") UUID recipeId) {
        shoppingListsService.addRecipeItemsToShoppingList(shoppingListId, recipeId);
        return new RedirectView("/shoppingLists/");
    }

    private Boolean translateCheckboxValue(String string) {
        return "on".equals(string);
    }

    private SortMethod translateSortMethod(String sortMethod) {
        return SortMethod.getValue(sortMethod);
    }

}
