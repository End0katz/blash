package com.end0katz.blash;

public record BlashContext(
        boolean root) {

    public static class Builder {

        boolean root = false;

        public Builder sudo() {
            root = true;
            return this;
        }

        public BlashContext build() {
            return new BlashContext(
                    root
            );
        }
    }
}
