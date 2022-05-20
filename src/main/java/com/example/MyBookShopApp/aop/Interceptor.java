package com.example.MyBookShopApp.aop;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.errs.EmptySearchException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Aspect
@Component
public class Interceptor {

    private final Logger logger = Logger.getLogger(this.getClass().getName());


    @Pointcut(value = "within(com.example.MyBookShopApp.controllers.ApiController)")
    public void apiMethods() {
    }

//    @Pointcut(value = "args(offset, limit)")
//    public void offsetLimitMethods(Integer offset, Integer limit) {
//    }

    @AfterThrowing(pointcut = "@annotation(com.example.MyBookShopApp.aop.SearchSection)", throwing = "ex")
    public void searchAction(EmptySearchException ex) {
        logger.info("Была попытка поиска по пустому вводу - " + ex.getLocalizedMessage());
    }

    @Around(value = "apiMethods() && args(offset, limit)")
    public Object logApiMethods(ProceedingJoinPoint proceedingJoinPoint, Integer offset, Integer limit) throws Throwable {

        Object retOb = proceedingJoinPoint.proceed();

        logger.info("was called api method with only limit and offset " + limit + " " + offset);

        return retOb;
    }

    @AfterReturning(value = "execution(* com.example.MyBookShopApp.security.UserRegister.registerNewUser(..))", returning = "user")
    public void logRegistration(User user) throws Throwable {
        logger.info("hooray new user! " + user);
    }


}
