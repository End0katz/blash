package com.end0katz.blash;

import java.nio.file.*;

public record BlashContext(
        boolean root,
        String username,
        Path userHome) {

    public static class Builder {

        boolean root = false;
        String username = System.getProperty("user.name");
        Path userHome = Paths.get(System.getProperty("user.home"));

        public Builder sudo() {
            root = true;
            return this;
        }

        public Builder sudo(boolean value) {
            root = value;
            return this;
        }

        public Builder name(String s) {
            username = s;
            return this;
        }

        public Builder homeDir(Path p) {
            userHome = p;
            return this;
        }

        public BlashContext build() {
            return new BlashContext(
                    root,
                    username,
                    userHome
            );
        }
    }
}
