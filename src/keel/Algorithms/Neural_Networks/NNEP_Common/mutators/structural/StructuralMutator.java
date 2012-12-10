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

 package keel.Algorithms.Neural_Networks.NNEP_Common.mutators.structural;



import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import keel.Algorithms.Neural_Networks.NNEP_Common.NeuralNetIndividual;
import keel.Algorithms.Neural_Networks.NNEP_Common.mutators.NeuralNetMutator;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.ExpNeuron;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.ILayer;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.INeuralNet;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.INeuron;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.LinearNeuron;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.Link;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.LinkedLayer;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.LinkedNeuron;
import keel.Algorithms.Neural_Networks.NNEP_Common.neuralnet.SigmNeuron;
import net.sf.jclec.IConfigure;
import net.sf.jclec.fitness.SimpleValueFitness;
import net.sf.jclec.util.range.Interval;

import org.apache.commons.configuration.Configuration;

/**  
 * <p>
 * @author Written by Pedro Antonio Gutierrez Penia (University of Cordoba) 16/7/2007
 * @author Written by Aaron Ruiz Mora (University of Cordoba) 16/7/2007
 * @version 0.1
 * @since JDK1.5
 * </p>
 */

public class StructuralMutator<I extends NeuralNetIndividual> extends NeuralNetMutator<I> implements IConfigure
{

    /**
     * <p>
     * Structural mutator for neural nets
     * 
     * IMPORTANT NOTE: Structural mutator works directly with  he individuals instead
     *                 of returning a mutated copy of them. This is for performance 
     *                 reasons. If you want to use another mutator you have to consider 
     *                 that individuals will be changed when you use structural mutation
     * </p>                 
     */
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -7550526084120396151L;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Temperature exponent for the mutations */
	
	protected double temperExponent;
	
	/** Minimum number of neurons to add in the mutations */
	
	protected int minNeuronsAdd;
	
	/** Maximum number of neurons to add in the mutations */
	
	protected int maxNeuronsAdd;
	
	/** Minimum number of neurons to remove in the mutations */
	
	protected int minNeuronsDel;
	
	/** Maximum number of neurons to remove in the mutations */
	
	protected int maxNeuronsDel;
	
	/** Minimum number of links to add in the mutations */
	
	protected int minLinksAdd;
	
	/** Maximum number of links to add in the mutations */
	
	protected int maxLinksAdd;
	
	/** Minimum number of links to remove in the mutations */
	
	protected int minLinksDel;
	
	/** Maximum number of links to remove in the mutations */
	
	protected int maxLinksDel;
	
	/** Minimum value of new weigths */
	
	protected double significativeWeigth;
	
	/** Has the operator a relative added/removed number of links, depending of the neural net*/
	
	protected boolean nOfLinksRelative;
	
	/** Percentage of links to remove in the mutations */
	
	protected int hiddenLinksPercentage;
	
	/** Percentage of links to remove in the mutations */
	
	protected int outputLinksPercentage;
	
	/** Structural Mutators of a specific neuron */
	
	protected Hashtable<String,INeuronStructuralMutator> neuronStructuralMutators = new Hashtable<String,INeuronStructuralMutator>();
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------------- Constructor
	/////////////////////////////////////////////////////////////////
	
    /**
     * <p>
     * Empty Constructor
     * </p>
     */
    
    public StructuralMutator() {
        super();
    }
    
	/////////////////////////////////////////////////////////////////
	// ------------------------------- Setting and getting Attributes
	/////////////////////////////////////////////////////////////////
    
    /**
     * <p>
	 * Returns the temperature exponent to be used in the mutations
	 * </p>
	 * @return double Temperature exponent
	 */
    
    public double getTemperExponent() {
        return temperExponent;
    }
    
    /**
     * <p>
	 * Sets the temperature exponent to be used in the mutations
	 * </p>
	 * @param temperExponent New temperature exponent
	 */
    
    public void setTemperExponent(double temperExponent) {
        this.temperExponent = temperExponent;
    }
    
    /**
     * <p>
	 * Returns the significative weigth for new links
	 * </p>
	 * @return double Significative new links weigth
	 */
    
    public double getSignificativeWeigth() {
        return significativeWeigth;
    }
    
    /**
     * <p>
	 * Sets the significative weigth for new links
	 * </p>
	 * @param significativeWeigth New significative weigth
	 */
    
    public void setSignificativeWeigth(double significativeWeigth) {
        this.significativeWeigth = significativeWeigth;
    }
    
    /**
     * <p>
	 * Returns the maximum number of links to add in mutations
	 * </p>
	 * @return int Maximum number of links to add
	 */
    
