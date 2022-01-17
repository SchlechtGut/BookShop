package com.example.MyBookShopApp.data;

import org.springframework.stereotype.Repository;

@Repository
public class TestEntityDAO extends AbstractHibernateDAO<TestEntity> {

    public TestEntityDAO() {
        super();
        setClazz(TestEntity.class);
    }
}
