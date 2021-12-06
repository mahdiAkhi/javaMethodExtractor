package javaParser;


import com.github.javaparser.ast.CompilationUnit;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class App {


    public static void main(String[] args) throws IOException {
        /**
         * Create a Directory obj for OS walk
         * @arg path: path to project root
         */
        Directory dir = new Directory("E:\\Papers\\Thesis\\code\\repo scrapper\\repo\\gson-master");
//        a list of java files in project
        List<Path> javaFiles = dir.getJavaFiles();


        /**
         * each project saves in a json file as an array of json object like below
         * [
         *      {
         *      longName: packageName,
         *      methods:[
         *          {
         *              signature: signature,
         *              comments: comments,
         *              body: body
         *              }
         *      ]
         *
         *      }
         * ]
         */

//        model project object(json file)
        String project = "[";

//        model each file class
        String fileClass = "";

//        retrieving all file in project
        for (Path javaFile : javaFiles) {
            System.out.println(javaFile);
//          get each java file to extractor for extracting methods
            Extractor extractor = new Extractor(javaFile);
//          compilation unit for soft working with extractor output
            CompilationUnit cu = extractor.getCompilationUnit();

            if(cu.getTypes().size()<1){
                continue;
            }
            if( !cu.getType(0).isClassOrInterfaceDeclaration()) {
                continue;
            }

            if(cu.getType(0).asClassOrInterfaceDeclaration().isInterface())
                continue;

//          create longName
            String packageName = getPackageName(cu);
            String className = getClassName(cu);
            String longName = "{\"longName\":" + "\"" + packageName + "." + className + "\",";
            fileClass += longName + '\n';

            fileClass += "\"methods\":[";

//          an hashtable to store method objects
            List<Hashtable> methods = new ArrayList<Hashtable>();
            methods = extractor.extractMethods();

            if(methods.size()<1){

            }
            for (Hashtable method : methods) {
                //     convert hashtable to json object
                JSONObject converter = new JSONObject(method);
                fileClass += converter + ",";
            }
            fileClass = fileClass.substring(0, fileClass.length() - 1);
            fileClass += "]},";

        }
        fileClass = fileClass.substring(0, fileClass.length() - 1);

        project +=fileClass+"\n]";
        File jsonoFile = new File("E:\\Papers\\Thesis\\code\\repo scrapper\\repo-scrapper\\src\\main\\java\\javaParser\\jsons\\gson.json");
        jsonoFile.createNewFile();

        FileWriter writer = new FileWriter(jsonoFile);
        writer.write(project);
        writer.close();
    }

    private static String getPackageName(CompilationUnit cu) {
        String packageName = cu.getPackageDeclaration().get().toString();
        return packageName.split(" ")[1].replace(";", "").trim();
    }

    private static String getClassName(CompilationUnit cu) {
        if(cu.getType(0).isClassOrInterfaceDeclaration()){
            return cu.getType(0).asClassOrInterfaceDeclaration().getName().toString().trim();
        }
        return "interface";

    }
}