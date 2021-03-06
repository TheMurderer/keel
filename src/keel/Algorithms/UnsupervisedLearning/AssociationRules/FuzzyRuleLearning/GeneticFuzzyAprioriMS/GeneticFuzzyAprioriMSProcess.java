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

package keel.Algorithms.UnsupervisedLearning.AssociationRules.FuzzyRuleLearning.GeneticFuzzyAprioriMS;

/**
 * <p>
 * @author Written by Alvaro Lopez
 * @version 1.0
 * @since JDK1.6
 * </p>
 */

import java.util.*;
import org.core.Randomize;
import keel.Algorithms.Shared.Parsing.*;

public class GeneticFuzzyAprioriMSProcess {
  /**
   * <p>
   * It provides the implementation of the algorithm to be run in a process
   * </p>
   */

  private int k;
  private double intervalThreshold;
  private double breakThreshold;
  private double pRNL;
  private int nEvaluations;
  private int popSize;
  private double pm;
  private double pc;
  private double d;
  private double maximumMinimumSupport;
  private boolean useMaxForOneFrequentItemsets;
  private double minConfidence;
  private myDataset dataset;
  
  private int nEval;
  private int nGenerations;
  private int evaluationStep;
  private String geneticLearningLog;
  private ArrayList<Integer> idOfAttributes;
  
  private ArrayList<FuzzyAttribute> fuzzyAttributes;
  private int countOneFrequentItemsets;
  private int countFrequentItemsets;
  private ArrayList<AssociationRule> associationRulesSet;
  private boolean[] coveredRecords;
  MinimumSupport[] minimum_supports;
  static Randomize rand;
  
  /**
   * <p>
   * It creates a new process for the algorithm by setting up its parameters
   * </p>
   * @param dataset The instance of the dataset for dealing with its records
   * @param k A parameter k for k-means clustering (number of clusters)
   * @param intervalThreshold A threshold to remove intervals.
   * @param breakThreshold A threshold to get the cluster break points.
   * @param pRNL The percentage of the required number of large 1-itemsets
   * @param nEvaluations The maximum number of evaluations to accomplish before terminating the genetic learning
   * @param popSize The maximum size of population to handle after each generation
   * @param pm The probability of the mutation operator
   * @param pc The probability of the crossover operator
   * @param d The parameter which is used while executing the crossover operator
   * @param useMaxForOneFrequentItemsets It indicates whether the max operator must be used while discovering 1-Frequent Itemsets
   * @param maximumMinimumSupport The user-specified maximum minimum support to generate minimum supports for each item (it will be considered if it is different from -1)
   * @param minConfidence The user-specified minimum confidence for the mined association rules
   */
  public GeneticFuzzyAprioriMSProcess(int k,double breakThreshold,double intervalThreshold,double pRNL,myDataset dataset, int nEvaluations, int popSize, double pm, double pc, double d, boolean useMaxForOneFrequentItemsets, double maximumMinimumSupport, double minConfidence) {
          this.k = k;
          this.breakThreshold = breakThreshold;
          this.pRNL = pRNL;
          this.nEvaluations = nEvaluations;
	  this.popSize = popSize;
	  this.pm = pm;
	  this.pc = pc;
	  this.d = d;
	  //this.nFuzzyRegionsForNumericAttributes = nFuzzyRegionsForNumericAttributes;
	  this.useMaxForOneFrequentItemsets = useMaxForOneFrequentItemsets;
	  this.minConfidence = minConfidence;
          this.maximumMinimumSupport = maximumMinimumSupport;
	  this.dataset = dataset;
          this.intervalThreshold = intervalThreshold * this.dataset.getnTrans();

	  this.nEval = 0;
	  this.nGenerations = 0;
	  this.evaluationStep = (int) Math.ceil(nEvaluations * 0.05);
	  this.idOfAttributes = dataset.getIDsOfNumericAttributes();
          if (this.idOfAttributes.size() == 0){
              this.idOfAttributes = dataset.getIDsOfNominalAttributes();
          }
	  
      this.countOneFrequentItemsets = 0;
      this.countFrequentItemsets = 0;
      this.associationRulesSet = new ArrayList<AssociationRule>();
      this.minimum_supports = new MinimumSupport[ this.idOfAttributes.size() ];

	  this.coveredRecords = new boolean[ dataset.getnTrans() ];
	  for (int i=0; i < this.coveredRecords.length; i++)
		  this.coveredRecords[i] = false;
  }
  
  /**
   * <p>
   * It runs the algorithm for mining association rules
   * </p>
   */
  public void run() {
	  this.fuzzyAttributes = this.runGeneticAlgorithm();
	  
	  if (this.fuzzyAttributes == null) this.fuzzyAttributes = new ArrayList<FuzzyAttribute>();
	  
	  this.addNominalFuzzyAttributes(this.fuzzyAttributes);
	  
	  /*for (int i=0; i < fuzzyAttributes.size(); i++)
	  System.out.println("ID Fuzzy Attribute #" + this.fuzzyAttributes.get(i).getIdAttr() + ":\n" + this.fuzzyAttributes.get(i) + "\n");*/
	  
	  this.runFuzzyApriori( new FuzzyDataset(this.dataset, this.fuzzyAttributes) );
  }
  
  /**
   * <p>
   * It returns a rules set once the algorithm has been carried out
   * </p>
   * @return An array of association rules having both minimum confidence and support
   */
  public ArrayList<AssociationRule> getRulesSet() {
	  return this.associationRulesSet;
  }
  
  /**
   * <p>
   * It prints out on screen relevant information regarding the mined association rules
   * </p>
   * @param rules The array of association rules from which gathering relevant information
   */
  public void printReport(ArrayList<AssociationRule> rules) {
	  int r;
	  double avg_sup = 0.0, avg_conf = 0.0, avg_ant_length = 0.0, avg_interest = 0.0;
	  AssociationRule ar;

	  for (r=0; r < rules.size(); r++) {
		  ar = rules.get(r);

		  avg_sup += ar.getRuleSupport();
		  avg_conf += ar.getConfidence();
		  avg_ant_length += ar.getAntecedent().size();
                  avg_interest += ar.getInterestingness();
	  }

	  System.out.println("\nNumber of Frequent Itemsets found: " + this.countFrequentItemsets);
	  System.out.println("Number of Association Rules generated: " + rules.size());

	  if (! rules.isEmpty()) {
		  System.out.println("Average Support: " + ( avg_sup / rules.size() ));
		  System.out.println("Average Confidence: " + ( avg_conf / rules.size() ));
		  System.out.println("Average Antecedents Length: " + ( avg_ant_length / rules.size() ));
		  System.out.println("Number of Covered Records (%): " + ( (100.0 * this.countCoveredRecords()) / this.dataset.getnTrans()));
                  System.out.println("Average Interestingness: " + ( avg_interest / rules.size() ));
	  }
  }
  
