package tasklist.backendspringboot.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tasklist.backendspringboot.entity.ErrorMsg;
import tasklist.backendspringboot.entity.Task;
import tasklist.backendspringboot.search.TaskSearchValues;
import tasklist.backendspringboot.service.TaskService;
import tasklist.backendspringboot.util.MyLogger;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> findAll() {

        MyLogger.showMethodName("task: findAll() ---------------------------------------------------------------- ");

        return ResponseEntity.ok(taskService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {

        MyLogger.showMethodName("task: add() ---------------------------------------------------------------- ");

        if (task.getId() != null && task.getId() != 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("You can't put id").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Title is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(taskService.add(task));

    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {

        MyLogger.showMethodName("task: update() ---------------------------------------------------------------- ");

        if (task.getId() == null || task.getId() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Id is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Title is required field").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        taskService.update(task);

        return new ResponseEntity(HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        MyLogger.showMethodName("task: delete() ---------------------------------------------------------------- ");

        try {
            taskService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Id not find").build(), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {

        MyLogger.showMethodName("task: findById() ---------------------------------------------------------------- ");

        Task task = null;

        try {
            task = taskService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity(ErrorMsg.builder().errorMessage("Id not find").build(), HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(task);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues) {

        MyLogger.showMethodName("task: search() ---------------------------------------------------------------- ");

        String text = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;

        Integer completed = taskSearchValues.getCompleted() != null ? taskSearchValues.getCompleted() : null;

        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

        Integer pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : null;
        Integer pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : null;


        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page result = taskService.findByParams(text, completed, priorityId, categoryId, pageRequest);


        return ResponseEntity.ok(result);

    }

}
