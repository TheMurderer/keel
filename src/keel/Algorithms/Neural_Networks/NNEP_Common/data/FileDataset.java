/***********************************************************************

	This file is part of KEEL-software, the Data Mining tool for regression, 
	classification, clustering, pattern mining and so on.

	Copyright (C) 2004-2010
	
	F. Herrera (herrera@decsai.ugr.es)
    L. S�nchez (luciano@uniovi.es)
    J. Alcal�-Fdez (jalcala@decsai.ugr.es)
    S. Garc�a (sglopez@ujaen.es)
    A. Fern�ndez (alberto.fernandez@ujaen.es)
    J. Luengo (julianlm@decsai.ugr.es)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see http://www.gnu.org/licenses/
  
**********************************************************************/

package keel.Algorithms.Neural_Networks.NNEP_Common.data;


import java.io.Reader;

import org.apache.commons.configuration.Configuration;

/**
 * <p>
 * @author Written by Amelia Zafra, Sebastian Ventura (University of Cordoba) 17/07/2007
 * @version 0.1
 * @since JDK1.5
 * </p>
 */

public class FileDataset extends AbstractDataset
{
	/**
	 * <p>
	 * File dataset
	 * </p>
	 */

	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 1L;
	
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////

	
	/** Data file name */
	
	protected String fileName;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////
	
	/** Data file reader */

	protected Reader fileReader;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * <p>
     * Empty constructor
	 * </p>
     */
	public FileDataset() 
	{
		super();
	}

	/**
	 * <p>
     * Constructor that receives the name of the file to be opened
	 * </p>
	 * @param fileName Name of the file to be opened
     */
	public FileDataset(String fileName) 
	{
		super();
		setFileName(fileName);
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Setting and getting properties
	/**
	 * <p>
     * Gets the file name
	 * </p>
	 * @return String The name of the file
     */
	public String getFileName() 
	{
		return fileName;
	}

	/**
	 * <p>
     * Sets the file name
	 * </p>
	 * @param fileName The name of the file to be set
     */
	public void setFileName(String fileName) 
	{
		this.fileName = fileName;
	}

	// IDataset interface
	
	/**  
	 * <p>
	 * Open dataset 	 
	 * </p>
	 * @throws DatasetException If dataset can't be opened
	 */
	@Override
	public void open() throws DatasetException {
		// TODO Auto-generated method stub
		
	}

	/**  
	 * <p>
	 * Close dataset
	 * </p>
	 * @throws DatasetException If dataset can't be closed
	 */
	@Override
	public void close() throws DatasetException {
		
		// TODO Auto-generated method stub
		
	}

	/**
	 * <p>
	 * Move cursor to index position
	 * </p>
	 * @param index New cursor position
	 * @return true|false 
	 * @throws DatasetException if a source access error occurs
	 */
	@Override
	public boolean move(int index) throws DatasetException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * <p>
	 * Return the next instance
	 * </p>
	 * @return The next instance
	 * @throws DatasetException if a source access error occurs
	 */
	@Override
	public boolean next() throws DatasetException {
		// TODO Auto-generated method stub
		return false;
	}

	/** 
	 * <p>
	 * Reset dataset
	 * </p>
	 * @throws DatasetException if a source access error occurs
	 */	
	@Override
	public void reset() throws DatasetException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * <p>
	 * Returns cursor instance
	 * </p>
	 * @return Actual instance (if exists)
	 * @throws DatasetException if a source access error occurs
	 */
	public IInstance read() throws DatasetException {
		// TODO Auto-generated method stub
		return null;
	}

	
	/////////////////////////////////////////////////////////////////
	// ---------------------------- Implementing IConfigure interface
	/////////////////////////////////////////////////////////////////
	
	/**
	 * <p>
	 * Configuration method
	 * Configuration parameters for ArrayDataset are:
	 * </p> 
	 * @param settings Configuration object to read the parameters 
	 */
	public void configure(Configuration settings)
	{
		// Set number-of-parents
		String fileName = settings.getString("file-name");
		setFileName(fileName);
		
	}
}

