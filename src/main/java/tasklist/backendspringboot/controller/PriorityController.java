package tasklist.backendspringboot.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tasklist.backendspringboot.entity.ErrorMsg;
import tasklist.backendspringboot.entity.Priority;
import tasklist.backendspringboot.search.PrioritySearchValues;
import tasklist.backendspringboot.service.PriorityService;
import tasklist.backendspringboot.util.MyLogger;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping ("/priority")
@CrossOrigin(origins = "http://localhost:4200")
public class PriorityController {

    private PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }


    @GetMapping("/all")
    public List<Priority> findAll() {

        MyLogger.showMethodName("PriorityController: findAll() ---------------------------------------------------------- ");


        return priorityService.findAll();

    }



    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority){

        MyLogger.showMethodName("PriorityController: add() ---------------------------------------------------------- ");


        if (priority.getId() != null && priority.getId() != 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("You can't put id").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Title is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Color is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priorityService.add(priority));
    }


    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority){

        MyLogger.showMethodName("PriorityController: update() ---------------------------------------------------------- ");


        if (priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Id is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Title is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Color is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        priorityService.update(priority);

        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Priority> findById(@PathVariable Long id) {

        MyLogger.showMethodName("PriorityController: findById() ---------------------------------------------------------- ");


        Priority priority = null;

        try{
            priority = priorityService.findById(id);
        }catch (NoSuchElementException e){
            e.printStackTrace();
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Id not find").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        return  ResponseEntity.ok(priority);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        MyLogger.showMethodName("PriorityController: delete() ---------------------------------------------------------- ");

        try {
            priorityService.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Id not find").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues){

        MyLogger.showMethodName("PriorityController: search() ---------------------------------------------------------- ");

        return ResponseEntity.ok(priorityService.findByTitle(prioritySearchValues.getText()));
    }



}
