package com.example.jpa.controller;

import com.example.jpa.exception.ResourceNotFoundException;
import com.example.jpa.model.Item;
import com.example.jpa.repository.ItemRepository;
import com.example.jpa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories/{categoryId}/items")
    public Page<Item> getAllItemsByCategoryId(@PathVariable (value = "categoryId") Long categoryId,
                                                Pageable pageable) {
        return itemRepository.findByCategoryId(categoryId, pageable);
    }

    @PostMapping("/categories/{categoryId}/items")
    public Item createItem(@PathVariable (value = "categoryId") Long categoryId,
                                 @Valid @RequestBody Item item) {
        return categoryRepository.findById(categoryId).map(category -> {
            item.setCategory(category);
            return itemRepository.save(item);
        }).orElseThrow(() -> new ResourceNotFoundException("CategoryId " + categoryId + " not found"));
    }

    @PutMapping("/categories/{categoryId}/items/{itemId}")
    public Item updateItem(@PathVariable (value = "categoryId") Long categoryId,
                                 @PathVariable (value = "commentId") Long itemId,
                                 @Valid @RequestBody Item commentRequest) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("CategoryId " + categoryId + " not found");
        }

        return itemRepository.findById(itemId).map(item -> {
            item.setText(commentRequest.getText());
            return itemRepository.save(item);
        }).orElseThrow(() -> new ResourceNotFoundException("ItemId " + itemId + "not found"));
    }

    @DeleteMapping("/categories/{categoryId}/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable (value = "categoryId") Long categoryId,
                              @PathVariable (value = "itemId") Long itemId) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("CategoryId " + categoryId + " not found");
        }

        return itemRepository.findById(itemId).map(item -> {
             itemRepository.delete(item);
             return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("ItemId " + itemId + " not found"));
    }
}