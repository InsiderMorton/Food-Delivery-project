package services;

import models.MenuItem;
import repositories.MenuRepository;

import java.util.List;

public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuItem> getAvailableItems() {
        return menuRepository.findAll()
                .stream()
                .filter(MenuItem::isAvailable)
                .toList();
    }

    public List<MenuItem> getItemsSortedByPrice() {
        return menuRepository.findAll()
                .stream()
                .sorted((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
                .toList();
    }
}

