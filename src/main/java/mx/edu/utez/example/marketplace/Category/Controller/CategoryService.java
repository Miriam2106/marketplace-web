package mx.edu.utez.example.marketplace.Category.Controller;

import mx.edu.utez.example.marketplace.Category.Model.Category;
import mx.edu.utez.example.marketplace.Category.Model.CategoryRepository;
import mx.edu.utez.example.marketplace.Utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    //inicia la transacción, tiene un bloque de código y si al final algo tiene un error las instrucciones anteriores se anulan
    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    public ResponseEntity<Message> findAll(){
        return new ResponseEntity<>(new Message("Ok", false, categoryRepository.findAll()), HttpStatus.OK);
    }

    @Transactional(rollbackOn = {SQLException.class})
    public ResponseEntity<Message> save(Category category){
        Optional<Category> existCategory = categoryRepository.findByDescription(category.getDescription());
        if(existCategory.isPresent()){
            return new ResponseEntity<>(new Message("La categoría ya existe", true, null), HttpStatus.BAD_REQUEST);
        }
        Category savedCategory = categoryRepository.saveAndFlush(category);
        return new ResponseEntity<>(new Message("Categoría registrada correctamente", false, savedCategory), HttpStatus.OK);
    }

    @Transactional(rollbackOn = {SQLException.class})
    public ResponseEntity<Message> update(Category category){
        if(categoryRepository.existById((category.getId()))){
            return new ResponseEntity<>(new Message("Ok", false, categoryRepository.saveAndFlush(category)), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message("La categoría no existe", true, null), HttpStatus.BAD_REQUEST);
    }
}
