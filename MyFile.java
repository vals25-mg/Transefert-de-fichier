package objets;

import java.io.Serializable;

public class MyFile implements Serializable{
    
    String name;
    int size;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
   
    public MyFile(String name,int size) {
        setName(name);
        setSize(size);
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    
    
}
