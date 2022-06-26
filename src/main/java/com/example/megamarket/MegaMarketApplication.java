package com.example.megamarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * https://github.com/docker/for-win/issues/12576 молимся
 * https://docs.docker.com/language/java/build-images/
 * docker run --name pg-14.4 -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=mega_market -d postgres:14.4
 *
 * https://www.toptal.com/java/spring-boot-rest-api-error-handling
 */
@SpringBootApplication
public class MegaMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MegaMarketApplication.class, args);
    }

}