  /**
   * <p>
   * It returns the number of 1-Frequent Itemsets
   * </p>
   * @return A value representing the number of 1-Frequent Itemsets
   */
  public int getNumberOfOneFrequentItemsets() {
	  return this.countOneFrequentItemsets;
  }
  
  /**
   * <p>
   * It returns the XML string representing the genetic learning log
   * </p>
   * @return A string containing the genetic learning text
   */
  public String getGeneticLearningLog() {
	  return this.geneticLearningLog;
  }
  
  /**
   * <p>
   * It returns the mined fuzzy attributes once the genetic learning has been accomplished
   * </p>
   * @return An array representing the mined fuzzy attributes
   */
  public ArrayList<FuzzyAttribute> getFuzzyAttributes() {
	  return this.fuzzyAttributes;
  }
  
  private void addNominalFuzzyAttributes(ArrayList<FuzzyAttribute> fuzzy_attributes) {
	  int attr, id_attr, id_region;
	  FuzzyRegion[] fuzzy_regions;
	  ArrayList<Integer> id_of_nominal_attributes;
	  
	  id_of_nominal_attributes = this.dataset.getIDsOfNominalAttributes();
	  
	  for (attr=0; attr < id_of_nominal_attributes.size(); attr++) {
		  id_attr = id_of_nominal_attributes.get(attr);
		  fuzzy_regions = new FuzzyRegion[((int) this.dataset.getMax(id_attr)) + 1];
		  
		  for (id_region=0; id_region < fuzzy_regions.length; id_region++) {
			  fuzzy_regions[id_region] = new FuzzyRegion();
			  
			  fuzzy_regions[id_region].setX0(this.dataset.getMin(id_attr) + id_region - 1);
			  fuzzy_regions[id_region].setX1(this.dataset.getMin(id_attr) + id_region);
			  fuzzy_regions[id_region].setX3(this.dataset.getMin(id_attr) + id_region + 1);
			  
			  fuzzy_regions[id_region].setY(1.0);
			  fuzzy_regions[id_region].setLabel(this.dataset.getNominalValue(id_attr, id_region));
		  }
		  
		  fuzzy_attributes.add( new FuzzyAttribute(id_attr, fuzzy_regions) );
	  }
	  
  }
  
  private ArrayList<FuzzyAttribute> runGeneticAlgorithm() {
	  ArrayList<FuzzyAttribute> best_fuzzy_attrs = null;
	  ArrayList<Chromosome> pop;
	  Chromosome individual;
          Gene[] genes;
          int g;

	  this.geneticLearningLog = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	  this.geneticLearningLog += "<genetic_learning>\n";
	  
	  if (! this.idOfAttributes.isEmpty()) {
		  
		  System.out.print("Initialing population... ");
		  
		  pop = this.initializePopulation();
		  
		  System.out.println("done [Evaluations: " + this.nEval + "].");
		    
		  while (this.nEval < this.nEvaluations) {
			  this.nGenerations++;
			  System.out.print("Computing Generation #" + this.nGenerations + "... ");
			  
			  this.crossover(pop);
			  this.mutate(pop);
                          individual = selectIndividual(pop);

                          genes = individual.getGenes();
                          for (g=0; g < genes.length; g++) {
                            this.minimum_supports[g].setMS(genes[g].getMinimumSupport().getMS());
                          }
                          best_fuzzy_attrs = this.select(pop);
                          System.out.println("done [Evaluations: " + this.nEval + "].");
		  }
		  
		  /*for (int i=0; i < pop.size(); i++)
		  	System.out.println("Chromosome #" + (i+1) + ":\n" + pop.get(i) + "\n");*/
	  }
	  
	  this.geneticLearningLog += "</genetic_learning>";
	  
	  return best_fuzzy_attrs;
  }
  