	public int getMaxLinksAdd() {
		return maxLinksAdd;
	}

    /**
     * <p>
	 * Sets the maximum number of links to add in mutations
	 * </p>
	 * @param maxLinksAdd New maximum number of links to add
	 */
	
	public void setMaxLinksAdd(int maxLinksAdd) {
		this.maxLinksAdd = maxLinksAdd;
	}

    /**
     * <p>
	 * Returns the maximum number of links to remove in mutations
	 * </p>
	 * @return int Maximum number of links to remove
	 */
	
	public int getMaxLinksDel() {
		return maxLinksDel;
	}

    /**
     * <p>
	 * Sets the maximum number of links to remove in mutations
	 * </p>
	 * @param maxLinksDel New maximum number of links to remove
	 */
	
	public void setMaxLinksDel(int maxLinksDel) {
		this.maxLinksDel = maxLinksDel;
	}

    /**
     * <p>
	 * Returns the maximum number of neurons to add in mutations
	 * </p>
	 * @return int Maximum number of neurons to add
	 */
	
	public int getMaxNeuronsAdd() {
		return maxNeuronsAdd;
	}
	
    /**
     * <p>
	 * Sets the maximum number of neurons to add in mutations
	 * </p>
	 * @param maxNeuronsAdd New maximum number of neurons to add
	 */

	public void setMaxNeuronsAdd(int maxNeuronsAdd) {
		this.maxNeuronsAdd = maxNeuronsAdd;
	}

    /**
     * <p>
	 * Returns the maximum number of neurons to remove in mutations
	 * </p>
	 * @return int Maximum number of neurons to remove
	 */
	
	public int getMaxNeuronsDel() {
		return maxNeuronsDel;
	}

    /**
     * <p>
	 * Sets the maximum number of neurons to remove in mutations
	 * </p>
	 * @param maxNeuronsDel New maximum number of neurons to remove
	 */
	
	public void setMaxNeuronsDel(int maxNeuronsDel) {
		this.maxNeuronsDel = maxNeuronsDel;
	}
	
    /**
     * <p>
	 * Returns the minimum number of links to add in mutations
	 * </p>
	 * @return int Minimum number of links to add
	 */

	public int getMinLinksAdd() {
		return minLinksAdd;
	}
	
    /**
     * <p>
	 * Sets the minimum number of links to add in mutations
	 * </p>
	 * @param minLinksAdd New maximum number of links to add in mutations
	 */

	public void setMinLinksAdd(int minLinksAdd) {
		this.minLinksAdd = minLinksAdd;
	}

    /**
     * <p>
	 * Returns the minimum number of links to remove in mutations
	 * </p>
	 * @return int Minimum number of links to remove
	 */
	
	public int getMinLinksDel() {
		return minLinksDel;
	}
	
    /**
     * <p>
	 * Sets the minimum number of links to remove in mutations
	 * </p>
	 * @param minLinksDel New maximum number of links to remove in mutations
	 */

	public void setMinLinksDel(int minLinksDel) {
		this.minLinksDel = minLinksDel;
	}

    /**
     * <p>
	 * Returns the minimum number of neurons to add in mutations
	 * </p>
	 * @return int Minimum number of neurons to add
	 */
	
	public int getMinNeuronsAdd() {
		return minNeuronsAdd;
	}

    /**
     * <p>
	 * Sets the minimum number of neurons to add in mutations
	 * </p>
	 * @param minNeuronsAdd New minimum number of neurons to add
	 */
	
	public void setMinNeuronsAdd(int minNeuronsAdd) {
		this.minNeuronsAdd = minNeuronsAdd;
	}

    /**
     * <p>
	 * Returns the minimum number of neurons to remove in mutations
	 * </p>
	 * @return int Minimum number of neurons to remove
	 */
	
	public int getMinNeuronsDel() {
		return minNeuronsDel;
	}

    /**
     * <p>
	 * Sets the minimum number of neurons to remove in mutations
	 * </p>
	 * @param minNeuronsDel New minimum number of neurons to remove
	 */
	
	public void setMinNeuronsDel(int minNeuronsDel) {
		this.minNeuronsDel = minNeuronsDel;
	}
	
	/////////////////////////////////////////////////////////////////
	// -------------------------- Overwriting AbstractMutator methods
	/////////////////////////////////////////////////////////////////
    
	/**
	 * <p>
	 * Mutates the next individual
	 * </p>
	 */
    
