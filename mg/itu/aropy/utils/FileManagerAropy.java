package mg.itu.aropy.utils;

public class FileManagerAropy {
    String filename;
    String extension;
    byte[] file;
    
    public FileManagerAropy(String filename, String extension, byte[] file){
        this.filename = filename;
        this.extension = extension;
        this.file = file;
    }
    public String getFilename() {
        return filename;
    }
    public String getExtension() {
        return extension;
    }
    public byte[] getFile() {
        return file;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public void setFile(byte[] file) {
        this.file = file;
    }
}