  private ArrayList<Chromosome> initializePopulation() {
	  int p, g, m, contGen;
	  MembershipFunction[] membership_functions;
	  Gene[] genes;
	  Chromosome chr;
	  ArrayList<Chromosome> popInit;
          Set<Double> QuantitativeValues;
         
          Double [] auxQValues;
          double [] QValues;
          double [][] QValuesCluster;
          int [] numQValuesCluster;

          int trans, attr, i,j,id_attr,iterQvPrev;
          double[][][] fuzzyTransactions = null;
          double[][] true_transactions = null, dataClusters;
          int []contElemXCluster;
          //MinimumSupport[] minimum_supports;
	  double []quantity;
          int [] AN; // array with the appearing number for each item
          double [] AQV; // array with the average quantitative value for each item
          double [] SV; // array with the support value for each item
          int iter,elemCluster,aux;
          int [][]Clusters;
          double []auxArray;
          int [][] Qvg,auxQvg; //represents the appearing number of the quantitative value v for the gth cluster
          int [][] breakPoints;
          double [][] probV,auxProbV,acumulativeProb; // represents the appearing probability of the quantitative value v in its corresponding interval
          int [] regiones;
          int iterQv;
          int bp;
          int contqv;
          KMeans Kclusters;

	  popInit = new ArrayList<Chromosome>();

	  for (p=0; p < this.popSize; p++) {
                  genes = new Gene[ this.idOfAttributes.size() ];
                  quantity = new double[this.idOfAttributes.size()];
                  AN = new int[this.idOfAttributes.size()];
                  AQV = new double[this.idOfAttributes.size()];
                  SV = new double[this.idOfAttributes.size()];
                  regiones = new int [this.k];
                  QuantitativeValues = new HashSet<Double>(this.dataset.getnTrans()*this.idOfAttributes.size());
                  numQValuesCluster = new int [this.k];
                     
                  //initializing arrays
                  for(iter=0; iter<this.idOfAttributes.size(); iter++){
                      quantity[iter]=0.0;
                      AN[iter]=0;
                      AQV[iter]=0.0;
                      SV[iter]=0.0;
                  }

                  fuzzyTransactions = new double[ this.dataset.getnTrans() ][ this.idOfAttributes.size() ][];
                  true_transactions = this.dataset.getTrueTransactions();
                  //Calculate de average quantitative value AQVj and de support value SVj for each item from given transactions
                 for (trans=0; trans < fuzzyTransactions.length; trans++) {
                    for (attr=0; attr < fuzzyTransactions[trans].length; attr++) {
                        id_attr = this.idOfAttributes.get(attr);
                        QuantitativeValues.add(true_transactions[trans][id_attr]);
                        AN[id_attr]++;
                        quantity[id_attr] += true_transactions[trans][id_attr];
                        
                    }
                 }

                 for (attr=0; attr < this.idOfAttributes.size(); attr++){
                    id_attr = this.idOfAttributes.get(attr);
                    AQV[id_attr] = quantity[id_attr]/(AN[id_attr] * 1.0);
                    SV[id_attr] = (AN[id_attr] * 1.0)/this.dataset.getnTrans();
                 }

                  //Divide the items into k clusters by the k-means clustering approach based on AQV and SV
                  auxArray = new double[this.idOfAttributes.size()];
                  for(attr=0; attr<this.idOfAttributes.size(); attr++){
                      auxArray[attr] = AQV[attr];
                  }
                  
                  Arrays.sort(auxArray);

                  if (this.k > this.idOfAttributes.size()){
                      this.k = this.idOfAttributes.size();
                  }
                
                Clusters = new int[this.k][this.idOfAttributes.size()];

                contElemXCluster = new int[this.k];
                for (int ccl=0; ccl<this.k; ccl++){
                    contElemXCluster[ccl] = 0;
                }

                dataClusters = new double[this.idOfAttributes.size()][1];
                for(int ccl=0; ccl<this.idOfAttributes.size(); ccl++){
                    dataClusters[ccl][0]= AQV[ccl];
                }
                rand.setSeed(1);
                Kclusters = new KMeans(dataClusters,this.k,rand);
                for (int ccl=0;ccl<dataClusters.length;ccl++) {
                    int idcluster=Kclusters.nearestCentroid(dataClusters[ccl]);
                    Clusters[idcluster][contElemXCluster[idcluster]]= ccl;
                    contElemXCluster[idcluster]++;
                }

                  //For each cluster, find the distribution of the quantitative values in the transactions, in order to get break points
                   auxQvg = new int [this.k][QuantitativeValues.size()];
                   Qvg = new int [this.k][QuantitativeValues.size()];
                   breakPoints = new int [this.k][QuantitativeValues.size()];
                   QValuesCluster = new double[this.k][QuantitativeValues.size()];
                   for (iter=0; iter<this.k; iter++){
                         for (iterQv=0; iterQv < QuantitativeValues.size(); iterQv++) {
                            auxQvg[iter][iterQv]=0;
                            Qvg[iter][iterQv]=0;
                            breakPoints[iter][iterQv]=1;
                            QValuesCluster[iter][iterQv]=-1;
                         }
                   }
                   
                  auxQValues = new Double[QuantitativeValues.size()];
                  auxQValues = QuantitativeValues.toArray(auxQValues);
                  QValues = new double[QuantitativeValues.size()];
                  for(int l=0; l< auxQValues.length; l++){
                     QValues[l] = auxQValues[l];
                  }
                  Arrays.sort(QValues);
                
                  for (iter=0; iter<this.k; iter++){
                      contqv=0;
                      elemCluster = contElemXCluster[iter];
                      if (elemCluster!=0){
                          for (i=0; i<elemCluster; i++){
                             id_attr = this.idOfAttributes.get(Clusters[iter][i]);
                             for (trans=0; trans < fuzzyTransactions.length; trans++) {
                                 for (iterQv=0; iterQv < QuantitativeValues.size(); iterQv++) {
                                     if (true_transactions[trans][id_attr] == QValues[iterQv]){
                                        if(auxQvg[iter][iterQv]==0){
                                            QValuesCluster[iter][contqv]= QValues[iterQv];
                                            contqv++;
                                            numQValuesCluster[iter]=contqv;
                                        }
                                        auxQvg[iter][iterQv]++;
                                     }
                                 }
                             }
                          }
                      }
                  }

                  for (iter=0; iter<this.k; iter++){
                      elemCluster = contElemXCluster[iter];
                      if (elemCluster!=0){
                          for (i=0; i<elemCluster; i++){
                             id_attr = this.idOfAttributes.get(Clusters[iter][i]);
                             for (trans=0; trans < fuzzyTransactions.length; trans++) {
                                 for (iterQv=0; iterQv<numQValuesCluster[iter]; iterQv++) {
                                     if (true_transactions[trans][id_attr] == QValuesCluster[iter][iterQv]){
                                        Qvg[iter][iterQv]++;
                                     }
                                 }
                             }
                         }
                     }
                  }


                   //if breakPoints[iter][iterQv]==0, it will be consider as a break point
               
                   for (iter=0; iter<this.k; iter++){
                         breakPoints[iter][0]=0;
                         for (iterQv=1; iterQv<numQValuesCluster[iter]-1 ; iterQv++) {
                            if((Qvg[iter][iterQv]*1.0)<= this.breakThreshold){
                               breakPoints[iter][iterQv]=0; //this is a break point
                            }
                         }
                         if((numQValuesCluster[iter]-1)>0){
                            breakPoints[iter][numQValuesCluster[iter]-1]= 0;
                         }
                   }

                  //Generate the first part of a population of P individuals according to the support values of the items
                  //That is, the minimum support of an item in an individual is randomly generated in the range between 0 and its support value
                  for (attr=0; attr < this.idOfAttributes.size(); attr++){
                      id_attr = this.idOfAttributes.get(attr);
                      if(this.maximumMinimumSupport != -1.0){
                            if(SV[id_attr] > this.maximumMinimumSupport){
                                SV[id_attr] = this.maximumMinimumSupport;
                            }
                      }
                      this.minimum_supports[id_attr] = new MinimumSupport();
                      this.minimum_supports[id_attr].setMS(Randomize.RanddoubleClosed(0.0,SV[id_attr]));
                  }


                  //for each cluster, calculate the appearing probability of each quantitative value in its corresponding interval.
                  //restated, it is the appearing number of the quantitative value divided by the appearing number of all the quantitative values in the same interval
                 auxProbV = new double [this.k][QValues.length];
                 probV = new double [this.k][QValues.length];
                 acumulativeProb = new double [this.k][QValues.length];
                 for (iter=0; iter<this.k; iter++){
                     for (iterQv=0; iterQv<numQValuesCluster[iter]; iterQv++) {
                        auxProbV[iter][iterQv]=0.0;
                        probV[iter][iterQv]=0.0;
                        acumulativeProb[iter][iterQv]=0.0;
                     }
                  }

                  for (iter=0; iter<this.k; iter++){
                      bp=0;
                      for (iterQv=0; iterQv<numQValuesCluster[iter]; iterQv++) {
                         if(breakPoints[iter][iterQv]==0){
                             if(auxProbV[iter][bp]<=this.intervalThreshold){
                                if(iterQv!=0 && iterQv!=numQValuesCluster[iter]-1){
                                    breakPoints[iter][iterQv]=1;
                                }
                                auxProbV[iter][bp] += Qvg[iter][iterQv];
                             }else{
                                bp=iterQv;
                             }
                         }else{
                             auxProbV[iter][bp] += Qvg[iter][iterQv];
                         }
                      }
                  }


                  //Get the matrix. Besides if the total quantity in an interval is less than or equal to the interval threshold, the interval is removed
                 
                  bp=0;
                  for (iter=0; iter<this.k; iter++){
                      for (iterQv=0; iterQv<numQValuesCluster[iter]; iterQv++) {
                         if(breakPoints[iter][iterQv]==0){
                             bp = iterQv;
                         }else{
                             auxProbV[iter][iterQv] = auxProbV[iter][bp];
                         }
                      }   
                  }

                  for (iter=0; iter<this.k; iter++){
                      for (iterQv=0; iterQv<numQValuesCluster[iter]; iterQv++) {
                         if(breakPoints[iter][iterQv]==0){
                            auxProbV[iter][iterQv] = 0.0;
                         }
                      }
                  }

                  for (iter=0; iter<this.k; iter++){
                     for (iterQv=0; iterQv<numQValuesCluster[iter]; iterQv++) {
                        if(auxProbV[iter][iterQv]!=0.0){
                            probV[iter][iterQv]=(Qvg[iter][iterQv]*1.0)/auxProbV[iter][iterQv];
                            acumulativeProb[iter][iterQv] = (Qvg[iter][iterQv]*1.0)/auxProbV[iter][iterQv];
                        }else{
                            probV[iter][iterQv]=0.0;
                            acumulativeProb[iter][iterQv] = 0.0;
                        }
                     }
                  }

                  //get membership functions according to the probabilities
                  for (iter=0; iter<this.k; iter++){
                     for (iterQv=0; iterQv<numQValuesCluster[iter]; iterQv++) {
                        if(breakPoints[iter][iterQv]!=0 && iterQv!=0){
                            iterQvPrev = iterQv-1;
                             acumulativeProb[iter][iterQv]+=acumulativeProb[iter][iterQvPrev];
                        }
                     }
                  }

                  for (iter=0; iter<this.k; iter++){
                     regiones[iter]=0;
                  }
                  for (iter=0; iter<this.k; iter++){
                     for (iterQv=0; iterQv<numQValuesCluster[iter]; iterQv++) {
                         if(iterQv!=0 && acumulativeProb[iter][iterQv] != 0.0 && acumulativeProb[iter][iterQv-1] == 0.0){
                             regiones[iter]++;
                         }
                     }
                  }
                  contGen = 0;
                  m=0;

                  
                  for (iter=0; iter<this.k; iter++){
                     if (regiones[iter]==0){
                        regiones[iter]=1;
                     }
                     elemCluster = contElemXCluster[iter];
                     if (elemCluster!=0){
                         for (g=0; g < elemCluster; g++) {
                            id_attr = this.idOfAttributes.get(contGen);
                            membership_functions = new MembershipFunction[regiones[iter]];
                            m=0;
                            iterQv=1;
                            int b=-1;
                            double a = Randomize.RanddoubleClosed(0.0,1.0);
                            while (iterQv < numQValuesCluster[iter]-1 && m < regiones[iter]){
                                 if(acumulativeProb[iter][iterQv] != 0.0 && acumulativeProb[iter][iterQv-1] == 0.0){
                                     b=iterQv;
                                     a = Randomize.RanddoubleClosed(0.0,1.0);
                                 }
                                 if (acumulativeProb[iter][iterQv] != 0.0 || b!=-1 ){
                                     if (acumulativeProb[iter][iterQv]>0.99999) acumulativeProb[iter][iterQv]=1.0;
                                     b=iterQv;
                                     if (a < acumulativeProb[iter][iterQv]){
                                        membership_functions[m] = new MembershipFunction();
                                        membership_functions[m].setC(QValuesCluster[iter][b]);
                                        membership_functions[m].setW( Randomize.RanddoubleClosed(0.0, (this.dataset.getMax(id_attr) - this.dataset.getMin(id_attr)) / 2.0) );
                                        m++;
                                        iterQv++;
                                        b=-1;
                                     }else if(a == acumulativeProb[iter][iterQv]){
                                          b=iterQv;
                                          membership_functions[m] = new MembershipFunction();
                                          membership_functions[m].setC(QValuesCluster[iter][b]);
                                          membership_functions[m].setW( Randomize.RanddoubleClosed(0.0, (this.dataset.getMax(id_attr) - this.dataset.getMin(id_attr)) / 2.0) );
                                          m++;
                                          iterQv++;
                                          b=-1;
                                     }else{
                                          b=iterQv;
                                          if(acumulativeProb[iter][iterQv+1] == 0.0){ // the last of interval
                                                membership_functions[m] = new MembershipFunction();
                                                membership_functions[m].setC(QValuesCluster[iter][b]);
                                                membership_functions[m].setW( Randomize.RanddoubleClosed(0.0, (this.dataset.getMax(id_attr) - this.dataset.getMin(id_attr)) / 2.0) );
                                                m++;
                                                b=-1;
                                           }
                                          iterQv++;
                                     }
                                 }else{
                                    iterQv++;
                                 }
                             }
                             if (m!=0){
                                genes[contGen]= new Gene(this.minimum_supports[contGen],membership_functions);
                                genes[contGen].sortMembershipFunctions();
                                contGen++;
                             }
                             else{
                                membership_functions[0] = new MembershipFunction();
                                membership_functions[0].setC( Randomize.RanddoubleClosed(this.dataset.getMin(id_attr), this.dataset.getMax(id_attr)) );
                                membership_functions[0].setW( Randomize.RanddoubleClosed(0.0, (this.dataset.getMax(id_attr) - this.dataset.getMin(id_attr)) / 2.0) );

                                genes[contGen]= new Gene(this.minimum_supports[contGen],membership_functions);
                                genes[contGen].sortMembershipFunctions();
                                contGen++;
                             }
                          }
                       }
                  }

                  
		  chr = new Chromosome(genes);
		  this.evaluateFitness(chr);
		  
		  popInit.add(chr);
	  }
	  return popInit;
  }
  
