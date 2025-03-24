package com.soundify.server;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class SoundifyModulesTests {
    ApplicationModules modules = ApplicationModules.of(SoundifyServerApplication.class);

    @Test
    void verify() {
        System.out.println(modules.toString());
        System.out.println("test");
        modules.verify();

    }
}