    public void mutateNext() {
    	I nnind = parentsBuffer.get(parentsCounter);
        INeuralNet neuralNet = nnind.getGenotype();
        double fitness = ((SimpleValueFitness) nnind.getFitness()).getValue();
        double temper = Math.pow(1-fitness, temperExponent);
        //System.out.println(temper);
        
        boolean mutated = false;
        boolean initial = true;
        int mutation = 0;
        
        //System.out.println("Antes --> " + neuralNet);
        
        while(!mutated)
        {
            //Add neurons (AN Mutation)
            if((initial && randgen.raw()<temper) || (!initial && mutation==1)){
            	
                int neuronsToAdd = (int) (minNeuronsAdd + ( randgen.raw()*temper*(maxNeuronsAdd-minNeuronsAdd) ));
            	//System.out.println(neuronsToAdd + " neuronas a�adidas");
                
                for(int i=0; i<neuronsToAdd; i++){
                    if( ANMutation(neuralNet) )
                        mutated=true;
                    else{
                    	//System.out.println("fallo");
                    	i=neuronsToAdd;
                    }
                }
                //System.out.println(neuralNet);
            }
            
            //Remove neurons (DN Mutation)
            if((initial && randgen.raw()<temper) || (!initial && mutation==2)){

                int neuronsToDel = (int) (minNeuronsDel + ( randgen.raw()*temper*(maxNeuronsDel-minNeuronsDel) ));
            	//System.out.println(neuronsToDel + " neuronas borradas");
                
                for(int i=0; i<neuronsToDel; i++){
                    if( DNMutation(neuralNet) )
                        mutated=true;
                    else{
                    	//System.out.println("fallo");
                    	i=neuronsToDel;
                    }
                }
                //System.out.println(neuralNet);                
            }
            
			//Add links (AL Mutation)
            if((initial && randgen.raw()<temper) || (!initial && mutation==3)){

            	//Relative mutation
            	if(nOfLinksRelative){
            		// Add links to hidden layers
            		for(int i=0; i<neuralNet.getNofhlayers(); i++){
            			int linksToAdd = (int) randgen.choose(1, (int) (( (hiddenLinksPercentage/100.) * neuralNet.getHlayer(i).getNoflinks())+1));
            			
        	            for(int j=0; j<linksToAdd; j++){
        	            	if( ALMutation(neuralNet, i) )
        	            		mutated=true;
        	            	else{
        	            		//System.out.println("fallo");
        	            		j=linksToAdd;
        	            	}
        	            }
            		}
            		
            		// Add links to output layer
        			int linksToAdd = (int) randgen.choose(1, (int) (( (outputLinksPercentage/100.) * neuralNet.getOutputLayer().getNoflinks())+1));
    	            
        			for(int j=0; j<linksToAdd; j++){
    	            	if( ALMutation(neuralNet, neuralNet.getNofhlayers()) )
    	            		mutated=true;
    	            	else{
    	            		//System.out.println("fallo");
    	            		j=linksToAdd;
    	            	}
    	            }
            	}
            	// Absolute mutation
            	else{
                    int linksToAdd = (int) (minLinksAdd + ( randgen.raw()*temper*(maxLinksAdd-minLinksAdd) ));
                    
    	            for(int i=0; i<linksToAdd; i++){
    	            	if( ALMutation(neuralNet, -1) )
    	            		mutated=true;
    	            	else{
    	            		//System.out.println("fallo");
    	            		i=linksToAdd;
    	            	}
    	            }
            	}
            	//System.out.println(linksToAdd + " enlaces a�adidos");
	            //System.out.println(neuralNet);
            }
            
			//Remove a link (DL Mutation)
            if((initial && randgen.raw()<temper) || (!initial && mutation==4)){
            	
            	//Relative mutation
            	if(nOfLinksRelative){
            		// Remove links of hidden layers
            		for(int i=0; i<neuralNet.getNofhlayers(); i++){
            			int linksToDel = (int) randgen.choose(1, (int) (( (hiddenLinksPercentage/100.) * neuralNet.getHlayer(i).getNoflinks())+1));
            			
        	            for(int j=0; j<linksToDel; j++){
        	            	if( DLMutation(neuralNet, i) )
        	            		mutated=true;
        	            	else {
        	            		//System.out.println("fallo");
        	            		j=linksToDel;
        	            	}
        	            }
            		}
            		
            		// Remove links of output layer
        			int linksToDel = (int) randgen.choose(1, (int) (( (outputLinksPercentage/100.) * neuralNet.getOutputLayer().getNoflinks())+1));
    	            
        			for(int j=0; j<linksToDel; j++){
    	            	if( DLMutation(neuralNet, neuralNet.getNofhlayers()) )
    	            		mutated=true;
    	            	else {
    	            		//System.out.println("fallo");
    	            		j=linksToDel;
    	            	}
    	            }
            	}
            	// Absolute mutation
            	else{
            		
                    int linksToDel = (int) (minLinksDel + ( randgen.raw()*temper*(maxLinksDel-minLinksDel) ));
		            for(int i=0; i<linksToDel; i++){
		            	if( DLMutation(neuralNet, -1) )
		            		mutated=true;
		            	else {
		            		//System.out.println("fallo");
		            		i=linksToDel;
		            	}
		            }
            	}
            	//System.out.println(linksToDel + " enlaces borrados");
	            //System.out.println(neuralNet);
            }
            
			//Bind two neurons (UN Mutation)
            if((initial && randgen.raw()<temper) || (!initial && mutation==5)){

            	//System.out.println(" unir neuronas");
            	if( UNMutation(neuralNet) )
                    mutated=true;
            	//else
            		//System.out.println("fallo");
            	
            	//System.out.println(neuralNet);
            }
            
            //Sets non initial phases
            if(!mutated){
                initial = false;
                mutation = randgen.choose(1, 6);
            }
            
        }
        //System.out.println("Despues --> " + neuralNet);
        nnind.setFitness(null);
        nnind.getGenotype().keepRelevantLinks(significativeWeigth);
        sonsBuffer.add(nnind);
    }

    
	/////////////////////////////////////////////////////////////////
	// ---------------------------- Implementing IConfigure interface
	/////////////////////////////////////////////////////////////////
	