  private ArrayList<FuzzyAttribute> select(ArrayList<Chromosome> pop) {
	  Collections.sort(pop);
	  
	  while (pop.size() > this.popSize)
		  pop.remove(this.popSize);
	  
	  return ( this.transformIntoFuzzyAttributes( pop.get(0) ) );
  }

  private Chromosome selectIndividual(ArrayList<Chromosome> pop) {
	  Collections.sort(pop);

	  while (pop.size() > this.popSize)
		  pop.remove(this.popSize);

	  return (pop.get(0));
  }
  
  private void crossover(ArrayList<Chromosome> pop) {
	  int i, j, index_best_chr, aux;
	  double best_fitness, sum_expected_values, rank_min, rank_max, factor, sum, rnd;
	  double[] expected_values;
	  int[] index_mating_pool;
	  Chromosome mom, dad;
	  Chromosome[] offsprings;	  
	  
	  rank_min = 0.75;
	  rank_max = 2.0 - rank_min;
	  factor = (rank_max - rank_min) / (double)(pop.size() - 1);
	  
	  expected_values = new double[ pop.size() ];
	  for (i=0; i < expected_values.length; i++)
		  expected_values[i] = 0.0;
	  
	  sum_expected_values = 0.0;
	  
	  for (i=0; i < pop.size(); i++) {
			index_best_chr = -1;
			best_fitness = 0.0;
			
			for (j=0; j < pop.size(); j++) {
				if ( (expected_values[j] == 0.0) && ( (index_best_chr == -1) || (pop.get(j).getFitness() > best_fitness) ) ) {
					best_fitness = pop.get(j).getFitness();
					index_best_chr = j;
				}
			}
			
			expected_values[index_best_chr] = rank_min + (pop.size() - 1 - i) * factor;
			sum_expected_values += expected_values[index_best_chr];
	  }
	  
	  index_mating_pool = new int[ expected_values.length ];
	  
	  for (i=0; i < index_mating_pool.length; i++) {
		  sum = 0.0;
		  rnd = Randomize.RanddoubleClosed(0.0, sum_expected_values);
		  
		  for (j=0; j < expected_values.length; j++) {
			  sum += expected_values[j];
			  if (sum > rnd) break;
		  }
		  
		  index_mating_pool[i] = j;
	  }
	  
	  for (i=0; i < index_mating_pool.length; i++) {
		  j = Randomize.Randint(i, index_mating_pool.length);
		  aux = index_mating_pool[j];
		  index_mating_pool[j] = index_mating_pool[i];
		  index_mating_pool[i] = aux;
	  }
	  
	  offsprings = new Chromosome[4];
	  
	  for (i=0; i < (index_mating_pool.length / 2); i++) {
		  mom = pop.get( index_mating_pool[2 * i] );
		  dad = pop.get( index_mating_pool[2 * i + 1] );
		  
		  if (Randomize.Rand() < this.pc) {
			  for (j=0; j < offsprings.length; j++) {
				  offsprings[j] = this.mma(j, mom.getGenes(), dad.getGenes());
				  this.evaluateFitness(offsprings[j]);
			  }
			  
			  Arrays.sort(offsprings);
			  
			  pop.add(offsprings[0]);
			  pop.add(offsprings[1]);
		  }
	  }
  }

