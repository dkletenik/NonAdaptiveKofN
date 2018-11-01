import java.util.*;
public class NewAllPermutations
//This class creates all permutations (non-adaptive) k-of-n strategies and finds the cheapest one.
{
	int n;
	int k;
	final String NEGATIVE = "-";
	final String POSITIVE = "+";
	final String RIGHT = "right";
	final String LEFT = "left";
	ArrayList<Variable> allVars;
	ArrayList<Variable> listCiOverPi; //cheapest ci/pi first
	ArrayList<Variable> listCiOver1Pi; //cheapest ci/1-pi first
	ArrayList<Variable> permutation;
	ArrayList<ArrayList<Variable>> allPermutations;
	ArrayList<Double> allPermutationsCost;
	//boolean isTie = false;
	
	//Node root;
	
	
	
	
	public NewAllPermutations(ArrayList<Variable> vars, int N, int K)
	{
		n = N;
		k = K;
		allVars = new ArrayList<Variable>(vars);
		allPermutations = generatePermutations(vars);
		
		
		//create list sorted in increasing order pi
		listCiOverPi = sortCiPi(allVars);
		listCiOver1Pi = sortCi1Pi(allVars);
		allPermutationsCost = new ArrayList<Double>();


	}
	
	
	public ArrayList<ArrayList<Variable>> generatePermutations(ArrayList<Variable> arr)
	{
		if (arr.size() == 0)
		{
			ArrayList<ArrayList<Variable>> result = new ArrayList<ArrayList<Variable>>();
			result.add(new ArrayList<Variable>());
			return result;
		}
		
		Variable firstVar = arr.remove(0);
		ArrayList<ArrayList<Variable>> returnValue = new ArrayList<ArrayList<Variable>>();
		ArrayList<ArrayList<Variable>> permutations = generatePermutations(arr);
		for (ArrayList<Variable> smallerPermutated: permutations)
		{
			for (int index = 0 ; index <= smallerPermutated.size(); index++)
			{
				ArrayList<Variable> temp = new ArrayList<Variable> (smallerPermutated);
				temp.add(index, firstVar);
				returnValue.add(temp);
			}
		}
		return returnValue;
		
	}
	
	public double findCostOfCheapestPermutation()
	{
		double bestCost = Double.MAX_VALUE;
		System.out.println("best cost: " + bestCost);
		ArrayList<Variable> bestPermutation = new ArrayList<Variable>();
		
		for (int i = 0; i < allPermutations.size(); i++)
		{
			ArrayList<Variable> permutation = allPermutations.get(i);
			Node root = new Node(permutation.get(0));
			ArrayList<Variable> newPerm = new ArrayList<Variable> (permutation);
			newPerm.remove(0);
			createSubtree(root, newPerm, 0, 1, RIGHT);
			createSubtree(root, newPerm, 1, 0, LEFT);
			double cost = findCost(root);
			allPermutationsCost.add(cost);
			
			if (cost < bestCost)
			{
				bestPermutation = permutation;
				bestCost = cost;
			}
		}
		
		System.out.println("best Cost: " + bestCost);
		System.out.println("best permutation: " + bestPermutation);
		//return permutation; 
		return bestCost;
	}
	
	/*boolean bestPermutationIsCounterexample()
	{
		ArrayList<Variable> bestPermutation = findCheapestPermutation();
		boolean found = isCounterexample(bestPermutation);
		if (!isTie && found)
		{
			System.out.println(bestPermutation);
			//createAndPrintTree(bestPermutation);
			return true;
		}
		//return bestCost;
		return false;
	}*/
		/*
	
	boolean bestPermutationDoesNotStartWithKBits(int k)
	{
		ArrayList<Variable> bestPermutation = findCheapestPermutation();
		boolean found = isCounterexampleToKBitsConjecture(bestPermutation,k);
		//if (!isTie && found)
		if (found)
		{
			System.out.println(bestPermutation);
			//createAndPrintTree(bestPermutation);
			return true;
		}
		//return bestCost;
		return false;
	}
	
	/*boolean isCounterexample(ArrayList<Variable> permutation)
	{
		System.out.println("increasing pi");
		System.out.println(listLowestProbFirst);
		System.out.println("decreasing pi");
		System.out.println(listHighestProbFirst);
		
		//boolean isCounterexample = false;
		
		for (int i = 0; i < permutation.size(); i++)
		{
			Variable var = permutation.get(i);
			if (var == listLowestProbFirst.get(0))
				listLowestProbFirst.remove(0);
			
			else if (var == listHighestProbFirst.get(0))
				listHighestProbFirst.remove(0);
			
			else 
				return true; //is counterexample
		}
		return false;
	}*/
	/*
	
	boolean isCounterexampleToKBitsConjecture(ArrayList<Variable> permutation, int k)
	{
		//returns true if the optimal permutation does NOT begin with something within the 
		//first k probabilities
		
		System.out.println("increasing pi");
		System.out.println(listLowestProbFirst);
		System.out.println("decreasing pi");
		System.out.println(listHighestProbFirst);
		System.out.println("best permutation: " + permutation);
		
		ArrayList<Variable> newListHighestProbFirst = new ArrayList<Variable>(listHighestProbFirst);
		
		//boolean isCounterexample = false;
		
		//for (int i = 0; i < permutation.size(); i++, k--)
		//must check first k variables because they can all be permuted symmetrically 
		for (int i = 0; i < k; i++)
		{
		//	int i = 0;
			Variable firstVar = permutation.get(i);
			ArrayList highestKList = new ArrayList<Variable>(newListHighestProbFirst.subList(0, k+1)); //this handles inductive
			//that first k variables have to be subset of first k+1 variables
			System.out.println("highest k bits: " + highestKList);
			if (highestKList.contains(firstVar))
				return false; //is not counterexample
			//else
				//newListHighestProbFirst.remove(firstVar);
		}
		return true;
	}
	*/
	
	
	
	//creates right descendent of root (nodeOne). Recursively creates subtrees of that node	
	public void createSubtree(Node subRoot, ArrayList<Variable> permutation, int numZeroes, int numOnes, String direction)
	{
		
		//check if reached goal value
		
		//check if right hand side is verified -- really has an additional one
		Node verifiedNode = verified(numZeroes,numOnes);
		if (verifiedNode != null)
		{
			if (direction.equals(RIGHT))
				subRoot.nodeOne = verifiedNode;
			if (direction.equals(LEFT))
				subRoot.nodeZero = verifiedNode;
			return;
		}
		
		//right subtree: :look for a zero -- find next on increasing order of 1 - pi
		if (permutation.size()  > 0)
		{
			Variable newVar = permutation.get(0);
			Node node2 = new Node(newVar);
			
			if (direction.equals(RIGHT))
				subRoot.nodeOne = node2;
			if (direction.equals(LEFT))
				subRoot.nodeZero = node2;
			
			ArrayList<Variable> newPermutation = new ArrayList<Variable> (permutation);
			newPermutation.remove(0);
			
			createSubtree(node2, newPermutation, numZeroes, numOnes+1, RIGHT);
			createSubtree(node2, newPermutation, numZeroes+1, numOnes, LEFT);
		}
		else
			System.out.println("ERROR: in subtree, direction " + direction + " , orderedVars.size is 0");
	}
	
	
	
	

	//creates "verified" node if goal value is reached (at least two 0s AND at least one 1)
	public Node verified (int numZeroes, int numOnes)
	{
		if (numZeroes >= n-k+1) //done, enough zeroes
		{
			
			Node nodeVerified = new Node(new Variable (NEGATIVE, 1, 1)); //goal value reached
			return nodeVerified;
		}
		
		else if (numOnes >= k) //done, enough ones
		{
			Node nodeVerified = new Node(new Variable(POSITIVE, 1, 1));
			return nodeVerified;
		}
	
		return null;
	}

	public void printTree()
	{
		//System.out.println("printing tree");
		//TreePrinter.print(root);
	}
	
	public double findCost(Node root)
	{
		return recurseFindCost(root, 1, 0);
	}
	
	public double recurseFindCost(Node node, double probability, int costSoFar)
	{
	
		if (node.var.name.equals(POSITIVE) || node.var.name.equals(NEGATIVE)) //leaf, return cost
		{
			double cost = probability * costSoFar;
			//System.out.println("cost of " + node.var + " " + cost + " prob: " + probability + " cost: " + costSoFar);
			return cost;
		}
			 
		
		return recurseFindCost(node.nodeOne, probability * node.var.pi, costSoFar + node.var.cost) + recurseFindCost(node.nodeZero, probability * node.var.mpi, costSoFar + node.var.cost);
	}
	
	
	
	public void printTreeVertical(Node root)
	{
		root.printTree();
	}
	
	public void createAndPrintTree(ArrayList<Variable> perm)
	{
		System.out.println("permutation: " + perm);
		Node root = new Node(perm.get(0));
		ArrayList<Variable> newPerm = new ArrayList<Variable> (perm);
		newPerm.remove(0);
		createSubtree(root, newPerm, 0, 1, RIGHT);
		createSubtree(root, newPerm, 1, 0, LEFT);
		
		
		printTreeVertical(root);
	}
	
	
	public ArrayList<Variable> sortCiPi(ArrayList<Variable> vars) {

		ArrayList<Variable> newVars = new ArrayList<Variable>(vars);
		
		int smallestIndex;

		// declare an int variable to hold the smallest value for each iteration
		// of the outer loop
		Variable smallest;

		// for each index in the array list
		for (int curIndex = 0; curIndex < newVars.size(); curIndex++) {

			/* find the index at which the element has smallest value */
			// initialize variables
			smallest = newVars.get(curIndex);
			smallestIndex = curIndex;

			for (int i = curIndex + 1; i < newVars.size(); i++) {
				if (smallest.ciOverPi() > newVars.get(i).ciOverPi()) {
					// update smallest
					smallest = newVars.get(i);
					smallestIndex = i;
				}
			}

			/* swap the value */
			// do nothing if the curIndex has the smallest value
			if (smallestIndex != curIndex)
			{
				Variable temp = newVars.get(curIndex);
				newVars.set(curIndex, newVars.get(smallestIndex));
				newVars.set(smallestIndex, temp);
			}

		}
		
		return newVars;
	}
	
	
	public ArrayList<Variable> sortCi1Pi(ArrayList<Variable> vars) {

		ArrayList<Variable> newVars = new ArrayList<Variable>(vars);
		
		int smallestIndex;

		// declare an int variable to hold the smallest value for each iteration
		// of the outer loop
		Variable smallest;

		// for each index in the array list
		for (int curIndex = 0; curIndex < newVars.size(); curIndex++) {

			/* find the index at which the element has smallest value */
			// initialize variables
			smallest = newVars.get(curIndex);
			smallestIndex = curIndex;

			for (int i = curIndex + 1; i < newVars.size(); i++) {
				if (smallest.ciOver1Pi() > newVars.get(i).ciOver1Pi()) {
					// update smallest
					smallest = newVars.get(i);
					smallestIndex = i;
				}
			}

			/* swap the value */
			// do nothing if the curIndex has the smallest value
			if (smallestIndex != curIndex)
			{
				Variable temp = newVars.get(curIndex);
				newVars.set(curIndex, newVars.get(smallestIndex));
				newVars.set(smallestIndex, temp);
			}

		}
		
		return newVars;
	}
	
	public void printAllCosts()
	{
		for (int i = 0; i < allPermutationsCost.size(); i++)
		{
			System.out.println("permutation: " + allPermutations.get(i) +  " cost: " + allPermutationsCost.get(i) );
		}
	}
	
}


