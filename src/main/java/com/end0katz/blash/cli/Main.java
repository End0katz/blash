package com.end0katz.blash.cli;

import java.io.IOException;

import com.end0katz.blash.*;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;

public class Main {

    public static ArgumentParser parser;

    public static BlashConfig getBlashConf() {
        BlashConfig.Builder bc = new BlashConfig.Builder();
        return bc.build();
    }

    public static BlashContext getBlashContext() throws IOException {
        BlashContext.Builder brc = new BlashContext.Builder();

        // Check if SUDO
        Process sudoP = Runtime.getRuntime().exec(new String[]{"id", "-u"});

        try {
            sudoP.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (new String(sudoP.getInputStream().readAllBytes()).equals("0\n")) {
            brc.sudo();
        }

        return brc.build();
    }

    public static void generateParserAndAddArguments() {
        parser = ArgumentParsers.newFor("blash").build();
    }

    public static void main(String[] args) throws IOException {
        generateParserAndAddArguments();

        Blash blash = new Blash(getBlashConf(), getBlashContext());
        System.out.println(blash.getCommand());
    }

}
