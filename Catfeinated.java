package assignment3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Catfeinated implements Iterable<Cat> {
	public CatNode root;

	public Catfeinated() {
	}


	public Catfeinated(CatNode dNode) {
		this.root = dNode;
	}

	// Constructor that makes a shallow copy of a Catfeinated cafe
	// New CatNode objects, but same Cat objects
	public Catfeinated(Catfeinated cafe) {
		if (cafe == null || cafe.root == null) {
			this.root = null;
		} else {
			this.root = copySubtree(cafe.root, null);
		}
	}

	private static CatNode copySubtree(CatNode node, CatNode parent) {
		if (node == null) {
			return null;
		}
		CatNode copy = new CatNode(node.catEmployee);
		copy.parent = parent;
		copy.junior = copySubtree(node.junior, copy);
		copy.senior = copySubtree(node.senior, copy);
		return copy;
	}


	// add a cat to the cafe database
	public void hire(Cat c) {
		if (root == null) 
			root = new CatNode(c);
		else
			root = root.hire(c);
	}

	// removes a specific cat from the cafe database
	public void retire(Cat c) {
		if (root != null)
			root = root.retire(c);
	}

	// get the oldest hire in the cafe
	public Cat findMostSenior() {
		if (root == null)
			return null;

		return root.findMostSenior();
	}

	// get the newest hire in the cafe
	public Cat findMostJunior() {
		if (root == null)
			return null;

		return root.findMostJunior();
	}

	// returns a list of cats containing the top numOfCatsToHonor cats 
	// in the cafe with the thickest fur. Cats are sorted in descending 
	// order based on their fur thickness. 
	public ArrayList<Cat> buildHallOfFame(int numOfCatsToHonor) {
		ArrayList<Cat> result = new ArrayList<Cat>();

		if (numOfCatsToHonor <= 0 || this.root == null) {
			return result;
		}

		ArrayList<Cat> allCats = new ArrayList<Cat>();
		CatNode.collectAll(this.root, allCats);

		int k = numOfCatsToHonor;
		if (k > allCats.size()) {
			k = allCats.size();
		}


		for (int i = 0; i < k; i++) {
			int bestIndex = 0;
			for (int j = 1; j < allCats.size(); j++) {
				if (allCats.get(j).getFurThickness() >
						allCats.get(bestIndex).getFurThickness()) {
					bestIndex = j;
				}
			}
			result.add(allCats.get(bestIndex));
			allCats.remove(bestIndex);
		}

		return result;
	}

	// Returns the expected grooming cost the cafe has to incur in the next numDays days
	public double budgetGroomingExpenses(int numDays) {
		if (this.root == null || numDays < 0) {
			return 0.0;
		}

		ArrayList<Cat> allCats = new ArrayList<Cat>();
		CatNode.collectAll(this.root, allCats);

		double total = 0.0;
		for (int i = 0; i < allCats.size(); i++) {
			Cat c = allCats.get(i);
			if (c.getDaysToNextGrooming() <= numDays) {
				total += c.getExpectedGroomingCost();
			}
		}
		return total;
	}

	// returns a list of list of Cats. 
	// The cats in the list at index 0 need be groomed in the next week. 
	// The cats in the list at index i need to be groomed in i weeks. 
	// Cats in each sublist are listed in from most senior to most junior. 
	public ArrayList<ArrayList<Cat>> getGroomingSchedule() {
		ArrayList<ArrayList<Cat>> schedule = new ArrayList<ArrayList<Cat>>();

		if (this.root == null) {
			return schedule;
		}


		ArrayList<Cat> ordered = new ArrayList<Cat>();
		CatNode.collectInOrder(this.root, ordered);

		for (int i = 0; i < ordered.size(); i++) {
			Cat c = ordered.get(i);
			int week = c.getDaysToNextGrooming() / 7;

			while (schedule.size() <= week) {
				schedule.add(new ArrayList<Cat>());
			}
			schedule.get(week).add(c);
		}

		return schedule;
	}


	public Iterator<Cat> iterator() {
		return new CatfeinatedIterator();
	}


	public static class CatNode {
		public Cat catEmployee;
		public CatNode junior;
		public CatNode senior;
		public CatNode parent;

		public CatNode(Cat c) {
			this.catEmployee = c;
			this.junior = null;
			this.senior = null;
			this.parent = null;
		}


		private static int month(Cat c) {
			return c.getMonthHired();
		}

		private static int fur(Cat c) {
			return c.getFurThickness();
		}



		// add the c to the tree rooted at this and returns the root of the resulting tree
		public CatNode hire (Cat c) {

			CatNode inserted = insertBST(this, c);


			CatNode newRoot = heapifyUp(inserted);


			return newRoot;
		}

		private static CatNode insertBST(CatNode root, Cat c) {
			CatNode cur = root;
			int m = month(c);

			while (true) {
				int curMonth = month(cur.catEmployee);

				if (m > curMonth) {
					if (cur.junior == null) {
						CatNode newNode = new CatNode(c);
						newNode.parent = cur;
						cur.junior = newNode;
						return newNode;
					} else {
						cur = cur.junior;
					}
				} else {
					if (cur.senior == null) {
						CatNode newNode = new CatNode(c);
						newNode.parent = cur;
						cur.senior = newNode;
						return newNode;
					} else {
						cur = cur.senior;
					}
				}
			}
		}


		private static CatNode rotateRight(CatNode p) {
			if (p == null || p.junior == null) {
				return p;
			}

			CatNode child = p.junior;
			CatNode B = child.senior;
			CatNode gp = p.parent;


			child.parent = gp;
			if (gp != null) {
				if (gp.junior == p) {
					gp.junior = child;
				} else if (gp.senior == p) {
					gp.senior = child;
				}
			}


			child.senior = p;
			p.parent = child;

			p.junior = B;
			if (B != null) {
				B.parent = p;
			}

			return child;
		}

		private static CatNode rotateLeft(CatNode p) {
			if (p == null || p.senior == null) {
				return p;
			}

			CatNode child = p.senior;
			CatNode B = child.junior;
			CatNode gp = p.parent;

			child.parent = gp;
			if (gp != null) {
				if (gp.junior == p) {
					gp.junior = child;
				} else if (gp.senior == p) {
					gp.senior = child;
				}
			}

			child.junior = p;
			p.parent = child;

			p.senior = B;
			if (B != null) {
				B.parent = p;
			}

			return child;
		}

		private static CatNode heapifyUp(CatNode node) {
			CatNode cur = node;

			while (cur.parent != null &&
					fur(cur.catEmployee) > fur(cur.parent.catEmployee)) {

				if (cur.parent.junior == cur) {
					cur = rotateRight(cur.parent);
				} else {
					cur = rotateLeft(cur.parent);
				}
			}

			while (cur.parent != null) {
				cur = cur.parent;
			}
			return cur;
		}

		private static CatNode heapifyDown(CatNode node) {
			if (node == null) {
				return null;
			}

			CatNode cur = node;

			boolean done = false;
			while (!done) {
				CatNode best = cur;
				if (cur.junior != null &&
						fur(cur.junior.catEmployee) > fur(best.catEmployee)) {
					best = cur.junior;
				}
				if (cur.senior != null &&
						fur(cur.senior.catEmployee) > fur(best.catEmployee)) {
					best = cur.senior;
				}
				if (best == cur) {
					done = true;
				} else {
					if (best == cur.junior) {
						cur = rotateRight(cur);
					} else {
						cur = rotateLeft(cur);
					}
				}
			}

			while (cur.parent != null) {
				cur = cur.parent;
			}
			return cur;
		}

		private static CatNode findNode(CatNode root, Cat c) {
			if (root == null || c == null) {
				return null;
			}

			int target = month(c);
			CatNode cur = root;

			while (cur != null) {
				int curMonth = month(cur.catEmployee);

				if (target == curMonth) {
					if (cur.catEmployee.equals(c)) {
						return cur;
					} else {
						return null;
					}
				} else if (target > curMonth) {
					cur = cur.junior;
				} else {
					cur = cur.senior;
				}
			}
			return null;
		}

		// remove c from the tree rooted at this and returns the root of the resulting tree
		public CatNode retire(Cat c) {
			CatNode node = findNode(this, c);
			if (node == null) {

				CatNode r = this;
				while (r.parent != null) {
					r = r.parent;
				}
				return r;
			}
			return deleteNode(node);
		}

		private static CatNode deleteNode(CatNode node) {
			if (node == null) {
				return null;
			}


			if (node.junior != null && node.senior != null) {

				CatNode pred = node.junior;
				while (pred.senior != null) {
					pred = pred.senior;
				}
				node.catEmployee = pred.catEmployee;
				return deleteNode(pred);
			}

			CatNode parent = node.parent;
			CatNode child = (node.junior != null) ? node.junior : node.senior;

			if (child != null) {
				child.parent = parent;
			}

			if (parent == null) {

				if (child == null) {
					return null;
				}

				return heapifyDown(child);
			} else {
				if (parent.junior == node) {
					parent.junior = child;
				} else if (parent.senior == node) {
					parent.senior = child;
				}

				CatNode start = parent;
				CatNode newRoot = heapifyDown(start);
				return newRoot;
			}
		}

		// find the cat with highest seniority in the tree rooted at this
		public Cat findMostSenior() {
			CatNode cur = this;
			while (cur.senior != null) {
				cur = cur.senior;
			}
			return cur.catEmployee;
		}

		// find the cat with lowest seniority in the tree rooted at this
		public Cat findMostJunior() {
			CatNode cur = this;
			while (cur.junior != null) {
				cur = cur.junior;
			}
			return cur.catEmployee;
		}

		private static void collectAll(CatNode node, ArrayList<Cat> list) {
			if (node == null) {
				return;
			}
			list.add(node.catEmployee);
			collectAll(node.junior, list);
			collectAll(node.senior, list);
		}

		private static void collectInOrder(CatNode node, ArrayList<Cat> list) {
			if (node == null) {
				return;
			}
			collectInOrder(node.senior, list); // more senior (smaller month)
			list.add(node.catEmployee);
			collectInOrder(node.junior, list); // more junior
		}

		// Feel free to modify the toString() method if you'd like to see something else displayed.
		public String toString() {
			String result = this.catEmployee.toString() + "\n";
			if (this.junior != null) {
				result += "junior than " + this.catEmployee.toString() + " :\n";
				result += this.junior.toString();
			}
			if (this.senior != null) {
				result += "senior than " + this.catEmployee.toString() + " :\n";
				result += this.senior.toString();
			} /*
			if (this.parent != null) {
				result += "parent of " + this.catEmployee.toString() + " :\n";
				result += this.parent.catEmployee.toString() +"\n";
			}*/
			return result;
		}
	}

	public class CatfeinatedIterator implements Iterator<Cat> {
		// HERE YOU CAN ADD THE FIELDS YOU NEED
		private ArrayList<Cat> cats;
		private int index;

		public CatfeinatedIterator() {
			this.cats = new ArrayList<Cat>();
			this.index = 0;
			if (root != null) {
				CatNode.collectInOrder(root, this.cats);
			}
		}

		public Cat next(){
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Cat c = this.cats.get(this.index);
			this.index++;
			return c;
		}

		public boolean hasNext() {
			return this.index < this.cats.size();
		}
	}

	public static void main(String[] args) {
		Cat B = new Cat("Buttercup", 45, 53, 5, 85.0);
		Cat C = new Cat("Chessur", 8, 23, 2, 250.0);
		Cat J = new Cat("Jonesy", 0, 21, 12, 30.0);	
		Cat JJ = new Cat("JIJI", 156, 17, 1, 30.0);
		Cat JTO = new Cat("J. Thomas O'Malley", 21, 10, 9, 20.0);
		Cat MrB = new Cat("Mr. Bigglesworth", 71, 0, 31, 55.0);
		Cat MrsN = new Cat("Mrs. Norris", 100, 68, 15, 115.0);
		Cat T = new Cat("Toulouse", 180, 37, 14, 25.0);


		Cat BC = new Cat("Blofeld's cat", 6, 72, 18, 120.0);
		Cat L = new Cat("Lucifer", 10, 44, 20, 50.0);

	}


}


