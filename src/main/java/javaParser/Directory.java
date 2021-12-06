package javaParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
* @class
* */
public class Directory {

    private String root;
    private List<Path> files;

    public Directory(String path) {
        this.root = path;
        this.files = null;
    }

    public List<Path> getListDirectory() {

        try (Stream<Path> paths = Files.walk(Paths.get(this.root))) {
            this.files = paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.files;
    }

    public List<Path> getJavaFiles() {

        if (this.files == null)
            this.getListDirectory();

        List<Path> javaFiles = new Vector<Path>();
        for (Path path : this.files) {
            if (path.toString().endsWith(".java")) {
                javaFiles.add(path);
            }
        }

        return javaFiles;
    }

    public List<Path> getFiles() {
        return files;
    }

}