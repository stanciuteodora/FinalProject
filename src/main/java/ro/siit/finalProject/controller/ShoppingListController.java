package ro.siit.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.model.ShoppingListItem;
import ro.siit.finalProject.service.IngredientsService;
import ro.siit.finalProject.service.ShoppingListService;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("shoppingList")
public class ShoppingListController {
    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private IngredientsService ingredientsService;


    @GetMapping("/")
    public String listItems(Model model) {
        model.addAttribute("shoppingList", shoppingListService.getShoppingList());
        return "shoppingList/list";
    }

    @GetMapping("/add")
    public String addItemForm(Model model) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        return "shoppingList/addForm";
    }

    @PostMapping("/add")
    public RedirectView addItem(Model model,
                                @RequestParam("ingredient_id") UUID ingredientId,
                                @RequestParam("item_quantity") Integer itemQuantity) {
        Ingredient ingredientById = ingredientsService.findIngredientById(ingredientId).get();
        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setId(UUID.randomUUID());
        shoppingListItem.setIngredient(ingredientById);
        shoppingListItem.setQuantity(itemQuantity);
        shoppingListService.addItem(shoppingListItem);
        return new RedirectView("/shoppingList/");
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteItem(Model model, @PathVariable("id") UUID itemId) {
        shoppingListService.deleteItemById(itemId);
        return new RedirectView("/shoppingList/");
    }

    @GetMapping("/edit/{id}")
    public String editItemForm(Model model, @PathVariable("id") UUID itemId) {
        Optional<ShoppingListItem> item = shoppingListService.findById(itemId);
        model.addAttribute("shoppingListItem", item.get());
        return "shoppingList/editForm";
    }

    @PostMapping("/edit")
    public RedirectView editItem(Model model,
                                 @RequestParam("ingredient_id") UUID ingredientId,
                                 @RequestParam("item_quantity") Integer itemQuantity) {
        Optional<ShoppingListItem> item = shoppingListService.findById(ingredientId);
        item.get().setQuantity(itemQuantity);
        shoppingListService.save(item.get());
        return new RedirectView("/shoppingList/");
    }
}
