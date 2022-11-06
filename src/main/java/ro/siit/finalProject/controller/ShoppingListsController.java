package ro.siit.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("shoppingLists")
public class ShoppingListsController {
    @Autowired
    private ShoppingListsService shoppingListsService;

    @Autowired
    private IngredientsService ingredientsService;

    @GetMapping("/")
    public String getShoppingLists(Model model) {
        List<ShoppingList> shoppingLists = shoppingListsService.getShoppingListsForCurrentUser();
        model.addAttribute("shopping_lists", shoppingLists);
        return "shoppingLists/list";
    }

    @GetMapping("/add")
    public String getShoppingListAddForm(Model model) {
        return "shoppingLists/addShoppingListForm";
    }

    @PostMapping("/add")
    public RedirectView addShoppingList(Model model,
                                        @RequestParam("name") String name) {
        shoppingListsService.addShoppingList(name);
        return new RedirectView("/shoppingLists/");
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteShoppingList(Model model,
                                            @PathVariable("id") UUID shoppingListId) {
        shoppingListsService.deleteShoppingList(shoppingListId);
        return new RedirectView("/shoppingLists/");
    }
    

    @GetMapping("/edit/{id}")
    public String getShoppingListEditForm(Model model,
                                    @PathVariable("id") UUID shoppingListId) {
        Optional<ShoppingList> shoppingListToEdit = shoppingListsService.findById(shoppingListId);
        model.addAttribute("shopping_list", shoppingListToEdit.get());
        return "shoppingLists/editShoppingListForm";
    }

    @GetMapping("/edit/{id}/addShoppingListItem")
    public String getShoppingListItemAddForm(Model model, @PathVariable("id") UUID shoppingListId) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        Optional<ShoppingList> shoppingList = shoppingListsService.findById(shoppingListId);
        model.addAttribute("shoppingList", shoppingList.get());
        return "/shoppingLists/addShoppingListItemForm";
    }

    @PostMapping("/edit/addShoppingListItem")
    public RedirectView addShoppingListItem(Model model,
                                            @RequestParam("ingredientId") UUID ingredientId,
                                            @RequestParam("shoppingListItemQuantity") Integer shoppingListItemQuantity,
                                            @RequestParam("shoppingListId") UUID shoppingListId) {
        Ingredient ingredientById = ingredientsService.findIngredientById(ingredientId).get();
        ShoppingList shoppingListById = shoppingListsService.findById(shoppingListId).get();
        shoppingListsService.addItem(ingredientById, shoppingListById, shoppingListItemQuantity );
        return new RedirectView("/shoppingLists/edit/" + shoppingListId);
    }

    @GetMapping("items/delete/{shopping_list_Item_Id}")
    public RedirectView deleteShoppingListItem(Model model,
                                         @PathVariable("shopping_list_Item_Id") UUID shoppingListItemId) {
        Optional<ShoppingListItem> shoppingListItem = shoppingListsService.findShoppingListById(shoppingListItemId);
        shoppingListsService.deleteShoppingListItemById(shoppingListItemId);
        return new RedirectView("/shoppingLists/edit/" + shoppingListItem.get().getShoppingList().getId());
    }

    @PostMapping("/edit/{id}")
    public RedirectView editRecipeName(Model model,
                                       @RequestParam("shoppingListId") UUID shoppingListId,
                                       @RequestParam("shoppingListName") String name) {
        Optional<ShoppingList> shoppingList = shoppingListsService.findById(shoppingListId);
        shoppingList.get().setName(name);
        shoppingListsService.save(shoppingList.get());
        return new RedirectView("/shoppingLists/edit/" + shoppingListId);
    }

    @PostMapping("/")
    public RedirectView saveShoppingList(Model model,
                                         @RequestParam("shoppingListItemId") UUID shoppingListItemId,
                                         @RequestParam("shoppingListItemQuantity") Integer itemQuantity) {
        Optional<ShoppingListItem> shoppingListItem = shoppingListsService.findShoppingListItem(shoppingListItemId);
        shoppingListItem.get().setQuantity(itemQuantity);
        shoppingListsService.save(shoppingListItem.get().getShoppingList());
        return new RedirectView("/shoppingLists/");
    }

    @PostMapping("/{id}/addToShoppingList")
    public RedirectView addItemsToShoppingList(Model model,
                                               @RequestParam("shoppingListId") UUID shoppingListId,
                                               @RequestParam("recipeId") UUID recipeId) {

        shoppingListsService.addItemsToShoppingList(shoppingListId, recipeId);
        return new RedirectView("/shoppingLists/");
    }

}
