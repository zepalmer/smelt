package com.bahj.smelt.model.syntax.datamodel.lexer;

/**
 * Represents a location in a file.
 * 
 * @author Zachary Palmer
 */
public class SourceLocation {
	public static final String EOF_DESCRIPTION = "<eof>";
	
	private String filename;
	private int line;
	private int column;

	/**
	 * Creates a new source location at the start of a file.
	 * 
	 * @param filename
	 *            The filename.
	 */
	public SourceLocation(String filename) {
		this(filename, 1, 0);
	}

	/**
	 * Creates a new source location at the specified filename, line (1-based),
	 * and column (0-based).
	 * 
	 * @param filename
	 *            The filename.
	 * @param line
	 *            The 1-based line.
	 * @param column
	 *            The 0-based column.
	 */
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

	public SourceLocation nextLine() {
		return new SourceLocation(this.filename, this.line + 1, 0);
	}

	public SourceLocation nextColumn() {
		return new SourceLocation(this.filename, this.line, this.column + 1);
	}
	
	public String toString() {
		return this.filename + "@" + this.line + ":" + this.column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
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
