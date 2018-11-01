
public class Variable implements Comparable<Variable> {
	String name;
	double pi;
	double mpi;
	int cost;
	
	public Variable(String n, double p, int c)
	{
		name = n;
		pi = p;
		mpi = 1 - pi;
		cost = c;
	}
	
	@Override
	public String toString()
	{
		return name + " " + pi + " " + cost;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;
		
		if(o.getClass() != this.getClass())
			return false;
		
		Variable var = (Variable)o;
		return (var.name.equals(this.name)) && (var.pi == this.pi) && (var.mpi == this.mpi) && (var.cost == this.cost);
	}
	
	@Override
	public int compareTo(Variable v)
	{
		//Variable v = (Variable)o;
		if (this.pi > v.pi)
			return 1;
		else if (this.pi < v.pi)
			return -1;
		return 0;
	}

	public double ciOverPi()
	{
		return (double)cost/pi;
	}
	
	public double ciOver1Pi()
	{
		return (double) cost/mpi;
	}
	
}
