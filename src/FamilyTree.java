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
	
	public FamilyTree() {
		super();
	}

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
	
	private void preorder(Node r) {
		if (r == null) return;
		System.out.print(r.name + " ");
		Node p = r.leftMostChild;
		while (p != null) {
			preorder(p);
			p = p.rightSibling;
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
	
	public static String findRoot() {
		ArrayList<String> wasParentArrayList = new ArrayList<String>();
		ArrayList<String> wasChildArrayList = new ArrayList<String>();
		ArrayList<String> ancestorList = new ArrayList<String>();
		int i = 0, j = 0;
		FileReader reader;
		try {
			reader = new FileReader("data.txt");
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			String line;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					String[] arrOfStr = line.split(" ", 2);
					if(!wasParentArrayList.contains(arrOfStr[1])) {
						wasParentArrayList.add(arrOfStr[1]);
					}
					
					if (!wasChildArrayList.contains(arrOfStr[0])) {
						wasChildArrayList.add(arrOfStr[0]);
					}
				}
				String[] wasParentArray = new String[wasParentArrayList.size()];
				String[] wasChildArray = new String[wasChildArrayList.size()];
				Iterator<String> k = wasParentArrayList.iterator();
				while (k.hasNext()) {
					wasParentArray[i++] = (String) k.next();
			      }
				Iterator<String> l = wasChildArrayList.iterator();
				while (l.hasNext()) {
					wasChildArray[j++] = (String) l.next();
			      }
				
				int checkAncestor = 0;
				for (i = 0; i < wasParentArray.length; i++) {
					for (j = 0; j < wasChildArray.length; j++) {
						if (wasParentArray[i].equals(wasChildArray[j])) {
							break;
						}
						checkAncestor++;
					}
					if (checkAncestor == wasChildArray.length) {
						ancestorList.add(wasParentArray[i]);
					}
					checkAncestor = 0;
				}
				if (ancestorList.size() > 1) {
					bufferedReader.close();
					reader.close();
					return null;
				}
				bufferedReader.close();
				reader.close();
				return ancestorList.get(0);
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
	
	public void buildTree() {
		try {
			FileReader reader = new FileReader("data.txt");
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					String[] arrOfStr = line.split(" ", 2);
					Node findNode = find(arrOfStr[1]);
					if (findNode != null) {//node exists
						System.out.println("ten cua node duoc tim thay: " + findNode.name);
						addChild(findNode, arrOfStr[0]);
					}
					else {//node does not exist
						Node newParent = new Node(arrOfStr[1]);
						addChild(newParent, arrOfStr[0]);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ancestor = FamilyTree.findRoot();
		if (ancestor != null) {
			System.out.println("Ancestor: " + ancestor);
		}
		else {
			System.out.println("No unique highest ancestor!!!");
		}
		FamilyTree tree = new FamilyTree(ancestor);
		tree.buildTree();
		tree.preorder();
	}
}