	/**
	 * <p>
	 * Configuration parameters for StructuralMutator are:
	 * </p>
	 * @param settings Settings to configure
	 * <ul>
	 * <li>
	 * <code>temperature-exponent[@value] double (default=1)</code></p>
	 * Temperature exponent to be used for obtaining temperature
	 * of each indivual mutated.
	 * </li>
	 * <li>
	 * <code>significative-weigth[@value] double (default=0.0000001)</code></p>
	 * Minimum value of new weigths.
	 * </li>
	 * <li>
	 * <code>neurons-ranges: complex</code></p> 
	 * Ranges of neurons added or deleted.
	 * <ul>
	 * 		<li>
	 * 		<code>neurons-ranges.added: complex</code>
	 * 		Ranges of neurons added.
	 * 		<ul>
	 * 			<li>
	 * 			<code>neurons-ranges.added[@min] int (default=1)</code>
	 * 			Minimum number of added neurons.
	 * 			</li>
	 * 			<li>
	 * 			<code>neurons-ranges.added[@max] int (default=2)</code>
	 * 			Maximum number of added neurons.
	 * 			</li>
	 * 		</ul> 
	 * 		</li>
	 * 		<li>
	 * 		<code>neurons-ranges.deleted: complex</code>
	 * 		Ranges of neurons deleted.
	 * 		<ul>
	 * 			<li>
	 * 			<code>neurons-ranges.deleted[@min] int (default=1)</code>
	 * 			Minimum number of deleted neurons.
	 * 			</li>
	 * 			<li>
	 * 			<code>neurons-ranges.deleted[@max] int (default=2)</code>
	 * 			Maximum number of deleted neurons.
	 * 			</li>
	 * 		</ul> 
	 * 		</li>
	 * </ul> 
	 * </li>
	 * <li>
	 * <code>links-ranges: complex</code></p> 
	 * Ranges of links added or deleted.
	 * <ul>
	 * 		<li>
	 * 		<code>links-ranges[@relative] boolean (default=false)</code>
	 *      If we use a relative number of links, then we have to specify
	 *      a percentage of links added or deleted, dependind of the layer
	 *      operated�
	 * 		</li>
	 * 		<li>
	 * 		<code>links-ranges.added: complex</code>
	 * 		Ranges of absolute number of links added 
	 *      (when <code>links-ranges.relative = false </code>).
	 * 		<ul>
	 * 			<li>
	 * 			<code>links-ranges.added[@min] int (default=1)</code>
	 * 			Minimum number of added links.
	 * 			</li>
	 * 			<li>
	 * 			<code>links-ranges.added[@max] int (default=6)</code>
	 * 			Maximum number of added links.
	 * 			</li>
	 * 		</ul> 
	 * 		</li>
	 * 		<li>
	 * 		<code>links-ranges.deleted: complex</code>
	 * 		Ranges of absolute number of links deleted 
	 *      (when <code>links-ranges.relative = false </code>).
	 * 		<ul>
	 * 			<li>
	 * 			<code>links-ranges.deleted[@min] int (default=1)</code>
	 * 			Minimum number of deleted links.
	 * 			</li>
	 * 			<li>
	 * 			<code>links-ranges.deleted[@max] int (default=6)</code>
	 * 			Maximum number of deleted links.
	 * 			</li>
	 * 		</ul> 
	 * 		</li>
	 * 		<li> 
	 * 			<code>links-ranges.percentages: complex</code>
	 * 			Percentages of links added and deleted
	 *      	(when <code>links-ranges.relative = true </code>).
	 * 			<ul>
	 * 				<li>
	 *	 			<code>links-ranges.percentages[@hidden] int (default=30)</code>
	 * 				Percentage of links added/deleted of each neuron of a hidden layer.
	 * 				</li>
	 * 				<li>
	 * 				<code>links-ranges.percentages[@output] int (default=5)</code>
	 * 				Percentage of links added/deleted of each neuron of an output layer.
	 * 				</li>
	 * 			</ul> 
	 * 		</li>
	 * </ul> 
	 * </li>
	 * </ul>
	 */
	
