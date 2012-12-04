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

package keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet;


import java.util.ArrayList;

import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <p>
 * @author Written by Pedro Antonio Gutierrez Penya, Aaron Ruiz Mora (University of Cordoba) 17/07/2007
 * @version 0.1
 * @since JDK1.5
 * </p>
 */

public abstract class AbstractNeuralNet implements INeuralNet{
	
	/**
	 * <p>
	 * Implementation of a neural net
	 * </p>
	 */
	
    /////////////////////////////////////////////////////////////////
    // ------------------------------------- Marshal/unmarshal format
    /////////////////////////////////////////////////////////////////

	/**
	 * <p>
	 * Marshal/Unmarshal input layer, hidden layers and output layer
	 * </p>
	 */
	
	protected static final javolution.xml.XmlFormat<AbstractNeuralNet> XML = 
		new XmlFormat<AbstractNeuralNet>(AbstractNeuralNet.class) 
		{
			public void format(AbstractNeuralNet source, XmlElement xml) 
			{
				// Marshal input layer
				xml.add(source.inputLayer, "input-layer");
				// Marshal hidden layers
				xml.add(source.hiddenLayers, "hidden-layers");
				// Marshal output layers
				xml.add(source.outputLayer, "output-layer");
			}

			public AbstractNeuralNet parse(XmlElement xml) 
			{
				// Resulting object
				AbstractNeuralNet result = (AbstractNeuralNet) xml.object();
				// Unmarshal input layer
				result.inputLayer = xml.<InputLayer>get("input-layer");
				// Unmarshal hidden layers
				result.hiddenLayers = xml.<ArrayList<LinkedLayer>>get("hidden-layers");
				// Update neuron references
				ILayer<? extends INeuron> previousLayer = result.inputLayer;
				for(LinkedLayer hiddenLayer:result.hiddenLayers){					
					for(int i=0; i<hiddenLayer.getNofneurons();i++){
						Link [] links = hiddenLayer.getNeuron(i).getLinks();
						for(int j=0; j<links.length; j++){
							links[j].setTarget(hiddenLayer.getNeuron(i));
							if(!links[j].isBroken() && j<previousLayer.getMaxnofneurons())
								links[j].setOrigin(previousLayer.getNeuron(j));
							else
								links[j].setOrigin(null);
						}
					}
					previousLayer = hiddenLayer;
				}
				// Unmarshal output layer
				result.outputLayer = xml.<LinkedLayer>get("output-layer");
				// Update neuron references
				for(int i=0; i<result.outputLayer.getNofneurons();i++){
					Link [] links = ((LinkedNeuron)result.outputLayer.getNeuron(i)).getLinks();
					for(int j=0; j<links.length; j++){
						links[j].setTarget(result.outputLayer.getNeuron(i));
						if(!links[j].isBroken() && j<previousLayer.getMaxnofneurons())
							links[j].setOrigin(previousLayer.getNeuron(j));
						else
							links[j].setOrigin(null);
					}
				}
				// Return result
				return result;
			}

			public String defaultName() 
			{
				return "neural-net";
			}
		};
		
    /////////////////////////////////////////////////////////////////
    // --------------------------------------- Serialization constant
    /////////////////////////////////////////////////////////////////

	/** Generated by eclipse */
	
	private static final long serialVersionUID = -5162585162983545056L;
	
    /////////////////////////////////////////////////////////////////
    // ------------------------------------------ Protected Variables
    /////////////////////////////////////////////////////////////////
    
    /** Input layer */
    protected InputLayer inputLayer;
    
    /** Hidden layer */
    protected ArrayList<LinkedLayer> hiddenLayers = 
        new ArrayList<LinkedLayer>();
    
    /** Ouput layer */
    protected LinkedLayer outputLayer;
    
    /////////////////////////////////////////////////////////////////
    // -------------------------------------------------- Constructor
    /////////////////////////////////////////////////////////////////
    
