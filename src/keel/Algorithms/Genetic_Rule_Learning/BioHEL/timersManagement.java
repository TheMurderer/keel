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

package keel.Algorithms.Genetic_Rule_Learning.BioHEL;

import java.util.Vector;

public class timersManagement {
	
	Vector<timingProcess> timers;
	int iteration;
	public timerMDL tMDL;
	void addTimer(timingProcess tp){timers.addElement(tp);}
	

	public timersManagement(){
		
		timers = new Vector<timingProcess>();
		
		iteration = -1;
		tMDL = new timerMDL();
		addTimer(tMDL);
	}




	public void incIteration(int lastIteration){
		iteration++;

		int i;
		for(i=0;i<timers.size();i++)
			timers.get(i).newIteration(iteration,lastIteration);
	}

	public void reinit(){
		iteration=-1;

		int i;
		for(i=0;i<timers.size();i++)
			timers.get(i).reinit();
	}


	public void dumpStats()
	{
		int i;
		for(i=0;i<timers.size();i++)
			timers.get(i).dumpStats(iteration);
	}

	public void setPW(populationWrapper pPW)
	{
		int i;
		for(i=0;i<timers.size();i++)
			timers.get(i).initialize(pPW);
	}

}

