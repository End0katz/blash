package com.end0katz.blash;

import java.nio.file.*;

public record BlashConfig(
        Path initialCWD) {

    public static class Builder {

        Path cwd = Paths.get(System.getProperty("user.home"));

        public BlashConfig build() {
            return new BlashConfig(cwd);
        }
    }
}
