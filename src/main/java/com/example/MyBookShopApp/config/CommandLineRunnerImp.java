package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.BookRepository;
import com.example.MyBookShopApp.data.TestEntity;
import com.example.MyBookShopApp.data.TestEntityCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.logging.Logger;

//@Configuration
public class CommandLineRunnerImp implements CommandLineRunner {

    TestEntityCrudRepository crudRepository;
    BookRepository bookRepository;

    @Autowired
    public CommandLineRunnerImp(TestEntityCrudRepository crudRepository, BookRepository bookRepository) {
        this.crudRepository = crudRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        for (int i = 0;i < 5;i++) {
//            createTestEntity(new TestEntity());
//        }
//
//        TestEntity readEntity = readTestEntityById(3L);
//
//        if (readEntity != null) {
//            Logger.getLogger(TestEntity.class.getSimpleName()).info("read " + readEntity.toString());
//        } else {
//            throw new NullPointerException();
//        }
//
//        TestEntity updatedTestEntity = updateTestEntityById(5L);
//
//        if (updatedTestEntity != null) {
//            Logger.getLogger(TestEntity.class.getSimpleName()).info("update " + updatedTestEntity.toString());
//        } else {
//            throw new NullPointerException();
//        }
//
//        deleteTestEntityById(4L);

//        Logger.getLogger(CommandLineRunnerImp.class.getSimpleName())
//                .info(bookRepository.findBooksByPubDateAfter(LocalDate.of(2015, 1, 1), PageRequest.of(0, 50)).getContent().toString());

//        Logger.getLogger(CommandLineRunnerImp.class.getSimpleName()).info(bookRepository.findBooksByAuthorFirstName("Burtie").toString());
//        Logger.getLogger(CommandLineRunnerImp.class.getSimpleName()).info(bookRepository.customFindAllBooks().toString());
//        Logger.getLogger(CommandLineRunnerImp.class.getSimpleName()).info(bookRepository.getPBooks().toString());
    }

    private void deleteTestEntityById(long id) {
        crudRepository.deleteById(id);
    }

    private TestEntity updateTestEntityById(long id) {
        TestEntity testEntity = crudRepository.findById(id).get();
        testEntity.setData("NEW DATA");
        return crudRepository.save(testEntity);
    }

    private TestEntity readTestEntityById(long id) {
        return crudRepository.findById(id).get();
    }

    private void createTestEntity(TestEntity entity) {
        entity.setData(String.valueOf(entity.hashCode()));
        crudRepository.save(entity);
    }
}