  /**
  * It implements the max-min-arithmetical crossover operator
  */
  private Chromosome mma(int index, Gene[] mom_genes, Gene[] dad_genes) {
	  int g, m;
	  Gene[] offspring_genes;
	  MembershipFunction[] offspring_mfs, mom_mfs, dad_mfs;
	  MinimumSupport[] offspring_ms, mom_ms, dad_ms;
	  
          offspring_ms = new MinimumSupport[ this.idOfAttributes.size() ];
	  offspring_genes = new Gene[ this.idOfAttributes.size() ];

          mom_ms = new MinimumSupport[ this.idOfAttributes.size() ];
          dad_ms = new MinimumSupport[ this.idOfAttributes.size() ];
	  switch(index) {
	  	case 0:
	  			for (g=0; g < offspring_genes.length; g++) {
                                        offspring_mfs = new MembershipFunction[ mom_genes[g].getMembershipFunctions().length ];

	  				mom_mfs = mom_genes[g].getMembershipFunctions();
	  				dad_mfs = dad_genes[g].getMembershipFunctions();
                                        mom_ms[g] = mom_genes[g].getMinimumSupport();
	  				dad_ms[g] = dad_genes[g].getMinimumSupport();
	  				
	  				for (m=0; m < offspring_mfs.length; m++) {
	  					offspring_mfs[m] = new MembershipFunction();
	  					
	  					offspring_mfs[m].setC(this.d * mom_mfs[m].getC() + (1 - this.d) * dad_mfs[m].getC());
	  					offspring_mfs[m].setW(this.d * mom_mfs[m].getW() + (1 - this.d) * dad_mfs[m].getW());
	  				}

                                        offspring_ms[g] = new MinimumSupport();
                                        offspring_ms[g].setMS(this.d * mom_ms[g].getMS() + (1 - this.d) * dad_ms[g].getMS());
	  				
	  				offspring_genes[g] = new Gene(offspring_ms[g],offspring_mfs);
	  				offspring_genes[g].sortMembershipFunctions();
	  			}
	  			
	  			break;
	  	case 1:
  				for (g=0; g < offspring_genes.length; g++) {
                                        offspring_mfs = new MembershipFunction[ mom_genes[g].getMembershipFunctions().length ];

  					mom_mfs = mom_genes[g].getMembershipFunctions();
  					dad_mfs = dad_genes[g].getMembershipFunctions();
                                        mom_ms[g] = mom_genes[g].getMinimumSupport();
	  				dad_ms[g] = dad_genes[g].getMinimumSupport();
  					
  					for (m=0; m < offspring_mfs.length; m++) {
  						offspring_mfs[m] = new MembershipFunction();
  						
  						offspring_mfs[m].setC((1 - this.d) * mom_mfs[m].getC() + this.d * dad_mfs[m].getC());
  						offspring_mfs[m].setW((1 - this.d) * mom_mfs[m].getW() + this.d * dad_mfs[m].getW());
  					}

                                        offspring_ms[g] = new MinimumSupport();
                                        offspring_ms[g].setMS((1 - this.d) * mom_ms[g].getMS() + this.d * dad_ms[g].getMS());

  					offspring_genes[g] = new Gene(offspring_ms[g],offspring_mfs);
  					offspring_genes[g].sortMembershipFunctions();
  				}
  				
	  			break;
	  	case 2:
	  			for (g=0; g < offspring_genes.length; g++) {
                                        offspring_mfs = new MembershipFunction[ mom_genes[g].getMembershipFunctions().length ];

					mom_mfs = mom_genes[g].getMembershipFunctions();
					dad_mfs = dad_genes[g].getMembershipFunctions();
                                        mom_ms[g] = mom_genes[g].getMinimumSupport();
	  				dad_ms[g] = dad_genes[g].getMinimumSupport();
					
					for (m=0; m < offspring_mfs.length; m++) {
						offspring_mfs[m] = new MembershipFunction();
						
						offspring_mfs[m].setC( Math.min(mom_mfs[m].getC(), dad_mfs[m].getC()) );
						offspring_mfs[m].setW( Math.min(mom_mfs[m].getW(), dad_mfs[m].getW()) );
					}

                                        offspring_ms[g] = new MinimumSupport();
                                        offspring_ms[g].setMS(Math.min(mom_ms[g].getMS(), dad_ms[g].getMS()) );

					offspring_genes[g] = new Gene(offspring_ms[g],offspring_mfs);
					offspring_genes[g].sortMembershipFunctions();
				}
	  			
	  			break;
	  	case 3:
	  			for (g=0; g < offspring_genes.length; g++) {
                                        offspring_mfs = new MembershipFunction[ mom_genes[g].getMembershipFunctions().length ];
   
	  				mom_mfs = mom_genes[g].getMembershipFunctions();
	  				dad_mfs = dad_genes[g].getMembershipFunctions();
                                        mom_ms[g] = mom_genes[g].getMinimumSupport();
	  				dad_ms[g] = dad_genes[g].getMinimumSupport();
	  				
	  				for (m=0; m < offspring_mfs.length; m++) {
	  					offspring_mfs[m] = new MembershipFunction();
	  					
	  					offspring_mfs[m].setC( Math.max(mom_mfs[m].getC(), dad_mfs[m].getC()) );
	  					offspring_mfs[m].setW( Math.max(mom_mfs[m].getW(), dad_mfs[m].getW()) );
	  				}

                                        offspring_ms[g] = new MinimumSupport();
                                        offspring_ms[g].setMS(Math.max(mom_ms[g].getMS(), dad_ms[g].getMS()) );

	  				offspring_genes[g] = new Gene(offspring_ms[g],offspring_mfs);
	  				offspring_genes[g].sortMembershipFunctions();
	  			}
	  }
	  
	  return ( new Chromosome(offspring_genes) );
  }

