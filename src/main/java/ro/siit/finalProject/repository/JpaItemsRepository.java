package ro.siit.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.siit.finalProject.model.ShoppingListItem;

import java.util.UUID;

@Repository
public interface JpaItemsRepository extends JpaRepository<ShoppingListItem, UUID> {

}
