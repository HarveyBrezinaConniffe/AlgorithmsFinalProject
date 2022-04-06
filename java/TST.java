import java.util.ArrayList;

public class TST {
	Node treeRoot;

	TST() {
		this.treeRoot = null;
	}

	public void insert(String key) {
		System.out.println("Inserting "+key);
		this.treeRoot = insert(key, this.treeRoot);
	}

	public Node insert(String key, Node root) {
		char currentCharacter = key.charAt(0);	
		System.out.println("Current character is "+currentCharacter);

		if(root == null) {
			System.out.println("Root is null, Creating new node.");
			root = new Node(currentCharacter);
		}

		if(currentCharacter == root.character) {
			System.out.println("Character is equal to root.");
			if(key.length() == 1) {
				System.out.println("Only 1 character in key, Setting root to word end.");
				root.wordEnd = true;
			}
			else {
				System.out.println("More than 1 character in key, Moving on to next character.");
				root.eq = insert(key.substring(1), root.eq);
			}
		}
		else if(currentCharacter < root.character) {
			System.out.println("Character is less than root, Going left!");
			root.lo = insert(key, root.lo);
		}
		else if(currentCharacter > root.character) {
			System.out.println("Character is greater than root, Going right!");
			root.hi = insert(key, root.hi);
		}
		return root;

	}

	// Finds the node corresponding to the end of prefix
	public Node findEnd(String prefix) {
		return findEnd(prefix, this.treeRoot);	
	}

	public Node findEnd(String prefix, Node root) {
		char currentCharacter = prefix.charAt(0);	

		System.out.println("findEnd: On character: "+currentCharacter);

		// Prefix is not in tree.
		if(root == null) {
			System.out.println("findEnd: Hit null node, Ending search.");
			return null;
		}

		System.out.println("findEnd: Root is "+root.character);

		// The current character is the same as the one in the current node
		if(currentCharacter == root.character) {
			System.out.println("findEnd: Root matches node!");
			// We have reached the end of the prefix so return the node directly after it.
			if(prefix.length() == 1) {
				System.out.println("findEnd: Last character in key! Returning eq child.");
				return root.eq;
			}
			// We are not at the end of the prefix, Keep searching.
			else {
				System.out.println("findEnd: Not last character, Continuing search!");
				return findEnd(prefix.substring(1), root.eq);
			}
		}
		else if(currentCharacter < root.character) {
			System.out.println("findEnd: Character less than root, Going left!");
			return findEnd(prefix, root.lo);
		}
		else {
			System.out.println("findEnd: Character greater than root, Going right!");
			return findEnd(prefix, root.hi);
		}
	}
		
	// Returns all strings starting with prefix.
	public ArrayList<String> autocomplete(String prefix) {
		System.out.println("Finding node at end of prefix "+prefix);
		Node lastNode = findEnd(prefix);
		return autocomplete(lastNode, "");
	}

	// Returns all strings starting at node currentNode
	public ArrayList<String> autocomplete(Node currentNode, String prefix) {
		ArrayList<String> completions = new ArrayList<String>();

		if(currentNode == null) {
			return completions;
		}
		
		char currentCharacter = currentNode.character;

		if(currentNode.wordEnd) {
			completions.add(prefix+currentCharacter);
		}
		
		completions.addAll(autocomplete(currentNode.eq, prefix+currentCharacter));
		completions.addAll(autocomplete(currentNode.lo, prefix));
		completions.addAll(autocomplete(currentNode.hi, prefix));

		return completions;
			
	}

	private class Node {
		public char character;
		public Node lo, eq, hi;
		public boolean wordEnd;

		Node(char character) {
			this.character = character;	
			this.lo = null;
			this.eq = null;
			this.hi = null;
			this.wordEnd = false;
		}	
	}
}
