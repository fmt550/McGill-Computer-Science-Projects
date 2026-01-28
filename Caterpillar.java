package assignment2;

import java.awt.Color;
import java.util.Random;

import assignment2.food.*;


public class Caterpillar {
	// All the fields have been declared public for testing purposes
	public Segment head;
	public Segment tail;
	public int length;
	public EvolutionStage stage;

	public MyStack<Position> positionsPreviouslyOccupied;
	public int goal;
	public int turnsNeededToDigest;


	public static Random randNumGenerator = new Random(1);


	// Creates a Caterpillar with one Segment. It is up to students to decide how to implement this. 
	public Caterpillar(Position p, Color c, int goal) {
		Segment first = new Segment(p, c);

		this.head = first;
		this.tail = first;
		this.length = 1;

		this.stage = EvolutionStage.FEEDING_STAGE;
		this.goal = goal;

		this.positionsPreviouslyOccupied = new MyStack<Position>();
		this.turnsNeededToDigest = 0;
	}

	public EvolutionStage getEvolutionStage() {
		return this.stage;
	}

	public Position getHeadPosition() {
		return this.head.position;
	}

	public int getLength() {
		return this.length;
	}


	// returns the color of the segment in position p. Returns null if such segment does not exist
	public Color getSegmentColor(Position p) {
		Segment cur = this.head;

		while (cur != null) {
			if (cur.position.equals(p)) {
				return cur.color;
			}
			cur = cur.next;
		}

		return null;

	}
	
	
    // Methods that need to be added for the game to work
    public Color[] getColors(){
        Color[] cs = new Color[this.length];
        Segment chk = this.head;
        for (int i = 0; i < this.length; i++){
            cs[i] = chk.color;
            chk = chk.next;
        }
        return cs;
    }

    public Position[] getPositions(){
        Position[] ps = new Position[this.length];
        Segment chk = this.head;
        for (int i = 0; i < this.length; i++){
            ps[i] = chk.position;
            chk = chk.next;
        }
        return ps;
    }

    
	// shift all Segments to the previous Position while maintaining the old color
	// the length of the caterpillar is not affected by this
	public void move(Position p) {

		int energy = cake.getEnergyProvided();
		if (energy <= 0) {
			return;
		}


		this.stage = EvolutionStage.GROWING_STAGE;

		int grown = 0;

		while (grown < energy &&
				this.positionsPreviouslyOccupied != null &&
				!this.positionsPreviouslyOccupied.empty()) {

			Position growPos = this.positionsPreviouslyOccupied.pop();

			// grow only if that position is not already occupied
			if (this.getSegmentColor(growPos) == null) {


				Color newColor;
				if (this.tail != null) {
					newColor = this.tail.color;
				} else if (this.head != null) {
					newColor = this.head.color;
				} else {
					newColor = GameColors.GREEN;
				}

				Segment newSeg = new Segment(growPos, newColor);

				if (this.tail == null) {
					this.head = newSeg;
					this.tail = newSeg;
					this.length = 1;
				} else {
					this.tail.next = newSeg;
					this.tail = newSeg;
					this.length++;
				}

				grown++;


				if (this.length >= this.goal) {
					this.stage = EvolutionStage.BUTTERFLY;
					break;
				}
			}
		}


		if (this.stage != EvolutionStage.BUTTERFLY) {
			this.stage = EvolutionStage.FEEDING_STAGE;
		}
	}


		// a segment of the fruit's color is added at the end
	public void eat(Fruit f) {

		if (this.positionsPreviouslyOccupied == null || this.positionsPreviouslyOccupied.empty()) {
			return;
		}

		Position newPos = this.positionsPreviouslyOccupied.pop();
		Color newColor = f.getColor();

		Segment newSeg = new Segment(newPos, newColor);

		if (this.tail == null) {
			this.head = newSeg;
			this.tail = newSeg;
			this.length = 1;
		}
		else {
			this.tail.next = newSeg;
			this.tail = newSeg;
			this.length++;
		}

		if (this.length >= this.goal && this.stage != EvolutionStage.BUTTERFLY) {
			this.stage = EvolutionStage.BUTTERFLY;
		}
	}



	// the caterpillar moves one step backwards because of sourness
	public void eat(Pickle p) {
		if (this.head == null) {
			return;
		}

		if (this.positionsPreviouslyOccupied == null || this.positionsPreviouslyOccupied.empty()) {

			return;
		}


		Position stackPos = this.positionsPreviouslyOccupied.pop();

		Segment cur = this.head;


		if (cur.next == null) {
			cur.position = stackPos;
			return;
		}

		while (cur.next != null) {
			cur.position = cur.next.position;
			cur = cur.next;
		}

		cur.position = stackPos;
	}

