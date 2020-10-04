/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biordm.sbol.toolkit.scrapbook;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Tomasz Zielinski
 */
public class MetaReader {
    
    
    Map<String,String[]> desc = Map.of();
    
    MetaReader() {
        
    }
    
    public MetaReader(Path file, int skip) {
        init(file, skip);
    }
    
    public void init(Path file, int skip) {
        desc = parse(file, skip);
    }
    
    public Set<String> ids() {
        return desc.keySet();
    }
    
    public String[] meta(String id) {
        String[] meta = desc.get(id);
        if (meta == null) throw new IllegalArgumentException("Missing id "+id);
        return meta;
    }
    
    public String notes(String id) {
        String[] meta = meta(id);
        
        String desc = "Summary: "+meta[4] +"\n";
        desc += "Creator: "+meta[6] +"\n";
        desc += "Principal Investigator: "+meta[1] +"\n";
        desc += "BioSafety Level: "+meta[3] +"\n";
        desc += "Backbone: "+meta[9] +"\n";
        desc += "Origin of replication: "+meta[10] +"\n";
        desc += "Selection Markers: "+meta[11] +"\n";
        return desc;
    }
    
    public String description(String id) {
        String[] meta = meta(id);
        
        return meta[5];
    }    
    
    public String originOfRep(String id) {
        String[] meta = meta(id);
        
        return meta[10];
    }    
    
    public String selection(String id) {
        String[] meta = meta(id);
        
        return meta[11];
    }   
    
    public String backbone(String id) {
        String[] meta = meta(id);
        
        return meta[9];
    }     
    
    public Map<String,String[]> parse(Path file, int skip) {
        
        List<String[]> rows = read(file, skip);
        return rows.stream().collect(Collectors.toMap(t -> t[0].trim(), t -> t));
    }    
    
    List<String[]> read(Path file, int skip) {
        
        try (CSVReader csvReader = new CSVReader(Files.newBufferedReader(file))) {
            
            csvReader.skip(skip);
            return csvReader.readAll();
            
        } catch (IOException| CsvException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
