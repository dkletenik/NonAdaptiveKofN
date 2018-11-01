import java.util.ArrayList;
import java.util.List;

public class NewAdaptiveAlg {
	int n;
	int k;
	final String NEGATIVE = "-";
	final String POSITIVE = "+";
	final String RIGHT = "right";
	final String LEFT = "left";
	ArrayList<Variable> allVars;
	ArrayList<Variable> listCiOverPi; //cheapest ci/pi first
	ArrayList<Variable> listCiOver1Pi; //cheapest ci/1-pi first
	Node root;

	
	public NewAdaptiveAlg(ArrayList<Variable> vars, int N, int K)
	{
		n = N;
		k = K;
		allVars = new ArrayList<Variable>(vars);		
		
		//create list sorted in increasing order pi
		listCiOverPi = sortCiPi(allVars);
		listCiOver1Pi = sortCi1Pi(allVars);
		//buildTree();

	}
	
	public void buildTree()
	{
		//create root and remove it from the lists
		Variable rootVar = kOfNChoice(n, k, listCiOverPi, listCiOver1Pi);
		root = new Node(rootVar);
		listCiOverPi.remove(rootVar);
		listCiOver1Pi.remove(rootVar);
		
		//create left branch
		
		//create right subtree of root
		createSubtree(root, listCiOverPi, listCiOver1Pi, n-1, k-1, 1, 0, RIGHT);
		
		//create left subtree of root
		createSubtree(root, listCiOverPi, listCiOver1Pi, n-1, k, 0, 1, LEFT);
		
		
		
		
	}
	
	
	public Variable kOfNChoice (int n, int k, ArrayList<Variable> lookingFor1s, ArrayList<Variable> lookingFor0s)
	//finds the variable (at least one) in the intersection of the high ci/pi and the high ci/1-pi lists.
	{
		if (n == 1)
		{
			return lookingFor1s.get(0); //only one choice anyway
		}
		
		int nk1 = n - k + 1;
		/*System.out.println("n = " + n);
		System.out.println("k = " + k);
		System.out.println("n - k + 1 = " + nk1);
		System.out.println("looking for 1s: " + lookingFor1s);
		System.out.println("Looking for 0s: " + lookingFor0s);*/
		//sublist has from index, inclusive; to index, exclusive.
		List<Variable> kHighProb = lookingFor1s.subList(0,k); //in first k positions of the "1s" list
		List<Variable> nK1LowProb = lookingFor0s.subList(0, nk1); //in first n - k + 1 positions of "0s" list
		
		Variable next = null;
		for (Variable v:kHighProb)
		{
			if (nK1LowProb.contains(v))
			{
				//System.out.println(v);
				return v;
			}
		}
		
		System.out.println("ERROR: no intersection");
		return next;
	}
	
	
	public void createSubtree(Node subRoot, ArrayList<Variable> lookingFor1s, ArrayList<Variable> lookingFor0s, int n, int k, int numOnes, int numZeroes, String side)
	//side says which side to make the subnode (is this creating a left subtree or right?)
	//k is the "k" of k-of-n function. 
	{
		Node newNode;
		//check to see if you have already finished
		newNode = verified(numOnes, numZeroes);

		if (newNode != null)
		{
			if (side.equals(RIGHT))
				subRoot.nodeOne = newNode;
			else
				subRoot.nodeZero = newNode;
			return;

		}
		
		Variable nextVar = kOfNChoice(n, k, lookingFor1s, lookingFor0s);
		newNode = new Node(nextVar);
	
		if (nextVar == null)
			return;
			
		if (side.equals(RIGHT))
			subRoot.nodeOne = newNode;
		else
			subRoot.nodeZero = newNode;

					
		ArrayList<Variable> newLookingFor1s = new ArrayList<Variable>(lookingFor1s);
		ArrayList<Variable> newLookingFor0s = new ArrayList<Variable>(lookingFor0s);
		newLookingFor1s.remove(nextVar);
		newLookingFor0s.remove(nextVar);
	
		createSubtree(newNode, newLookingFor1s, newLookingFor0s, n-1, k-1, numOnes + 1, numZeroes, RIGHT);
		createSubtree(newNode, newLookingFor1s, newLookingFor0s, n-1, k, numOnes, numZeroes + 1, LEFT);
		
	}

	
	public Node verified (int numOnes, int numZeroes)
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
	
	
	
	
	
	public double findCost()
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
	
	
	
	public void printTreeVertical()
	{
		root.printTree();
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
		
		for (Variable var:newVars)
			System.out.println(var + " " +  var.ciOverPi());
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
		for (Variable var:newVars)
			System.out.println(var + " " +  var.ciOver1Pi());
		
		return newVars;
	}
	
	

	
	

}
