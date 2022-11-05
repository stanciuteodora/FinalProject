package ro.siit.finalProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ro.siit.finalProject.model.Ingredient;
import ro.siit.finalProject.service.IngredientsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("ingredients")
public class IngredientsListController {
    @Autowired
    private IngredientsService ingredientsService;

    @GetMapping("/")
    public String getIngredients(Model model) {
        List<Ingredient> ingredients = ingredientsService.getIngredients();
        model.addAttribute("ingredients", ingredients);
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
        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName(name);
        ingredient.setUnitOfMeasure(unitOfMeasure);
        ingredientsService.addIngredient(ingredient);
        return new RedirectView("/ingredients/");
    }

    @GetMapping("/delete/{id}")
    public RedirectView deleteIngredient(@PathVariable("id") UUID itemId) {
        ingredientsService.deleteIngredient(itemId);
        return new RedirectView("/ingredients/");
    }

    @GetMapping("/edit/{id}")
    public String getIngredientEditForm(Model model, @PathVariable("id") UUID id) {
        Optional<Ingredient> ingredientToEdit = ingredientsService.findIngredientById(id);
        model.addAttribute("ingredient", ingredientToEdit.get());
        return "ingredients/editForm";
    }

    @PostMapping("/edit")
    public RedirectView editIngredient(Model model,
                                       @RequestParam("ingredientId") UUID id,
                                       @RequestParam("ingredientName") String name,
                                       @RequestParam("ingredientUnitOfMeasure") String unitOfMeasure) {
        Optional<Ingredient> ingredient = ingredientsService.findIngredientById(id);
        ingredient.get().setName(name);
        ingredient.get().setUnitOfMeasure(unitOfMeasure);
        ingredientsService.save(ingredient.get());
        return new RedirectView("/ingredients/");
    }

}
