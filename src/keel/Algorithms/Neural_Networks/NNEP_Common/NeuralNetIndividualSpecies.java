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

package keel.Algorithms.Neural_Networks.NNEP_Common;

import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.INeuralNet;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.InputLayer;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.LinkedLayer;
import net.sf.jclec.IConfigure;
import net.sf.jclec.util.range.Interval;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * <p>
 * @author Written by Pedro Antonio Gutierrez Penia (University of Cordoba) 16/7/2007
 * @author Written by Aaron Ruiz Mora (University of Cordoba) 16/7/2007
 * @version 0.1
 * @since JDK1.5
 * </p>
 */

public class NeuralNetIndividualSpecies extends AbstractNeuralNetSpecies<NeuralNetIndividual> implements IConfigure
{
	/**
	 * <p>
	 * Individuals that use a INeuralNet as genotype
	 * </p>
	 */
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	private static final long serialVersionUID = 2813451004932981929L;
	
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
		
	/**
	 * <p>
	 * Empty constructor
	 * </p>
	 */
	 
	public NeuralNetIndividualSpecies() 
	{
		super();
	}
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Setting properties
	/////////////////////////////////////////////////////////////////

	/**
	 * <p>
	 * Sets number of hidden layers of the neural nets
	 * 
	 * @param nOfHiddenLayers Number of hidden layers
	 * </p>
	 */
	
	public void setNOfHiddenLayers(int nOfHiddenLayers) {
		this.nOfHiddenLayers = nOfHiddenLayers;
	}

	/**
	 * <p>
	 * Sets number of inputs of the neural nets
	 * 
	 * @param nOfInputs Number of hidden layers
	 * </p>
	 */
	
	public void setNOfInputs(int nOfInputs) {
		this.nOfInputs = nOfInputs;
	}

	/**
	 * <p>
	 * Sets number of outputs of the neural nets
	 * 
	 * @param nOfOutputs Number of hidden layers
	 * </p>
	 */
	
	public void setNOfOutputs(int nOfOutputs) {
		this.nOfOutputs = nOfOutputs;
	}

	/**
	 * <p>
	 * Sets weight range of a hidden layer
	 * 
	 * @param index Index of the desired hidden layer
	 * @param indexRange Index of the desired range into the layer (useful for hibrid layer) 
	 * @param hiddenLayerWeightRange New weight range
	 * </p>
	 */
	
	public void setHiddenLayerWeightRange(int index, int indexRange, Interval hiddenLayerWeightRange) {
		this.weightRanges[index][indexRange] = hiddenLayerWeightRange;
	}

	/**
	 * <p>
	 * Sets weight range of the output layer
	 * 
	 * @param indexRange Index of the desired range into the layer (useful for hibrid layer) 
	 * @param outputLayerWeightRange New weight range
	 * </p>
	 */
	
	public void setOutputLayerWeightRange(int indexRange, Interval outputLayerWeightRange) {
		this.weightRanges[weightRanges.length-1][indexRange] = outputLayerWeightRange;
	}

	/**
	 * <p>
	 * Sets maximum number of neurons of a hidden layer
	 * 
	 * @param index Index of the desired hidden layer
	 * @param hiddenLayerMaxNofneurons Maximum number of neurons
	 * </p>
	 */
	
	public void setHiddenLayerMaxNofneurons(int index, int hiddenLayerMaxNofneurons) {
		this.maxNofneurons[index] = hiddenLayerMaxNofneurons;
	}
	
	/**
	 * <p>
	 * Sets minimum number of neurons of a hidden layer
	 * 
	 * @param index Index of the desired hidden layer
	 * @param hiddenLayerMinNofneurons Minimum number of neurons
	 * </p>
	 */
	
	public void setHiddenLayerMinNofneurons(int index, int hiddenLayerMinNofneurons) {
		this.minNofneurons[index] = hiddenLayerMinNofneurons;
	}

	/**
	 * <p>
	 * Sets initial maximum number of neurons of a hidden layer
	 * 
	 * @param index Index of the desired hidden layer
	 * @param hiddenLayerInitialMaxNofneurons Initial maximum number of neurons
	 * </p>
	 */
	