   /**
  * It implements the one-point mutation operator
  */
  private void mutate(ArrayList<Chromosome> pop) {
	  int p, id_attr, id_region;
	  double w,ms, eps,omega;
	  Chromosome chr;
	  Gene[] genes;
	  MembershipFunction[] membership_functions;
	  MinimumSupport minimum_support;

	  for (p=0; p < pop.size(); p++) {
		  if (Randomize.Rand() < this.pm) {
			  chr = new Chromosome( pop.get(p).getGenes() );
			  
			  genes = chr.getGenes();
			  id_attr = Randomize.Randint(0, genes.length);
			  
			  membership_functions = genes[id_attr].getMembershipFunctions();
                          minimum_support = genes[id_attr].getMinimumSupport();

			  id_region = Randomize.Randint(0, membership_functions.length);
			  
			  w = membership_functions[id_region].getW();
                          ms = minimum_support.getMS();

			  eps = Randomize.RanddoubleClosed(-w, w);
			  omega = Randomize.RanddoubleClosed(0.04-ms, 0.9-ms);

			  if (Randomize.Rand() < 0.5) {
				  membership_functions[id_region].setC(membership_functions[id_region].getC() + eps);
				 
				  genes[id_attr].sortMembershipFunctions();
			  }
			  else membership_functions[id_region].setW(w + eps);

                          minimum_support.setMS(ms + omega);
			  
			  this.evaluateFitness(chr);
			  
			  pop.add(chr);
		  }
	  }
  }
  
