import java.util.ArrayList;

public class TST {
	Node treeRoot;

	TST() {
		this.treeRoot = null;
	}

	public void insert(String key) {
		this.treeRoot = insert(key, this.treeRoot);
	}

	public Node insert(String key, Node root) {
		char currentCharacter = key.charAt(0);	

		if(root == null) {
			root = new Node(currentCharacter);
		}

		if(currentCharacter == root.character) {
			if(key.length() == 1) {
				root.wordEnd = true;
			}
			else {
				root.eq = insert(key.substring(1), root.eq);
			}
		}
		else if(currentCharacter < root.character) {
			root.lo = insert(key, root.lo);
		}
		else if(currentCharacter > root.character) {
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

		// Prefix is not in tree.
		if(root == null) {
			return null;
		}

		// The current character is the same as the one in the current node
		if(currentCharacter == root.character) {
			// We have reached the end of the prefix so return the node directly after it.
			if(prefix.length() == 1) {
				return root.eq;
			}
			// We are not at the end of the prefix, Keep searching.
			else {
				return findEnd(prefix.substring(1), root.eq);
			}
		}
		else if(currentCharacter < root.character) {
			return findEnd(prefix, root.lo);
		}
		else {
			return findEnd(prefix, root.hi);
		}
	}
		
	// Returns all strings starting with prefix.
	public ArrayList<String> autocomplete(String prefix) {
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
