package tasklist.backendspringboot.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import tasklist.backendspringboot.entity.Category;
import tasklist.backendspringboot.entity.ErrorMsg;
import tasklist.backendspringboot.search.CategorySearchValues;
import tasklist.backendspringboot.service.CategoryService;
import tasklist.backendspringboot.util.MyLogger;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping ("/category") // base url
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    // access to db
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/all")
    public List<Category> findAll() {

        //MyLogger.showMethodName("CategoryController: findAll() ---------------------------------------------------------- ");
        return categoryService.findAllByOrderByTitleAsc();
    }


    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category){
//MyLogger.showMethodName("CategoryController: add() ---------------------------------------------------------- ");
        if (category.getId() != null && category.getId() != 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("You can't put id").build(), HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Title is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(categoryService.add(category));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category){
        if (category.getId() == null || category.getId() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Id is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Title is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }
        categoryService.update(category);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {

        Category category = null;
        try{
            category = categoryService.findById(id);
        }catch (NoSuchElementException e){
            e.printStackTrace();
            return new ResponseEntity("id="+id+" not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return  ResponseEntity.ok(category);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        MyLogger.showMethodName("CategoryController: delete() ---------------------------------------------------------- ");
        try {
            categoryService.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return new ResponseEntity("id="+id+" not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues){
        return ResponseEntity.ok(categoryService.findByTitle(categorySearchValues.getText()));
    }




}