	public void configure(Configuration settings){
		
		// Setup pr
		temperExponent = settings.getDouble("temperature-exponent[@value]", 1);
		
		// Setup significativeWeigth
		significativeWeigth = settings.getDouble("significative-weigth[@value]", 0.0000001);
		
		// Setup neurons mutation parameters
		minNeuronsAdd = settings.getInt("neurons-ranges.added[@min]", 1);
		maxNeuronsAdd = settings.getInt("neurons-ranges.added[@max]", 2);
		minNeuronsDel = settings.getInt("neurons-ranges.deleted[@min]", 1);
		maxNeuronsDel = settings.getInt("neurons-ranges.deleted[@max]", 2);
		
		// Setup links mutation parameters
		nOfLinksRelative = settings.getBoolean("links-ranges[@relative]", false);
		if(!nOfLinksRelative){
			minLinksAdd = settings.getInt("links-ranges.added[@min]", 1);
			maxLinksAdd = settings.getInt("links-ranges.added[@max]", 6);
			minLinksDel = settings.getInt("links-ranges.deleted[@min]", 1);
			maxLinksDel = settings.getInt("links-ranges.deleted[@max]", 6);
		}
		else{
			hiddenLinksPercentage = settings.getInt("links-ranges.percentages[@hidden]", 30);
			outputLinksPercentage = settings.getInt("links-ranges.percentages[@output]", 5);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ---------------------------------------------- Private Methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * <p>
	 * Adds neuron structural mutator obtaining the instant 
	 * according to the type of the neuron
	 * </p>
	 * @param neuron Neuron that is going to be mutated, needed to know the type of neuron
	 * 
	 * @return INeuronStructuralMutator Specific neuron structural mutator
	 */
    
	private INeuronStructuralMutator addNeuronStructuralMutator(LinkedNeuron neuron) {
		
		// If it is a Sigmoidal neuron
		if(neuron instanceof SigmNeuron) {
			SigmNeuronStructuralMutator neuronStructuralMutator = new SigmNeuronStructuralMutator();
			// Set the random generator and the significate weight value
			neuronStructuralMutator.setRandgen(randgen);
			neuronStructuralMutator.setSignificativeWeigth(significativeWeigth);
			neuronStructuralMutators.put(neuron.getClass().getCanonicalName(), neuronStructuralMutator);
			return neuronStructuralMutator;
		}
		// If it is a Linear neuron
		else if(neuron instanceof LinearNeuron) {
			LinearNeuronStructuralMutator neuronStructuralMutator = new LinearNeuronStructuralMutator();
			// Set the random generator and the significate weight value
			neuronStructuralMutator.setRandgen(randgen);
			neuronStructuralMutator.setSignificativeWeigth(significativeWeigth);
			neuronStructuralMutators.put(neuron.getClass().getCanonicalName(), neuronStructuralMutator);
			return neuronStructuralMutator;
		}
		// If it is a product unit neuron
		else if(neuron instanceof ExpNeuron) {
			ExpNeuronStructuralMutator neuronStructuralMutator = new ExpNeuronStructuralMutator();
			// Set the random generator and the significate weight value
			neuronStructuralMutator.setRandgen(randgen);
			neuronStructuralMutator.setSignificativeWeigth(significativeWeigth);
			neuronStructuralMutators.put(neuron.getClass().getCanonicalName(), neuronStructuralMutator);
			return neuronStructuralMutator;
		}
		
		return null;
	}
	
	/**
	 * <p>
	 * Adds a neuron to the neural net (AN Mutation)
	 * </p>
	 * @param neuralNet Neural net to be mutated
	 * 
	 * @return true if the mutation have been done
	 */
	
	@SuppressWarnings("unchecked")
	private boolean ANMutation(INeuralNet neuralNet)
	{
		//System.out.println(neuralNet.getNofhneurons());
	    //If the neural net is full we cannot do the mutation
	    if(neuralNet.neuronsFull())
	        return false;
	    
	    //Select a layer to add the new neuron
	    int noflayer = 0;
	    if(neuralNet.getNofhlayers()>1){
		    do{
		    	noflayer = randgen.choose(0, neuralNet.getNofhlayers());
		    }while(neuralNet.getHlayer(noflayer).neuronsFull());
	    }

	    //Selected layer
	    LinkedLayer layer = neuralNet.getHlayer(noflayer);
	    
	    //Previous layer
	    ILayer<? extends INeuron> previousLayer;
	    if(noflayer==0)
	        previousLayer = neuralNet.getInputLayer();
	    else
	        previousLayer = neuralNet.getHlayer(noflayer-1);

        //Next layer
        LinkedLayer nextLayer;
        if(noflayer == neuralNet.getNofhlayers()-1)
            nextLayer = neuralNet.getOutputLayer();
        else
            nextLayer = neuralNet.getHlayer(noflayer+1);
	    
        //Obtain a new neuron (depends on the specific kind of layer)
        LinkedNeuron newNeuron = layer.obtainNewNeuron();
        
        // Set the weight range to the neuron        
		Interval weightRange = species.getHiddenLayerWeightRange(noflayer, 0);
		newNeuron.setWeightRange(weightRange);
        
    	// Obtain the Structural mutation of adding a specific neuron
    	INeuronStructuralMutator neuronStructuralMutator = neuronStructuralMutators.get( newNeuron.getClass().getCanonicalName() );
        
    	// If specific mutator is null add the neuron structural mutation
    	if(neuronStructuralMutator==null)
    		neuronStructuralMutator = addNeuronStructuralMutator(newNeuron);
        
    	if(neuronStructuralMutator!=null) {
        	// Performe the structural mutation of the specific neuron
    		neuronStructuralMutator.addNeuron(newNeuron, layer, previousLayer, nextLayer);
        
	        //Neuron succesfully added
		    return true;
    	}
    	else
    		return false;
	}
	
	/**
	 * <p>
	 * Removes a neuron of the neural net (DN Mutation)
	 * </p>
	 * @param neuralNet Neural net to be mutated
	 * 
	 * @return true if the mutation have been done
	 */
	
	private boolean DNMutation(INeuralNet neuralNet)
	{
		//If the neural net has no neurons we cannot do the mutation
	    if(neuralNet.neuronsEmpty())
	        return false;
	    
		//Select a layer to remove a neuron
	    int noflayer = 0;
	    if(neuralNet.getNofhlayers()>1){
			do{
				noflayer = randgen.choose(0, neuralNet.getNofhlayers());
			}while(neuralNet.getHlayer(noflayer).neuronsEmpty());
	    }
		
	    //Selected layer
	    LinkedLayer layer = neuralNet.getHlayer(noflayer);
	    
		//Avoid removing the last neuron of a layer
		if(layer.getNofneurons()==layer.getMinnofneurons())
		    return false;
	    
		//Selected neuron
	    int selectedNeuron = randgen.choose(0, layer.getNofneurons());
		
        //Next layer
        LinkedLayer nextLayer;
        if(noflayer == neuralNet.getNofhlayers()-1)
            nextLayer = neuralNet.getOutputLayer();
        else
            nextLayer = neuralNet.getHlayer(noflayer+1);
	    
	    //Avoid removing the unique link neuron of a next layer neuron
	    for(int i=0; i<nextLayer.getNofneurons(); i++){
	    	LinkedNeuron nextNeuron = nextLayer.getNeuron(i);
	    	Link[] nextLinks = nextNeuron.getLinks();	    			
	    	if(!nextLinks[selectedNeuron].isBroken())
	    		if((!nextNeuron.isBiased() && nextNeuron.getNoflinks()<=1) || 
	    		   ( nextNeuron.isBiased() && nextNeuron.getNoflinks()<=2))
	    			return false;
	    }
	    
    	// Obtain the Structural mutation of remove a specific neuron
    	INeuronStructuralMutator neuronStructuralMutator = neuronStructuralMutators.get( layer.getNeuron(selectedNeuron).getClass().getCanonicalName() );
	    
	    // If specific mutator is null add the neuron structural mutation
    	if(neuronStructuralMutator==null)
    		neuronStructuralMutator = addNeuronStructuralMutator( layer.getNeuron(selectedNeuron) );
	    
    	if(neuronStructuralMutator!=null) {
        	// Performe the structural mutation of the specific neuron
    		neuronStructuralMutator.removeNeuron(layer, nextLayer, selectedNeuron);

	        //Neuron succesfully removed
	        return true;
    	}
    	else
    		return false;
	}
	
	/**
	 * <p>
	 * Adds a link to the neural net (AL Mutation)
	 * </p>
	 * @param neuralNet Neural net to be mutated
	 * @param selectedLayer Number of selected layer (if it is equal to -1,
	 *                                               we use a random layer)
	 * 
	 * @return true if the mutation have been done
	 */
	
	@SuppressWarnings("unchecked")
	private boolean ALMutation(INeuralNet neuralNet, int selectedLayer)
	{		
		/*//If the neural net has no space for new links
		//we cannot do the mutation
	    if(neuralNet.linksFull())
	        return false;*/

		// Select a layer to add a link
		int noflayer=0;
		if(selectedLayer!=-1)
			noflayer = selectedLayer;
		else{
			if(neuralNet.getOutputLayer().getNofneurons()==1)
				noflayer = randgen.choose(0, neuralNet.getNofhlayers());
			else
				noflayer = randgen.choose(0, neuralNet.getNofhlayers()+1);
		}

		// Previous layer
		ILayer<? extends INeuron> previousLayer = null;
		if(noflayer!=0)
			previousLayer = neuralNet.getHlayer(noflayer-1);
		else
			previousLayer = neuralNet.getInputLayer();
		
	    //Selected layer
	    LinkedLayer layer;
		if(noflayer!=neuralNet.getNofhlayers())
			layer = neuralNet.getHlayer(noflayer);
		else
			layer = neuralNet.getOutputLayer();
	    
	    //Select a neuron of the layer
	    int selectedNeuron = randgen.choose(0, layer.getNofneurons());
	    
	    //Select a neuron of the previous layer
	    int selectedOrigin = randgen.choose(0, previousLayer.getNofneurons());
	    
	    // Neuron selected
	    LinkedNeuron neuron = layer.getNeuron(selectedNeuron);
	    
    	// Obtain the Structural mutation of adding a link of a specific neuron
    	INeuronStructuralMutator neuronStructuralMutator = neuronStructuralMutators.get( neuron.getClass().getCanonicalName() );
	    
	    // If specific mutator is null add the neuron structural mutation
    	if(neuronStructuralMutator==null)
    		neuronStructuralMutator = addNeuronStructuralMutator(neuron);
	    
    	if(neuronStructuralMutator!=null)
        	// Performe the structural mutation of the specific neuron
    		return neuronStructuralMutator.addLink(neuron, layer, previousLayer, selectedNeuron, selectedOrigin);
    	else 
    		return false;
	}
	
	/**
	 * <p>
	 * Removes a link of the neural net (DL Mutation)
	 * </p>
	 * @param neuralNet Neural net to be mutated
	 * @param selectedLayer Number of selected layer (if it is equal to -1,
	 *                                               we use a random layer)
	 * 
	 * @return true if the mutation have been done
	 */
	
	@SuppressWarnings("unchecked")
	private boolean DLMutation(INeuralNet neuralNet, int selectedLayer)
	{
		// If the neural net has no links we cannot do the mutation
	    /*if(neuralNet.linksEmpty())
	        return false;*/
	    
		// Select a layer to add a link
		int noflayer=0;
		if(selectedLayer!=-1)
			noflayer = selectedLayer;
		else{
			if(neuralNet.getOutputLayer().getNofneurons()==1)
				noflayer = randgen.choose(0, neuralNet.getNofhlayers());
			else
				noflayer = randgen.choose(0, neuralNet.getNofhlayers()+1);				
		}

	    // Selected layer
	    LinkedLayer layer;
		if(noflayer!=neuralNet.getNofhlayers())
			layer = neuralNet.getHlayer(noflayer);
		else
			layer = neuralNet.getOutputLayer();
	    
	    // Select a neuron of the layer
	    int selectedNeuron = randgen.choose(0, layer.getNofneurons());
	    LinkedNeuron neuron = layer.getNeuron(selectedNeuron);
	    
		// Previous layer
		ILayer<? extends INeuron> previousLayer = null;
		if(noflayer!=0)
			previousLayer = neuralNet.getHlayer(noflayer-1);
		else
			previousLayer = neuralNet.getInputLayer();
		
	    // Select a neuron of the previous layer
	    int selectedOrigin = randgen.choose(0, previousLayer.getNofneurons());
	    
		// Avoid removing the unique link of a neuron
		if((!neuron.isBiased() && neuron.getNoflinks()<=1) || (neuron.isBiased() && neuron.getNoflinks()<=2))
		    return false;
	    
	    // If previous layer is not input layer
	    if(noflayer!=0) {
	    	// Avoid removing the unique output link of a hidden node
			int outputLinks=0;
			for(int i=0; i<layer.getNofneurons(); i++){
				LinkedNeuron layerNeuron = layer.getNeuron(i);
				if(!layerNeuron.getLinks()[selectedOrigin].isBroken())
					outputLinks++;
			}
			if(outputLinks==1)
				return false;
	    }
	    
    	// Obtain the Structural mutation of removing a link of specific neuron
    	INeuronStructuralMutator neuronStructuralMutator = neuronStructuralMutators.get( neuron.getClass().getCanonicalName() );
	    
	    // If specific mutator is null add the neuron structural mutation
    	if(neuronStructuralMutator==null)
    		neuronStructuralMutator = addNeuronStructuralMutator(neuron);
	    
    	if(neuronStructuralMutator!=null)
        	// Performe the structural mutation of the specific neuron
    		return neuronStructuralMutator.removeLink(neuron, selectedOrigin);
    	else
    		return false;
	}
	
	/**
	 * <p>
	 * Units two neurons of a layer in the neural net (UN Mutation)
	 * </p>
	 * @param neuralNet Neural net to be mutated
	 * 
	 * @return true if the mutation have been done
	 */
	
	@SuppressWarnings("unchecked")
	private boolean UNMutation(INeuralNet neuralNet)
	{
		// If the neural net has no neurons enough we cannot do the mutation
	    if(neuralNet.getNofhneurons()<2)
	        return false;
	    
		// Select a layer to unit the neurons
		int noflayer = 0;
	    if(neuralNet.getNofhlayers()>1){
			for(int i=0; i<neuralNet.getNofhlayers() && 
				neuralNet.getHlayer(noflayer).getNofneurons() == neuralNet.getHlayer(noflayer).getMinnofneurons(); i++)
				noflayer =i;
	    }
	    else{
	    	if(neuralNet.getHlayer(noflayer).getNofneurons() == neuralNet.getHlayer(noflayer).getMinnofneurons())
	    		return false;
	    }
		
	    // Selected layer
	    LinkedLayer layer = neuralNet.getHlayer(noflayer);
	    
	    // Avoid that if there are not two neurons of same type the
	    // unit neuron mutation is not performed
	    boolean exit=true;
	    List<Class> neuronTypes = new ArrayList<Class>();
	    for(int i=0; i<layer.getNofneurons(); i++) {
	    	Class neuronType = layer.getNeuron(i).getClass();
	    	
	    	// If a neuron type is alredy contained in the list, that means that exists 
	    	// two neurons with the same type, so unit neuron mutation can be performed
	    	if(neuronTypes.contains(neuronType)) {
	    		exit=false;
	    		i=layer.getNofneurons(); // To exit loop
	    	}
	    	else
	    		neuronTypes.add(neuronType);
	    }
	    
	    if(exit)
	    	return false;
	    
	    // Select a neuron of the layer
	    int firstNeuron;
	    int secondNeuron;
	    do{
	        firstNeuron = randgen.choose(0, layer.getNofneurons());
	        secondNeuron = randgen.choose(0, layer.getNofneurons());
	    }while( firstNeuron==secondNeuron ||
	    		!layer.getNeuron(firstNeuron).getClass().equals(layer.getNeuron(secondNeuron).getClass()) );
	    
        // Next layer
        LinkedLayer nextLayer;
        if(noflayer == neuralNet.getNofhlayers()-1)
            nextLayer = neuralNet.getOutputLayer();
        else
            nextLayer = neuralNet.getHlayer(noflayer+1);

        // Obtain the neuron to unit
        LinkedNeuron firstLinkedNeuron = layer.getNeuron(firstNeuron);
        LinkedNeuron secondLinkedNeuron = layer.getNeuron(secondNeuron);
        
    	// Obtain the Structural mutation of uniting two specific neuron
    	INeuronStructuralMutator neuronStructuralMutator = neuronStructuralMutators.get( firstLinkedNeuron.getClass().getCanonicalName() );
        
	    // If specific mutator is null add the neuron structural mutation
    	if(neuronStructuralMutator==null)
    		neuronStructuralMutator = addNeuronStructuralMutator(firstLinkedNeuron);
        
    	if(neuronStructuralMutator!=null) {
        	// Performe the structural mutation of the specific neuron
	        neuronStructuralMutator.unitNeuronsWeights(firstLinkedNeuron, secondLinkedNeuron, layer, nextLayer, firstNeuron, secondNeuron);
		    	    
		    // Remove second neuron
		    neuronStructuralMutator.removeNeuron(layer, nextLayer, secondNeuron);
		    
			// Neurons succesfully united
			return true;
		}
    	else
    		return false;
	}
	
}


