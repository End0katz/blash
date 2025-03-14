package com.end0katz.blash;

import java.io.IOException;
import java.nio.file.Path;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.terminal.*;

/**
 * Blash CLI
 */
public final class Blash {

    Terminal term;

    public BlashConfig bc;
    public BlashContext brc;

    public Path cwd;

    public void lanternaSetup() throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        term = factory.createTerminal();
    }

    public void kill(int exitCode) throws IOException {
        term.close();
        System.exit(exitCode);
    }

    public void println(String s) throws IOException {
        print(s);
        print("\n");
    }

    public void println() throws IOException {
        println("");
    }

    public void print(String s) throws IOException {
        for (char c : s.toCharArray()) {
            term.putCharacter(c);
        }
    }

    public void printCommandPrompt() throws IOException {
        term.resetColorAndSGR();
        term.enableSGR(SGR.BOLD);
        term.setForegroundColor(TextColor.ANSI.CYAN);
        print(brc.username());
        term.setForegroundColor(TextColor.ANSI.WHITE);
        print("/");
        term.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        print(
                cwd.startsWith(brc.userHome())
                ? "~" + cwd.toString()
                : cwd.toAbsolutePath().toString()
        );
        term.setForegroundColor(TextColor.ANSI.WHITE);
        print(" ");
        print(brc.root() ? "#" : "$");
        print(" ");
        term.resetColorAndSGR();

        term.flush();
    }

    public Blash(BlashConfig bc, BlashContext brc) {

        this.bc = bc;
        this.brc = brc;

        this.cwd = bc.initialCWD();

        try {
            lanternaSetup();
        } catch (IOException e) {
            try {
                kill(-1);
            } catch (IOException e2) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getCommand() throws IOException {
        printCommandPrompt();
        println();
        return "printf \"\\033[1;31mINTERNAL BLASH ERROR\\033[m\\n\"";
    }
}