	public void setHiddenLayerInitialNofneurons(int index, int hiddenLayerInitialMaxNofneurons) {
		this.initialMaxNofneurons[index] = hiddenLayerInitialMaxNofneurons;
	}
	
	/**
	 * <p>
	 * Sets type of neurons of a hidden layer
	 * 
	 * @param index Index of the desired hidden layer
	 * @param hiddenLayerType Type of neurons
	 * </p>
	 */
	
	public void setHiddenLayerType(int index, String hiddenLayerType) {
		this.type[index] = hiddenLayerType;
	}
	
	/**
	 * <p>
	 * Sets type of neurons of the output layer
	 * 
	 * @param outputLayerType Type of neurons
	 * </p>
	 */
	
	public void setOutputLayerType(String outputLayerType) {
		this.type[type.length-1] = outputLayerType;
	}
	
	/**
	 * <p>
	 * Sets initiator of neurons of a hidden layer
	 * 
	 * @param index Index of the desired hidden layer
	 * @param hiddenLayerInitiator Initiator of neurons
	 * </p>
	 */
	
	public void setHiddenLayerInitiator(int index, String hiddenLayerInitiator) {
		this.initiator[index] = hiddenLayerInitiator;
	}
	
	/**
	 * <p>
	 * Sets initiator of neurons of the output layer
	 * 
	 * @param outputLayerInitiator Initiator of neurons
	 * </p>
	 */
	
	public void setOutputLayerInitiator(String outputLayerInitiator) {
		this.initiator[initiator.length-1] = outputLayerInitiator;
	}

	/**
	 * <p>
	 * Sets a boolean indicating if a hidden layer is biased
	 * 
	 * @param index Index of the desired hidden layer
	 * @param hiddenLayerBiased Is hidden layer biased?
	 * </p>
	 */
	
	public void setHiddenLayerBiased(int index, boolean hiddenLayerBiased) {
		this.biased[index] = hiddenLayerBiased;
	}
	
	/**
	 * <p>
	 * Sets a boolean indicating if the output layer is biased
	 * 
	 * @param outputLayerBiased Is output layer biased?
	 * </p>
	 */
	
	public void setOutputLayerBiased(boolean outputLayerBiased) {
		this.biased[biased.length-1] = outputLayerBiased;
	}
	
	/**
	 * <p>
	 * Sets an array of neuron types of a concrete layer
	 * (this is an hibrid layer)
	 * 
	 * @param index Index of the desired hidden layer
	 * @param neuronTypes Array of neurons types
	 * </p>
	 */
	
	public void setNeuronTypes(int index, String[] neuronTypes) {
		this.neuronTypes[index] = neuronTypes;
	}
	
	/**
	 * <p>
	 * Sets an array of percentages of a concrete layer
	 * (this is an hibrid layer)
	 * 
	 * @param index Index of the desired hidden layer
	 * @param percentages Array of percentages
	 * </p>
	 */
	
	public void setPercentages(int index, double[] percentages) {
		this.percentages[index] = percentages;
	}
	
	/**
	 * <p>
	 * Sets an array of initiators of neurons of hibrid layers
	 * 
	 * @param index Index of the desired hidden layer
	 * @param initiatorNeuronTypes Array of initiators of neurons
	 * </p>
	 */
	
	public void setInitiatorNeuronTypes(int index, String[] initiatorNeuronTypes) {
		this.initiatorNeuronTypes[index] = initiatorNeuronTypes;
	}
	
	/////////////////////////////////////////////////////////////////
	// --------------------- Implementing INeuralNetSpecies interface
	/////////////////////////////////////////////////////////////////
    
	/**
	 * <p>
	 * Creates a new individual
	 * @return A NeuraNetIndividual
	 * </p>
	 */
	
	public NeuralNetIndividual createIndividual() 
	{
		return new NeuralNetIndividual();
	}

	/**
	 * <p>
	 * Creates a new individual
	 * @param genotype Genotype of the individual
	 * @return A NeuraNetIndividual
	 * </p>
	 */	
	public NeuralNetIndividual createIndividual(INeuralNet genotype) 
	{
		return new NeuralNetIndividual(genotype);
	}
	

	/**
	 * <p>
	 * Creates the genotype of the individual
	 * @return The Genotype
	 * <(p>
	 */
	
