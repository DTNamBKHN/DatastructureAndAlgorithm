import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

class Node {
	String name;
	Node leftMostChild;
	Node rightSibling;

	public Node(String name) {
		this.name = name;
		this.leftMostChild = null;
		this.rightSibling = null;
	}
}

public class FamilyTree {

	Node root;
	
	public FamilyTree(String rootName) {
		root = new Node(rootName);
	}
	
	private Node find(Node r, String name) {
		if (r == null) return null;
		if (r.name.equals(name)) return r;
		Node p = r.leftMostChild;
		while (p != null) {
			Node pv = find(p, name);
			if (pv != null) return pv;
			p = p.rightSibling;
		}
		return null;
	}
	
	public Node find(String name) {
		return find(root, name);
	}
	
	public void addChild(Node p, String value) {
		//create new node pv with name = value
		//make pv as a child of p
		Node pv = new Node(value);
		Node pi = p.leftMostChild; //head of the children list
		if (pi == null) {
			p.leftMostChild = pv;
		} else {
			while (pi.rightSibling != null) {
				pi = pi.rightSibling;
			}
			pi.rightSibling = pv;
		}
	}
	
	public void addChild(Node p, Node child) {
		//there is already a "child" node in tree
		Node pi = p.leftMostChild; //head of the children list
		if (pi == null) {
			p.leftMostChild = child;
		} else {
			while (pi.rightSibling != null) {
				pi = pi.rightSibling;
			}
			pi.rightSibling = child;
		}
	}
	
	private void preorder(Node r) {
		if (r == null) return;
		System.out.print(r.name + " ");
		Node p = r.leftMostChild;
		while (p != null) {
			preorder(p);
			p= p.rightSibling;
		}
	}
	
	public void preorder() {
		preorder(root);
	}
	
	private int count(Node r) {
		if (r == null) return 0;
		int c = 1;
		Node p = r.leftMostChild;
		while (p != null) {
			int cp = count(p);
			c = c + cp;
			p = p.rightSibling;
		}
		return c;
	}
	
	public int count() {
		return this.count(root);
	}
	
	private int height(Node p) {
		if (p == null) return 0;
		int h = 0;
		Node pi = p.leftMostChild;
		while (pi != null) {
			int hi = height(pi);
			h = h > hi ? h : hi;
			pi = pi.rightSibling;
		}
		return h + 1;
	}
	
	public int height(String name) {
		Node r = find(name);
		return height(r);
	}
	
	//Find the parent of a node of the tree
	private Node parent(Node r, Node p) {
		if (r == null) return null;
		Node q = r.leftMostChild;
		while (q != null) {
			if (q == p) return r;
			Node h = parent(q, p);
			if (h != null) return h;
			q = q.rightSibling;
		}
		return null;
	}
	
	public Node parent(Node p) {
		return parent(root, p);
	}
	
	public Node readFile(Node root) {
		ArrayList<String> wasParentArrayList = new ArrayList<String>();
		ArrayList<String> wasChildArrayList = new ArrayList<String>();
		int i = 0, j = 0;
		FileReader reader;
		try {
			reader = new FileReader("data.txt");
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			String line;
			int time = 0;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					String[] arrOfStr = line.split(" ", 2);
					Node parentNode = find(root, arrOfStr[1]);
					if (parentNode != null) {//parentNode exists
						Node childNode = find(root, arrOfStr[0]);
						if(childNode != null) {//childNode exists in tree
							addChild(parentNode, childNode);
						}
						else {//childNode does not exist in tree
							addChild(parentNode, arrOfStr[0]);
						}
						System.out.println("Thang BO hien tai "+ parentNode.name);
//						System.out.println("TEN CUA CON: " + temp.leftMostChild.name);
						time++;
						System.out.println("Chay dc " + time + " lan roi ne ulala");
						preorder(parentNode);
					}
					else {//parentNode does not exist
						Node newParent = new Node(arrOfStr[1]);
						addChild(root, newParent);
						
						Node childNode = find(root, arrOfStr[0]);
						if(childNode != null) {//childNode exists in tree
							addChild(newParent, childNode);
						}
						else {//childNode does not exist in tree
							addChild(newParent, arrOfStr[0]);
						}
						System.out.println("Thang bo hien tai " + newParent.name);
//						System.out.println("Ten cua con " + newParent.leftMostChild.name);
						time++;
						System.out.println("Chay dc " + time + " lan roi ne");
						preorder(newParent);
						wasParentArrayList.add(arrOfStr[1]);
					}
					if (!wasChildArrayList.contains(arrOfStr[0])) {
						wasChildArrayList.add(arrOfStr[0]);
					}
				}
//				Node findNode = find("Mark");
//				System.out.println("tim node trong ***: " + findNode.leftMostChild.name);
//				preorder(findNode);
				String[] wasParentArray = new String[wasParentArrayList.size()];
				String[] wasChildArray = new String[wasChildArrayList.size()];
				Iterator k = wasParentArrayList.iterator();
				while (k.hasNext()) {
					wasParentArray[i++] = (String) k.next();
			      }
				Iterator l = wasChildArrayList.iterator();
				while (l.hasNext()) {
					wasChildArray[j++] = (String) l.next();
			      }
				preorder(root);
				int checkAncestor = 0;
				for (i = 0; i < wasParentArray.length; i++) {
					for (j = 0; j < wasChildArray.length; j++) {
						if (wasParentArray[i].equals(wasChildArray[j])) {
							break;
						}
						checkAncestor++;
					}
					if (checkAncestor == wasChildArray.length) {
						root = find(wasParentArray[i]);
						break;
					}
					checkAncestor = 0;
				}
				
				bufferedReader.close();
				reader.close();
				return root;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FamilyTree tree = new FamilyTree("***");
//		tree.addChild(tree.root, "Peter");
//		tree.addChild(tree.root, "Mark");
//		tree.addChild(tree.root, "David");
//		Node mark = tree.find("Mark");
//		tree.addChild(mark, "Paul");
//		tree.addChild(mark, "Stepen");
//		tree.addChild(mark, "Thomas");
//		Node david = tree.find("David");
//		tree.addChild(david, "John");
//		tree.addChild(david, "Bill");
//		Node thomas = tree.find("Thomas");
//		tree.addChild(thomas, "Michael");
//		tree.addChild(thomas, "Pierre");
		tree.root = tree.readFile(tree.root);
		System.out.println("goc hien tai: "+ tree.root.name);
		tree.preorder();
	}

}
