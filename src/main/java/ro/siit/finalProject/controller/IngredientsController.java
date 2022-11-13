package ro.siit.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.service.IngredientsService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("ingredients")
public class IngredientsController {
    @Autowired
    private IngredientsService ingredientsService;

    @GetMapping("/")
    public String getIngredients(Model model) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        return "ingredients/list";
    }

    @GetMapping("/add")
    public String getIngredientAddForm(Model model) {
        return "ingredients/addForm";
    }

    @PostMapping("/add")
    public RedirectView addIngredient(Model model,
                                      @RequestParam("ingredientName") String name,
                                      @RequestParam("ingredientUnitOfMeasure") String unitOfMeasure) {
        ingredientsService.saveIngredient(new Ingredient(UUID.randomUUID(), name, unitOfMeasure));
        return new RedirectView("/ingredients/");
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteIngredient(@PathVariable("id") UUID itemId) {
        ingredientsService.deleteIngredient(itemId);
        return new RedirectView("/ingredients/");
    }

    @GetMapping("/edit/{id}")
    public String getIngredientEditForm(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("ingredient", ingredientsService.getIngredientById(id));
        return "ingredients/editForm";
    }

    @PostMapping("/edit")
    public RedirectView editIngredient(Model model,
                                       @RequestParam("ingredientId") UUID id,
                                       @RequestParam("ingredientName") String name,
                                       @RequestParam("ingredientUnitOfMeasure") String unitOfMeasure) {
        ingredientsService.updateIngredient(id, new Ingredient(id, name, unitOfMeasure));
        return new RedirectView("/ingredients/");
    }
}
