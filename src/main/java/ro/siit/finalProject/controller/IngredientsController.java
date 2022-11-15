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

/**
 * This is a controller for ingredients.
 */
@Controller
@RequestMapping("ingredients")
public class IngredientsController {
    @Autowired
    private IngredientsService ingredientsService;

    /**
     * Controller for viewing a list of ingredients.
     *
     * @param model- the mvc model
     * @return the path to the view
     */
    @GetMapping("/")
    public String getIngredients(Model model) {
        model.addAttribute("ingredients", ingredientsService.getIngredients());
        return "ingredients/list";
    }

    /**
     * Controller for viewing the add ingredient form.
     *
     * @param model- the mvc model
     * @return the path to the view
     */
    @GetMapping("/add")
    public String getIngredientAddForm(Model model) {
        return "ingredients/addForm";
    }

    /**
     * Adds an ingredient to the db.
     *
     * @param model-        the mvc model
     * @param name          - the name of the ingredient
     * @param unitOfMeasure - the unit of measure of the ingredient
     * @return a redirect view to ingredients view
     */
    @PostMapping("/add")
    public RedirectView addIngredient(Model model,
                                      @RequestParam("ingredientName") String name,
                                      @RequestParam("ingredientUnitOfMeasure") String unitOfMeasure) {
        ingredientsService.saveIngredient(new Ingredient(UUID.randomUUID(), name, unitOfMeasure));
        return new RedirectView("/ingredients/");
    }

    /**
     * Deletes an ingredient from db.
     *
     * @param itemId - the ingredient id
     * @return a redirect view to the list of ingredients
     */
    @GetMapping("/delete/{id}")
    public RedirectView deleteIngredient(@PathVariable("id") UUID itemId) {
        ingredientsService.deleteIngredient(itemId);
        return new RedirectView("/ingredients/");
    }

    /**
     * The controller for viewing the ingredient edit form.
     *
     * @param model - the mvc model
     * @param id    - the id of the ingredient
     * @return the path to the edit form
     */
    @GetMapping("/edit/{id}")
    public String getIngredientEditForm(Model model, @PathVariable("id") UUID id) {
        model.addAttribute("ingredient", ingredientsService.getIngredientById(id));
        return "ingredients/editForm";
    }

    /**
     * Updates the ingredient.
     *
     * @param model-        the mvc model
     * @param id            - the id of the ingredient
     * @param name          - the name of the ingredient
     * @param unitOfMeasure - the unit of measure of the ingredient
     * @return a redirect view to the ingredients list
     */
    @PostMapping("/edit")
    public RedirectView editIngredient(Model model,
                                       @RequestParam("ingredientId") UUID id,
                                       @RequestParam("ingredientName") String name,
                                       @RequestParam("ingredientUnitOfMeasure") String unitOfMeasure) {
        ingredientsService.updateIngredient(id, new Ingredient(id, name, unitOfMeasure));
        return new RedirectView("/ingredients/");
    }
}
