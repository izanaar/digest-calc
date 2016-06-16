package com.izanaar.digestCalc.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.RecursiveAction;
import java.util.function.Function;

public class DigestForkJoinTask extends RecursiveAction {

    private Function <byte[], String> performer;
    private URL source;

    public DigestForkJoinTask(Function<byte[], String> performer, URL source) {
        this.performer = performer;
        this.source = source;
    }

    @Override
    protected void compute() {
        try {
            InputStream stream = source.openStream();
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            String hex = performer.apply(bytes);
            System.out.println(hex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
