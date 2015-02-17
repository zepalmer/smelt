package com.bahj.smelt.syntax;

/**
 * Describes a textual location within a file.
 * 
 * @author Zachary Palmer
 */
public class SourceLocation {
    private String filename;
    private int line;
    private int column;

    public SourceLocation(String filename, int line, int column) {
        super();
        this.filename = filename;
        this.line = line;
        this.column = column;
    }

    public String getFilename() {
        return filename;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
    
    @Override
    public String toString() {
        return this.filename + "@" + this.line + ":" + this.column;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + ((filename == null) ? 0 : filename.hashCode());
        result = prime * result + line;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SourceLocation other = (SourceLocation) obj;
        if (column != other.column)
            return false;
        if (filename == null) {
            if (other.filename != null)
                return false;
        } else if (!filename.equals(other.filename))
            return false;
        if (line != other.line)
            return false;
        return true;
    }

}