    /**
     * <p>
     * Empty constructor
     * </p>
     */
    public AbstractNeuralNet() {
        super();
    }
    
    /////////////////////////////////////////////////////////////////
    // ---------------------------- Implementing INeuralNet interface
    /////////////////////////////////////////////////////////////////
    
    /**
     * <p>
     * Returns the number of hidden layers of the neural net
     * </p>
     * @return int Number of hidden layers
     */
    public int getNofhlayers() {
        return hiddenLayers.size();
    }
    
    /**
     * <p>
	 * Returns the input layer of this neural net
	 * </p>
	 * @return InputLayer Input layer of the net
	 */
    public InputLayer getInputLayer() {
        return inputLayer;
    }
    
    /**
     * <p>
	 * Sets the input layer of this neural net
	 * </p>
	 * @param inputLayer New input layer of the net
	 */
    public void setInputLayer(InputLayer inputLayer) {
        this.inputLayer = inputLayer;
    }
    
    /**
     * <p>
     * Adds a new layer to the neural net
     * </p>
     * @param layer New layer
     */
    public void addHlayer(LinkedLayer layer) {
        hiddenLayers.add(layer);
    }
    
    /**
     * <p>
     * Returns a specific hidden layer of the neural net
     * </p>
     * @param index Number of layer to return
     * @return LinkedLayer Hidden layer
     */
    public LinkedLayer getHlayer(int index) {
        return hiddenLayers.get(index);
    }
    
    /**
     * <p>
	 * Returns the output layer of this neural net
	 * </p>
	 * @return LinkedLayer Output layer of the net
	 */
    public LinkedLayer getOutputLayer() {
        return outputLayer;
    }
    
    /**
     * <p>
	 * Sets the output layer of this neural net
	 * </p>
	 * @param outputLayer New output layer of the net
	 */
    public void setOutputLayer(LinkedLayer outputLayer) {
        this.outputLayer = outputLayer;
    }
    
    /**
     * <p>
     * Checks if this neural net is equal to another
     * </p>
     * @param other Other neural net to compare
     * @return true if both neural nets are equals
     */
    public boolean equals(INeuralNet other){
        if(this.hashCode()!=other.hashCode())
            return false;
        else
            return true;
    }
    
    /**
     * <p>
	 * Returns an integer number that identifies the neural net
	 * </p>
	 * @return int Hashcode
	 */
    public int hashCode(){
        HashCodeBuilder hcb = new HashCodeBuilder(61, 67);
        hcb.append(outputLayer);
        return hcb.toHashCode();
    }
    
    /**
     * <p>
     * Checks if this neural net is full of neurons
     * </p>
     * @return true if the neural net is full of neurons
     */
    public boolean neuronsFull()
    {
        for(LinkedLayer layer:hiddenLayers){
            if(!layer.neuronsFull())
                return false;
        }
        return true;
    }
    
    /**
     * <p>
     * Checks if this neural net is empty of neurons
     * </p>
     * @return true if the neural net is empty of neurons
     */
    public boolean neuronsEmpty()
    {
        for(LinkedLayer layer:hiddenLayers){
            if(!layer.neuronsEmpty())
                return false;
        }
        return true;
    }
    
    /**
     * <p>
     * Checks if this neural net is full of links
     * </p>
     * @return true if the neural net is full of links
     */
    public boolean linksFull() {
    	
    	// If here is not hidden layers
    	if(hiddenLayers.size()==0) {
    		if( !outputLayer.linksFull(inputLayer) )
        		return false;
    	}
    	else { 
    		// First hidden layer with input layer
    		if ( !hiddenLayers.get(0).linksFull(inputLayer) )
    			return false;
    		
    		// Hidden layer with its previous hidden layer
    		for(int i=1; i<hiddenLayers.size(); i++)
    			if( !hiddenLayers.get(i).linksFull(hiddenLayers.get(i-1)) )
    				return false;
    		
    		// Output layer with the last hidden layer
    		if( !outputLayer.linksFull(hiddenLayers.get(hiddenLayers.size()-1)) )
        		return false;
    	}
        
        return true;
    }
    
