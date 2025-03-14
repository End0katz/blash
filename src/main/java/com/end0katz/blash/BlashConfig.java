package com.end0katz.blash;

import java.nio.file.*;

public record BlashConfig(
        Path initialCWD,
        int tabSize) {

    public static class Builder {

        Path cwd = Paths.get(System.getProperty("user.home"));
        int tabSize = 4;

        public Builder cwd(Path p) {
            cwd = p;
            return this;
        }

        public Builder tabSize(int i) {
            tabSize = i;
            return this;
        }

        public BlashConfig build() {
            return new BlashConfig(
                    cwd,
                    tabSize
            );
        }
    }
}
