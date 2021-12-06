package javaParser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class Extractor {

    private final Path path;
    public CompilationUnit cu;
    private String code = null;

    public Extractor(Path path) {
        this.path = path;
        this.readFile();
        this.cu = StaticJavaParser.parse(this.code);
    }


    public CompilationUnit getCompilationUnit() {

        return this.cu;
    }

    private void readFile() {

        File file = new File(this.path.toString());
        String text = "";

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                text += scanner.nextLine() + "\n";
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        this.code = text;

    }

    public String getCode() {
        if (code == null) {
            this.readFile();
        }
        return this.code;
    }

    public List<Hashtable> extractMethods() {

        if (this.code == null)
            this.readFile();

        Hashtable<String, String> method = new Hashtable<>();
        List<Hashtable> methods = new ArrayList<Hashtable>();


        cu.findAll(MethodDeclaration.class).forEach(n -> {

            method.put("Signature", n.getDeclarationAsString());
            String comment = (n.hasJavaDocComment()) ? n.getJavadoc().get().toString() : "";
            method.put("Comment", comment);
            String body="";
            if(!n.getBody().equals(Optional.empty())){
                body = n.getBody().get().toString();
            }
            method.put("Body : ", body);
            method.put("Method:", n.toString());
            methods.add(method);
        });
        return methods;
    }
}