    /**
     * <p>
     * Checks if this neural net is empty of links
     * </p>
     * @return true if the neural net is empty of links
     */
    public boolean linksEmpty()
    {
        for(LinkedLayer layer:hiddenLayers){
            if(!layer.linksEmpty())
                return false;
        }
        
        if(!outputLayer.linksEmpty())
            return false;
        
        return true;
    }
    
    /**
     * <p>
	 * Returns the number of hidden neurons of this neural net
	 * </p>
	 * @return int Number of hidden neurons
	 */
    public int getNofhneurons()
    {
        int nofhneurons = 0;
        for(LinkedLayer layer:hiddenLayers){
            nofhneurons += layer.getNofneurons();
        }
        return nofhneurons;
    }
    
    /**
     * <p>
	 * Returns the number of effective links of this neural net
	 * </p>
	 * @return int Number of effective links
	 */
    public int getNoflinks()
    {
        int noflinks = 0;
        for(LinkedLayer layer:hiddenLayers){
            noflinks += layer.getNoflinks();
        }
        noflinks += outputLayer.getNoflinks();
        return noflinks;
    }
    
    /**
     * <p>
	 * Keep relevant links, that is, those links whose weight is higher
	 * than certain number
	 * </p>
     * @param significativeWeight Significative weight
	 */
    public void keepRelevantLinks(double significativeWeight){
    	for(LinkedLayer layer:hiddenLayers){
    		layer.keepRelevantLinks(significativeWeight);
    	}
    	outputLayer.keepRelevantLinks(significativeWeight);
    }
    
    /**
     * <p>
     * Returns a string representation of the neural net
     * </p>
     * @return String Representation of the neural net
     */
    public String toString(){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<outputLayer.getNofneurons(); i++){
            String neuron = outputLayer.getNeuron(i).toString();
            neuron = neuron.replace("+-", "-");
            neuron = neuron.replace("-+", "-");
            neuron = neuron.replace("--", "+");
            
            // Add " " at the end of the string so the next step works correctly
            neuron+=" ";
            // Add a "\n" at the end of each node
            int count=0;
            for(int j=0; j<neuron.length(); j++) {
            	if(neuron.charAt(j)=='(')
            		count++;
            	if(neuron.charAt(j)==')') {
            		count--;
            		if(count==0)
            			neuron = neuron.substring(0, j+1)+"\n"+neuron.substring(j+2, neuron.length());
            	}
            }
            
            sb.append(neuron);
        }
        return sb.toString();
    }
    
    /**
     * <p>
     * Returns a copy of this neural net
     * </p>
     * @return INeuralNet Copy of this neural net
     */
    public INeuralNet copy(){
    	
    	// Resulting neural net
    	AbstractNeuralNet result = null;
    	
		try {
	    	result = this.getClass().newInstance();
	    	
	    	// Copy the input layer
	    	result.inputLayer = this.inputLayer.copy();
	    	
	    	// Copy hidden layers
	    	ILayer<? extends INeuron> lastCopiedLayer = result.inputLayer;
	    	for(LinkedLayer hl:this.hiddenLayers){
	    		// Copy hidden layer
	    		LinkedLayer hlr = hl.copy(lastCopiedLayer);
	    		result.addHlayer(hlr);
	    		
	    		// Update last copied layer
	    		lastCopiedLayer = hlr;
	    	}
	    	
	    	// Copy the output layer
	    	result.outputLayer = this.outputLayer.copy(lastCopiedLayer);
		}catch (InstantiationException e) {
			System.out.println("Instantiation Error " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println("Illegal Access Error " + e.getLocalizedMessage());
			e.printStackTrace();
		}
    	return result;
    }
}

