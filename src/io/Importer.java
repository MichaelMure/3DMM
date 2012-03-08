package io;

import java.io.FileNotFoundException;

import javax.media.j3d.BranchGroup;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;

public interface Importer {

	public BranchGroup LoadObject(String file) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException;
}