	@SuppressWarnings("unchecked")
	public INeuralNet createGenotype() 
	{
		INeuralNet result = null;
		
		// New neural net
		try {
			Class<INeuralNet> neuralNetClass = 
				(Class<INeuralNet>) Class.forName(neuralNetType);
			
			// Range instance
			result = neuralNetClass.newInstance();
			
			//Generate topology
			generateTopology(result);
			
			//Set input layer number of neurons
			result.getInputLayer().setMaxnofneurons(nOfInputs);
		}
		catch (ClassNotFoundException e) {
			throw new ConfigurationRuntimeException("Illegal neural net classname");
		} 
		catch (InstantiationException e) {
			throw new ConfigurationRuntimeException("Problems creating an instance of " + type, e);
		} 
		catch (IllegalAccessException e) {
			throw new ConfigurationRuntimeException("Problems creating an instance of " + type, e);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * <p>
	 * Generate topology of a INeuralNet
	 * 
	 * @param neuralNet New neural net genotype
	 * </p>
	 */
	
	private void generateTopology(INeuralNet neuralNet){		
		//Input Layer
		InputLayer inputLayer = new InputLayer();
		neuralNet.setInputLayer(inputLayer);
		
		try {
			//Each hidden Layer
			for(int i=0; i<nOfHiddenLayers; i++){
				LinkedLayer hiddenLayer = 
					(LinkedLayer) Class.forName(getHiddenLayerType(i)).newInstance();
				hiddenLayer.setType(LinkedLayer.HIDDEN_LAYER);
				neuralNet.addHlayer(hiddenLayer);
			}
			
			//Output Layer
			LinkedLayer outputLayer = 
				(LinkedLayer) Class.forName(getOutputLayerType()).newInstance();
			outputLayer.setType(LinkedLayer.OUTPUT_LAYER);
			neuralNet.setOutputLayer(outputLayer);
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------- Overwriting Object methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * <p>
	 * Compare two individuals
	 * @return True if the two individuals are equals
	 * </p>
	 */
	
	public boolean equals(Object other)
	{
		if (other instanceof NeuralNetIndividualSpecies) {
			EqualsBuilder eb = new EqualsBuilder();
			NeuralNetIndividualSpecies nnoth = (NeuralNetIndividualSpecies) other;
			eb.append(this.neuralNetType, nnoth.neuralNetType);
			eb.append(this.nOfInputs, nnoth.nOfInputs);
			eb.append(this.nOfHiddenLayers, nnoth.nOfHiddenLayers);
			eb.append(this.nOfOutputs, nnoth.nOfOutputs);
			eb.append(this.weightRanges, nnoth.weightRanges);
			eb.append(this.maxNofneurons, nnoth.maxNofneurons);
			eb.append(this.minNofneurons, nnoth.minNofneurons);
			eb.append(this.initialMaxNofneurons, nnoth.initialMaxNofneurons);
			eb.append(this.type, nnoth.type);
			eb.append(this.initiator, nnoth.initiator);
			eb.append(this.biased, nnoth.biased);
			eb.append(this.neuronTypes, nnoth.neuronTypes);
			eb.append(this.percentages, nnoth.percentages);
			eb.append(this.initiatorNeuronTypes, nnoth.initiatorNeuronTypes);
			return eb.isEquals();
		}
		else {
			return false;
		}
	}
	
	
	/////////////////////////////////////////////////////////////////
	// ---------------------------- Implementing IConfigure interface
	/////////////////////////////////////////////////////////////////

	/**
	 * <p>
	 * Configuration parameters for this species are:
	 * 
	 * 
	 * input-layer.number-of-inputs (int)
	 *  Number of inputs. Number of inputs of this species neural nets.
	 *
	 * output-layer.number-of-outputs (int)
	 *  Number of inputs. Number of outputs of this species neural nets.
	 * 
	 * hidden-layer(i).weight-range (complex)
	 *  Weigth range of the hidden layer number "i".
	 * 
	 * output-layer.weight-range (complex)
	 *  Weigth range of the outputlayer.
	 *  
	 * hidden-layer(i).maximum-number-of-neurons (int)
	 *  Maximum number of neurons of hidden layer number "i".
	 * 
	 * hidden-layer(i).initial-number-of-neurons (int)
	 *  Initial number of neurons of hidden layer number "i".
	 * 
	 * hidden-layer(i)[@type] (string)
	 *  Layer type of the hidden layer number "i".
	 *  
	 * output-layer[@type] (string)
	 *  Layer type of the output layer.
	 * 
	 * hidden-layer(i)[@biased] (string)
	 *  Boolean indicating if hidden layer number "i" is biased.
	 * 
	 * output-layer[@type] (string)
	 *  Boolean indicating if the output layer is biased.
	 *  
	 *  @param configuration Configuration if the Individual
	 * </p>
	 */

	@SuppressWarnings("unchecked")
	public void configure(Configuration configuration)
    {    	
		// -------------------------------------- Setup neuralNetType
		neuralNetType = configuration.getString("neural-net-type");
    	
		// -------------------------------------- Setup nOfHiddenLayers 
    	nOfHiddenLayers = configuration.getList("hidden-layer[@type]").size();
    	
		// -------------------------------------- Setup nOfInputs 
		//nOfInputs = configuration.getInt("input-layer.number-of-inputs");
		
		// -------------------------------------- Setup nOfOutputs
    	//nOfOutputs = configuration.getInt("output-layer.number-of-outputs");
    	
    	// Initialize arrays
	    maxNofneurons = new int[nOfHiddenLayers];
	    minNofneurons = new int[nOfHiddenLayers];
	    initialMaxNofneurons = new int[nOfHiddenLayers];
	    type = new String[nOfHiddenLayers+1];
	    initiator = new String[nOfHiddenLayers+1];
	    biased = new boolean[nOfHiddenLayers+1];
    	
    	weightRanges = new Interval[nOfHiddenLayers+1][];
    	neuronTypes = new String[nOfHiddenLayers+1][];
    	percentages = new double[nOfHiddenLayers+1][];
    	initiatorNeuronTypes = new String[nOfHiddenLayers+1][];
    	for(int i =0; i<nOfHiddenLayers+1; i++){
    		String header;
    		
    		if(i!=nOfHiddenLayers){
				header = "hidden-layer("+i+")";
				// ---------------------------------- Setup maxNofneurons array
				maxNofneurons[i] = configuration.getInt(header + ".maximum-number-of-neurons");
				// ---------------------------------- Setup minNofneurons array
				minNofneurons[i] = configuration.getInt(header + ".minimum-number-of-neurons");
				// ---------------------------------- Setup initialMaxNofneurons array
				initialMaxNofneurons[i] = configuration.getInt(header + ".initial-maximum-number-of-neurons");
				// ---------------------------------- Setup initiator array
				initiator[i] = configuration.getString(header + ".initiator-of-links");
    		}
    		else {
    			header = "output-layer";
    			// ---------------------------------- Setup initiator array
    			initiator[i] = configuration.getString(header + ".initiator-of-links");
    		}

			//  ----------------------------------------- Setup type array
			type[i] = configuration.getString(header + "[@type]");
						
			//  ----------------------------------------- Setup biased array
			biased[i] = configuration.getBoolean(header + "[@biased]");
    		
			
	        // -------------------------------------- Setup weight ranges array
			weightRanges[i] = new Interval[1];
			try {
				// Range name
				String rangeName = header + ".weight-range";
				// Range classname
				String rangeClassname = configuration.getString(rangeName + "[@type]");
				// Range class
				Class<Interval> rangeClass = 
					(Class<Interval>) Class.forName(rangeClassname);
				// Range instance
				Interval range = rangeClass.newInstance();
				// Configura range
				range.configure(configuration.subset(rangeName));
				// Set range
				if(i!=nOfHiddenLayers)
					setHiddenLayerWeightRange(i, 0, range);
				else
					setOutputLayerWeightRange(0, range);
			} 
			catch (ClassNotFoundException e) {
				throw new ConfigurationRuntimeException("Illegal range classname");
			} 
			catch (InstantiationException e) {
				throw new ConfigurationRuntimeException("Problems creating an instance of range", e);
			} 
			catch (IllegalAccessException e) {
				throw new ConfigurationRuntimeException("Problems creating an instance of range", e);
			}			
    	}
    }
}

