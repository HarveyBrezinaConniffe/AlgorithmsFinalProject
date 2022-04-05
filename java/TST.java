public class TST {
	Node treeRoot;

	TST() {
		this.treeRoot = null;
	}

	public void insert(String key) {
		insert(key, this.treeRoot);
	}

	public void insert(String key, Node root) {
		char currentCharacter = key.charAt(0);	

		if(root == null) {
			root = new Node(currentCharacter);
		}

		if(currentCharacter == root.character) {
			if(key.length() == 1) {
				root.wordEnd = true;
				return;
			}
			else {
				insert(key.substring(1), root.eq);
				return;
			}
		}
		else if(currentCharacter < root.character) {
			insert(key, root.lo);
			return;
		}
		else if(currentCharacter > root.character) {
			insert(key, root.hi);
			return;
		}

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
