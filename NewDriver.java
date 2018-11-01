import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
public class NewDriver 
{
	public static void main(String[] args) 
	{
		final int N = 7;
		
		ThreadLocalRandom rand = ThreadLocalRandom.current();
		boolean found = false;
		
		while (!found)
		{
			System.out.println("new round");
			
			ArrayList<Variable> vars = new ArrayList<Variable>();
			for (int i = 0; i < N; i++)
			{
				Integer newInteger = rand.nextInt(1,10);
				double prob = (double)newInteger/10;
				
				Integer newCost = rand.nextInt(1,10);
				//int newCost = 1;
				vars.add(new Variable("x"+i, prob, newCost));
			}
				

			/*vars.add(new Variable("x1", .25, 2000));
			vars.add(new Variable("x2", .5, 3000));
			vars.add(new Variable("x3", .33, 3000));
			vars.add(new Variable("x4", .4375, 3000));*/
			
			System.out.println("STARTING AGAIN");
			System.out.println(vars);
			
			int k = N/2;
			
			
			NewAdaptiveAlg adaptive = new NewAdaptiveAlg(vars, N, k);
			adaptive.buildTree();
			double costOfAdaptive = adaptive.findCost();
			
			
			NewAllPermutations permutations = new NewAllPermutations (vars, N, k);
			double costOfNA = permutations.findCostOfCheapestPermutation();
			//permutations.printAllCosts();
			
			//System.out.println("cost of Adaptive: " + costOfAdaptive);
			//System.out.println("Cost of Non Adaptive: "  + costOfNA);
			
			double ratio = costOfNA/costOfAdaptive;
			//System.out.println("NA/ A " + ratio);
			System.out.println("NA/ A " + ratio);

			
			if (ratio >= 1.29)
			{
				found = true;
				System.out.println("Cost of adaptive: " + costOfAdaptive);
				adaptive.printTreeVertical();
				permutations.printAllCosts();
				
				System.out.println("cost of Adaptive: " + costOfAdaptive);
				System.out.println("Cost of Non Adaptive: "  + costOfNA);
				
				
				System.out.println("NA/ A " + ratio);
			}
				
		}
		
	}

}