  private void evaluateFitness(Chromosome c) {
	  int g, id_attr, num_one_frequent_itemsets;
	  double suitability, fitness;
          double RS,finalRS;
          double RNL;
	  Gene[] genes;
          double sumOverlapFactor, sumCoverageFactor;

          sumOverlapFactor=0;
          sumCoverageFactor=0;
	  
	  genes = c.getGenes();
	  suitability = 0.0;

          num_one_frequent_itemsets = ( this.generateOneFrequentItemsets(new FuzzyDataset(this.dataset, this.transformIntoFuzzyAttributes(c) ), false) ).size();
          finalRS = 0.0;
          for (g=0; g < genes.length; g++) {
                  RNL = genes[g].getNumberOfMembershipFunctions() * this.pRNL;
                  if (num_one_frequent_itemsets<=RNL){
                      RS = num_one_frequent_itemsets / RNL;
                  }else{
                      RS = RNL / num_one_frequent_itemsets;
                  }
                  finalRS += RS;
		  id_attr = this.idOfAttributes.get(g);
		  sumOverlapFactor += ( genes[g].calculateOverlapFactor());
                  sumCoverageFactor += genes[g].calculateCoverageFactor(this.dataset.getMin(id_attr), this.dataset.getMax(id_attr));
	  }

          suitability = sumOverlapFactor + sumCoverageFactor/this.idOfAttributes.size();
	  
	 
          fitness = finalRS / suitability;
	  
	  c.setNumOneFrequentItemsets(num_one_frequent_itemsets);
	  c.setSuitability(suitability);
	  c.setFitness(fitness);
	  
	  this.nEval++;
	  
	  if ((this.nEval % this.evaluationStep) == 0) this.buildXMLRecord(fitness, num_one_frequent_itemsets, suitability);
  }
  
  private void buildXMLRecord(double fitness, int num_one_frequent_itemsets, double suitability) {
	  this.geneticLearningLog += "<log n_evaluations=\"" + this.nEval + "\" ";
	  this.geneticLearningLog += "n_generation=\"" + this.nGenerations + "\" ";
	  this.geneticLearningLog += "fitness=\"" + fitness + "\" ";
	  this.geneticLearningLog += "n_one_frequent_itemsets=\"" + num_one_frequent_itemsets + "\" ";
	  this.geneticLearningLog += "suitability=\"" + suitability + "\"/>\n";
  }
  
  private ArrayList<FuzzyAttribute> transformIntoFuzzyAttributes(Chromosome c) {
	  int g, m;
	  Gene[] genes;
	  MembershipFunction[] membership_functions;
	  FuzzyRegion[] fuzzy_regions;
	  ArrayList<FuzzyAttribute> fuzzy_attributes;
	  
	  fuzzy_attributes = new ArrayList<FuzzyAttribute>();
	  genes = c.getGenes();
	  
	  for (g=0; g < genes.length; g++) {
		  membership_functions = genes[g].getMembershipFunctions();
		  fuzzy_regions = new FuzzyRegion[ membership_functions.length ];
		  
		  for (m=0; m < membership_functions.length; m++) {
			  fuzzy_regions[m] = new FuzzyRegion();
			  
			  fuzzy_regions[m].setX0( membership_functions[m].getC() - membership_functions[m].getW() );
			  fuzzy_regions[m].setX1( membership_functions[m].getC() );
			  fuzzy_regions[m].setX3( membership_functions[m].getC() + membership_functions[m].getW() );
			  
			  fuzzy_regions[m].setY(1.0);
			  fuzzy_regions[m].setLabel("LABEL_" + m);
		  }
		  
		  fuzzy_attributes.add( new FuzzyAttribute(this.idOfAttributes.get(g), fuzzy_regions) );
	  }
	  
	  return fuzzy_attributes;
  }
  
  private void runFuzzyApriori(FuzzyDataset fuzzyDataset) {
	  int pass = 0;
	  ArrayList<Itemset> current_frequent_itemsets;
	  
	  current_frequent_itemsets = this.generateOneFrequentItemsets(fuzzyDataset, this.useMaxForOneFrequentItemsets);
	  this.countOneFrequentItemsets = current_frequent_itemsets.size();
	  this.countFrequentItemsets = this.countOneFrequentItemsets;
	  
	  System.out.println("\nPass: " + (pass + 1) + "; Total Frequent Itemsets: " + this.countFrequentItemsets);
	  
	  for (pass=1; (pass < this.dataset.getnVars()) && (current_frequent_itemsets.size() > 1); pass++) {
		  current_frequent_itemsets = this.generateCandidateItemsetsAndRules(fuzzyDataset, current_frequent_itemsets);
		  this.countFrequentItemsets += current_frequent_itemsets.size();
		  
		  System.out.println("Pass: " + (pass + 1) + "; Total Frequent Itemsets: " + this.countFrequentItemsets + "; Total Association Rules: " + this.associationRulesSet.size());
	  }
  }
  
  private ArrayList<Itemset> generateOneFrequentItemsets(FuzzyDataset fuzzyDataset, boolean use_max_for_one_frequent_itemsets) {
	  int id_attr, id_region;
	  double max_support;
	  int[] num_fuzzy_regions;
	  Itemset itemset, best_itemset;
	  ArrayList<Itemset> one_frequent_itemsets;
	  
	  num_fuzzy_regions = fuzzyDataset.getNumberOfFuzzyRegions();
	  one_frequent_itemsets = new ArrayList<Itemset>();
	  
	  if (use_max_for_one_frequent_itemsets) {
		  
		  for (id_attr=0; id_attr < fuzzyDataset.getNumberOfFuzzyAttributes(); id_attr++) {
			  best_itemset = new Itemset();
			  best_itemset.add( new Item(id_attr, 0) );
			  best_itemset.calculateSupport(fuzzyDataset);
			  max_support = best_itemset.getSupport();
			  
			  for (id_region=1; id_region < num_fuzzy_regions[id_attr]; id_region++) {
				  itemset = new Itemset();
				  itemset.add( new Item(id_attr, id_region) );
				  itemset.calculateSupport(fuzzyDataset);
				  
				  if (itemset.getSupport() > max_support) {
					  max_support = itemset.getSupport();
					  best_itemset = itemset;
				  }
			  }
			  if (id_attr < this.minimum_supports.length){
                            if (max_support >= this.minimum_supports[id_attr].getMS()) one_frequent_itemsets.add(best_itemset);
                          }else{
                            if (max_support >= 0.04) one_frequent_itemsets.add(best_itemset);
                          }
		  }
	  }
	  else {
		  for (id_attr=0; id_attr < fuzzyDataset.getNumberOfFuzzyAttributes(); id_attr++) {
			  
			  for (id_region=0; id_region < num_fuzzy_regions[id_attr]; id_region++) {
				  itemset = new Itemset();  
				  itemset.add( new Item(id_attr, id_region) );
				  itemset.calculateSupport(fuzzyDataset);
                                  //System.out.println("\n" + id_attr);

                                  if (id_attr < this.minimum_supports.length){
                                    if (itemset.getSupport() >= this.minimum_supports[id_attr].getMS()) one_frequent_itemsets.add(itemset);
                                  }else{
                                      if (itemset.getSupport() >= 0.04) one_frequent_itemsets.add(itemset);
                                  }
                          }
		  }
	  }
	  
	  return one_frequent_itemsets;
  }
  
