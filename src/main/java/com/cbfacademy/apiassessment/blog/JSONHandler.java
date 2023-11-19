package com.cbfacademy.apiassessment.blog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class JSONHandler {
    
    // read json as java object (list of blog) - deserialization (json string to java object)
     public static List<Blog> read (String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        List<Blog> blogs = new ArrayList<>();
        
        try {
            File sourceFile = Paths.get(fileName).toFile();
            blogs = new ArrayList<>(Arrays.asList(objectMapper.readValue(sourceFile, Blog[].class)));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return blogs;
    }

    // read java object (list of blog) as json - serialization (java object to json string)
    public static void saveList(List<Blog> blogs, String outputFile) {
        ObjectMapper objectMapper = new ObjectMapper()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .findAndRegisterModules();

        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());

        try {
             // create a writer
            FileWriter writer = new FileWriter((outputFile)); 
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // convert blog[] object to JSON string, and write to file
            objectWriter.writeValue(bufferedWriter, blogs);
            // close writer
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }   
    }

    // read one java object (blog) as json - serialization (java object to json string)
    public static void save(Blog blogs, String outputFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()).
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        try {
            // convert blog object to JSON string, and write to file
            objectMapper.writeValue(Paths.get(outputFile).toFile(), blogs);
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }   
    }
}
