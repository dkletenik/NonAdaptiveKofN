
public class Node implements PrintableNode {
	Variable var;
	Node nodeZero;
	Node nodeOne;


	public Node(Variable v)
	{
		var = v;
		nodeZero = null;
		nodeOne = null;
	}
	
	public String getText(){return var.name;}
	
	public Node getLeft(){return nodeZero;}
	
	public Node getRight() {return nodeOne;}
	
	
	
	
	public void printTree() {
        if (nodeOne != null) {
            nodeOne.printTree(true, "");
        }
        
        printNodeValue();
        if (nodeZero != null) {
            nodeZero.printTree(false, "");
        }
    }
    private void printNodeValue() {
        if (var == null) {
            System.out.print("<null>");
        } else {
            System.out.print(var.name);
        }
        System.out.print('\n');
    }
    // use string and not stringbuffer on purpose as we need to change the indent at each recursion
    private void printTree(boolean isRight, String indent) {
        if (nodeOne != null) {
            nodeOne.printTree(true, indent + (isRight ? "        " : " |      "));
        }
        System.out.print(indent);
        if (isRight) {
           System.out.print(" /");
        } else {
            System.out.print(" \\");
        }
        System.out.print("----- ");
        printNodeValue();
        if (nodeZero != null) {
            nodeZero.printTree( false, indent + (isRight ? " |      " : "        "));
        }
    }
	
	
}