		// all the caterpillar's colors shuffle around
	public void eat(Lollipop lolly) {

		if (this.length <= 1) return;

		Color[] colors = new Color[this.length];
		Segment cur = this.head;
		int i = 0;
		while (cur != null) {
			colors[i] = cur.color;
			cur = cur.next;
			i++;
		}

		for (i = colors.length - 1; i > 0; i--) {
			int j = randNumGenerator.nextInt(i + 1);
			Color tmp = colors[i];
			colors[i] = colors[j];
			colors[j] = tmp;
		}

		cur = this.head;
		i = 0;
		while (cur != null) {
			cur.color = colors[i];
			cur = cur.next;
			i++;
		}
	}

	// brain freeze!!
	// It reverses and its (new) head turns blue
	public void eat(IceCream gelato) {

		if (this.head == null) return;

		Segment prev = null;
		Segment cur = this.head;
		Segment next = null;

		while (cur != null) {
			next = cur.next;
			cur.next = prev;
			prev = cur;
			cur = next;
		}

		Segment oldHead = this.head;
		this.head = prev;
		this.tail = oldHead;

		this.head.color = GameColors.BLUE;

		this.positionsPreviouslyOccupied.clear();
	}

	// the caterpillar embodies a slide of Swiss cheese loosing half of its segments. 
	public void eat(SwissCheese cheese) {
		if (this.length <= 1) return;

		int keep = (this.length + 1) / 2;

		Segment cur = this.head;
		int i = 1;

		while (i < keep && cur != null) {
			cur = cur.next;
			i++;
		}

		Segment newTail = cur;
		Segment toDrop = (newTail == null) ? null : newTail.next;

		int dropCount = this.length - keep;
		Position[] dropped = new Position[dropCount];

		i = 0;
		while (toDrop != null && i < dropCount) {
			dropped[i] = toDrop.position;
			toDrop = toDrop.next;
			i++;
		}

		for (i = dropCount - 1; i >= 0; i--) {
			this.positionsPreviouslyOccupied.push(dropped[i]);
		}

		if (newTail != null) {
			newTail.next = null;
		}

		this.tail = newTail;
		this.length = keep;
	}


	
	public void eat(Cake cake) {

		int energy = cake.getEnergyProvided();
		if (energy <= 0) {
			return;
		}


		this.stage = EvolutionStage.GROWING_STAGE;

		int grown = 0;

		while (grown < energy &&
				this.positionsPreviouslyOccupied != null &&
				!this.positionsPreviouslyOccupied.empty()) {

			Position growPos = this.positionsPreviouslyOccupied.pop();


			if (this.getSegmentColor(growPos) == null) {


				Color newColor;
				if (this.tail != null) {
					newColor = this.tail.color;
				} else if (this.head != null) {
					newColor = this.head.color;
				} else {
					newColor = GameColors.GREEN;
				}

				Segment newSeg = new Segment(growPos, newColor);

				if (this.tail == null) {
					this.head = newSeg;
					this.tail = newSeg;
					this.length = 1;
				} else {
					this.tail.next = newSeg;
					this.tail = newSeg;
					this.length++;
				}

				grown++;

				if (this.length >= this.goal) {
					this.stage = EvolutionStage.BUTTERFLY;
					break;
				}
			}
		}


		if (this.stage != EvolutionStage.BUTTERFLY) {
			this.stage = EvolutionStage.FEEDING_STAGE;
		}
	}




	// This nested class was declared public for testing purposes
	public class Segment {
		private Position position;
		private Color color;
		private Segment next;

		public Segment(Position p, Color c) {
			this.position = p;
			this.color = c;
		}

	}


	public String toString() {
		Segment s = this.head;
		String snake = "";
		while (s!=null) {
			String coloredPosition = GameColors.colorToANSIColor(s.color) + 
					s.position.toString() + GameColors.colorToANSIColor(Color.WHITE);
			snake = coloredPosition + " " + snake;
			s = s.next;
		}
		return snake;
	}



	public static void main(String[] args) {
		Position startingPoint = new Position(3, 2);
		Caterpillar gus = new Caterpillar(startingPoint, GameColors.GREEN, 10);

		System.out.println("1) Gus: " + gus);
		System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);

		gus.move(new Position(3,1));
		gus.eat(new Fruit(GameColors.RED));
		gus.move(new Position(2,1));
		gus.move(new Position(1,1));
		gus.eat(new Fruit(GameColors.YELLOW));


		System.out.println("\n2) Gus: " + gus);
		System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);

		gus.move(new Position(1,2));
		gus.eat(new IceCream());

		System.out.println("\n3) Gus: " + gus);
		System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);

		gus.move(new Position(3,1));
		gus.move(new Position(3,2));
		gus.eat(new Fruit(GameColors.ORANGE));


		System.out.println("\n4) Gus: " + gus);
		System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);

		gus.move(new Position(2,2));
		gus.eat(new SwissCheese());

		System.out.println("\n5) Gus: " + gus);
		System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);

		gus.move(new Position(2, 3));
		gus.eat(new Cake(4));

		System.out.println("\n6) Gus: " + gus);
		System.out.println("Stack of previously occupied positions: " + gus.positionsPreviouslyOccupied);
	}
}