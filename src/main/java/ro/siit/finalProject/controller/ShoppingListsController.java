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

@Controller
@RequestMapping("shoppingLists")
public class ShoppingListsController {
    @Autowired
    private ShoppingListsService shoppingListsService;

    @Autowired
    private IngredientsService ingredientsService;

    @GetMapping("/")
    public String getShoppingLists(Model model,
                                   @RequestParam(name = "sort", required = false) String sortMethod) {
        model.addAttribute("shopping_lists", shoppingListsService.getShoppingListsForCurrentUser(translateSortMethod(sortMethod)));
        return "shoppingLists/list";
    }

    @GetMapping("/add")
    public String getShoppingListAddForm(Model model) {
        return "shoppingLists/addShoppingListForm";
    }

    @PostMapping("/add")
    public RedirectView addShoppingList(Model model, @RequestParam("name") String name) {
        shoppingListsService.saveShoppingList(name);
        return new RedirectView("/shoppingLists/");
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteShoppingList(Model model, @PathVariable("id") UUID shoppingListId) {
        shoppingListsService.deleteShoppingList(shoppingListId);
        return new RedirectView("/shoppingLists/");
    }


    @GetMapping("/edit/{id}")
    public String getShoppingListEditForm(Model model, @PathVariable("id") UUID shoppingListId) {
        model.addAttribute("shopping_list", shoppingListsService.getShoppingListById(shoppingListId));
        return "shoppingLists/editShoppingListForm";
    }

    @GetMapping("/edit/{id}/addShoppingListItem")
    public String getShoppingListItemAddForm(Model model, @PathVariable("id") UUID shoppingListId) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        model.addAttribute("shoppingList", shoppingListsService.getShoppingListById(shoppingListId));
        return "/shoppingLists/addShoppingListItemForm";
    }

    @PostMapping("/edit/addShoppingListItem")
    public RedirectView addShoppingListItem(Model model,
                                            @RequestParam("shoppingListId") UUID shoppingListId,
                                            @RequestParam("ingredientId") UUID ingredientId,
                                            @RequestParam("shoppingListItemQuantity") String shoppingListItemQuantity) {
        shoppingListsService.addShoppingListItemsToShoppingList(shoppingListId, ingredientId, Integer.valueOf(shoppingListItemQuantity));
        return new RedirectView("/shoppingLists/edit/" + shoppingListId);
    }

    @GetMapping("items/delete/{shopping_list_Item_Id}")
    public RedirectView deleteShoppingListItem(Model model, @PathVariable("shopping_list_Item_Id") UUID shoppingListItemId) {
        ShoppingListItem shoppingListItem = shoppingListsService.getShoppingListItemById(shoppingListItemId);
        shoppingListsService.deleteShoppingListItemById(shoppingListItemId);
        return new RedirectView("/shoppingLists/edit/" + shoppingListItem.getShoppingList().getId());
    }

    @PostMapping("/edit/{id}")
    public RedirectView editRecipeName(Model model,
                                       @RequestParam("shoppingListId") UUID shoppingListId,
                                       @RequestParam("shoppingListName") String name,
                                       @RequestParam(name = "favorite", required = false) String favorite) {
        shoppingListsService.updateShoppingList(
                shoppingListId,
                new ShoppingList(shoppingListId, name, translateCheckboxValue(favorite))
        );
        return new RedirectView("/shoppingLists/edit/" + shoppingListId);
    }

    @PostMapping("/")
    // todo review
    public RedirectView saveShoppingList(Model model,
                                         @RequestParam("shoppingListItemId") UUID shoppingListItemId,
                                         @RequestParam("shoppingListItemQuantity") Integer itemQuantity) {
        ShoppingListItem shoppingListItem = shoppingListsService.getShoppingListItem(shoppingListItemId);
        shoppingListItem.setQuantity(itemQuantity);
        shoppingListsService.saveShoppingList(shoppingListItem.getShoppingList());
        return new RedirectView("/shoppingLists/");
    }

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
