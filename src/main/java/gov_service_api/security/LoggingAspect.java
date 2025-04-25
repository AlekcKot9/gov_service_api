package gov_service_api.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Логируем все методы в контроллерах
    @Pointcut("execution(* gov_service_api.controller..*(..))")
    public void controllerMethods() {}

    // До вызова метода
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Вызов метода: {} с аргументами: {}",
                joinPoint.getSignature(), joinPoint.getArgs());
    }

    // После успешного выполнения
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Метод {} завершён, результат: {}", joinPoint.getSignature(), result);
    }

    // При возникновении исключения
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Ошибка в методе {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
    }
}