  private ArrayList<Itemset> generateCandidateItemsetsAndRules(FuzzyDataset fuzzyDataset, ArrayList<Itemset> curr_freq_itemsets) {
	  int i, j, size;
	  boolean generated_rules;
	  Itemset i_itemset, j_itemset, new_itemset;
	  ArrayList<Integer> covered_tids;
	  ArrayList<Itemset> next_freq_itemsets;
	  
	  size = curr_freq_itemsets.size();
	  next_freq_itemsets = new ArrayList<Itemset>();
	  
	  for (i=0; i < size-1; i++) {
		  i_itemset = curr_freq_itemsets.get(i);
		  
		  for (j=i+1; j < size; j++) {
			  j_itemset = curr_freq_itemsets.get(j);
			  
			  if ( this.isCombinable(i_itemset, j_itemset, curr_freq_itemsets) ) {
				  new_itemset = i_itemset.clone();
				  new_itemset.add( ( j_itemset.get(j_itemset.size() - 1) ).clone() );
				  covered_tids = new_itemset.calculateSupport(fuzzyDataset);

                                  if (i < this.minimum_supports.length){
                                      if (new_itemset.getSupport() >= this.minimum_supports[i].getMS()) {
                                              generated_rules = this.generateRulesFromItemset(fuzzyDataset, new_itemset);
                                              if (generated_rules) this.markCoveredRecords(covered_tids);

                                              next_freq_itemsets.add(new_itemset);
                                      }
                                  }else{
                                      if (new_itemset.getSupport() >= 0.1) {
                                              generated_rules = this.generateRulesFromItemset(fuzzyDataset, new_itemset);
                                              if (generated_rules) this.markCoveredRecords(covered_tids);

                                              next_freq_itemsets.add(new_itemset);
                                      }
                                  }
			  }
		  }
	  }
	  
	  return next_freq_itemsets;
  }
  
  private boolean generateRulesFromItemset(FuzzyDataset fuzzyDataset, Itemset curr_itemset) {
	  int i;
	  double rule_sup, ant_sup, rule_conf,cons_sup,interest;
	  boolean generated_rules = false;
	  Item i_item;
	  Itemset antecedent, consequent;

	  for (i=0; i < curr_itemset.size(); i++) {
		  antecedent = curr_itemset.clone();
		  i_item = antecedent.remove(i);
		  antecedent.calculateSupport(fuzzyDataset);

		  rule_sup = curr_itemset.getSupport();
		  ant_sup = antecedent.getSupport();
		  rule_conf = rule_sup / ant_sup;

		  if (rule_conf >= this.minConfidence) {
			  consequent = new Itemset();
			  consequent.add(i_item);
                          consequent.calculateSupport(fuzzyDataset);
                          cons_sup = consequent.getSupport();
                          interest = rule_conf * (rule_sup/cons_sup) * (1 - (rule_sup/this.dataset.getnTrans()));
			  this.associationRulesSet.add( new AssociationRule(antecedent, consequent, rule_sup, ant_sup, rule_conf,cons_sup,interest) );

			  if (! generated_rules) generated_rules = true;
		  }
	  }

	  return generated_rules;
  }
  
  private boolean isCombinable(Itemset i_itemset, Itemset j_itemset, ArrayList<Itemset> curr_freq_itemsets) {
	  int i;
	  Item i_item, j_item;
	  Itemset itemset;

	  if (i_itemset.size() != j_itemset.size()) return false;

	  i_item = i_itemset.get(i_itemset.size() - 1);
	  j_item = j_itemset.get(i_itemset.size() - 1);
	  
	  if (i_item.getIDAttribute() >= j_item.getIDAttribute()) return false;

	  for (i=0; i < (i_itemset.size() - 1); i++) {
		  i_item = i_itemset.get(i);
		  j_item = j_itemset.get(i);
		  
		  if (! i_item.equals(j_item)) return false;
	  }
	  
	  itemset = i_itemset.clone();
	  itemset.add( ( j_itemset.get(i_itemset.size() - 1) ).clone() );
	  if ( this.pruning(itemset, curr_freq_itemsets) ) return false;

	  return true;
  }
  
  private boolean pruning(Itemset itemset, ArrayList<Itemset> curr_freq_itemsets) {
	  int i;
	  Itemset sub;

	  for (i=0; i < itemset.size() - 2; i++) {
		  sub = itemset.clone();
		  sub.remove(i);
		  if (! this.existingIntoFrequentItemsets(sub, curr_freq_itemsets)) return true;
	  }
	  
	  return false;
  }
  
  private boolean existingIntoFrequentItemsets(Itemset itemset, ArrayList<Itemset> curr_freq_itemsets) {
	  int i;
	  Itemset its;

	  for (i=0; i < curr_freq_itemsets.size(); i++) {
		  its = curr_freq_itemsets.get(i);
		  if ( its.equals(itemset) ) return true;
	  }
	  
	  return false;
  }
  
  private void markCoveredRecords(ArrayList<Integer> covered_tids) {
	  int i, t;
	  
	  for (i=0; i < covered_tids.size(); i++) {
		  t = covered_tids.get(i);
		  if (! this.coveredRecords[t]) this.coveredRecords[t] = true;
	  }
  }
  
  private int countCoveredRecords() {
	  int i, cnt_covered_records = 0;
	  
	  for (i=0; i < this.coveredRecords.length; i++) {
		  if (this.coveredRecords[i]) cnt_covered_records++;
	  }
	  
	  return cnt_covered_records;
  }
  
}