package com.end0katz.blash;

import java.io.IOException;
import java.nio.file.Path;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
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
                ? "~" + cwd.toString().substring(brc.userHome().toString().length())
                : cwd.toAbsolutePath().toString()
        );
        term.setForegroundColor(TextColor.ANSI.WHITE);
        print(" ");
        print(brc.root() ? "#" : "$");
        print(" ");
        term.resetColorAndSGR();

        term.flush();
    }

    public String input() throws IOException {
        String result = "";
        TerminalPosition start = term.getCursorPosition();
        int index = 0;

        while (!result.endsWith("\n")) {
            term.setCursorPosition(start);
            print(result);
            term.setCursorPosition(start.withRelativeColumn(index));

            KeyStroke ks = term.readInput();

            switch (ks.getKeyType()) {
                case KeyType.Character -> {
                    result = result.substring(0, index)
                            + ks.getCharacter()
                            + result.substring(index);
                    index++;
                }

                case KeyType.ArrowLeft -> {
                    if (index > 0) {
                        index--;
                    }
                }
                case KeyType.ArrowRight -> {
                    if (index < result.length()) {
                        index++;
                    }
                }
                // TODO implement history

                case KeyType.Backspace -> {
                    if (index > 0) {
                        term.setCursorPosition(start);
                        print(" ".repeat(result.length()));

                        result = result.substring(0, index - 1)
                                + result.substring(index);
                        index--;
                    }
                }

                case KeyType.Delete -> {
                    if (index < result.length()) {
                        term.setCursorPosition(start);
                        print(" ".repeat(result.length()));

                        result = result.substring(0, index)
                                + result.substring(index + 1);
                    }
                }

                case KeyType.Home -> {
                    index = 0;
                }
                case KeyType.End -> {
                    index = result.length();
                }

                case KeyType.Tab -> {
                    do {
                        result = result.substring(0, index)
                                + " "
                                + result.substring(index);
                        index++;
                    } while (index % bc.tabSize() > 0);
                }
                case KeyType.ReverseTab -> {
                    int initialParity = result.length() % bc.tabSize();

                    do {
                        result = result.substring(0, index)
                                + " "
                                + result.substring(index);
                    } while (result.length() % bc.tabSize() != initialParity);
                }

                case KeyType.Enter -> {
                    return result;
                }

                case KeyType.Escape -> {

                }

                case KeyType.Insert -> {

                }

                case KeyType.PageDown -> {
                    index = Math.min(
                            index + term.getTerminalSize().getRows(),
                            result.length());
                }
                case KeyType.PageUp -> {
                    index = Math.max(
                            index - term.getTerminalSize().getRows(),
                            0
                    );
                }

                default -> {
                }
            }
        }
        return result;
    }

    public String getCommand() throws IOException {
        String cmd; // = "printf \"\\033[1;31mINTERNAL BLASH ERROR\\033[m\\n\"";
        printCommandPrompt();
        cmd = input();
        println();

        return cmd;
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

}
