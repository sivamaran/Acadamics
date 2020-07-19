package project;

public class Polynomial {
	class Node {
		int key;
		double value;
		Node next;

		public Node(int k, double v) {
			key = k;
			value = v;
			next = null;
		}

	};

	private String expr;//Store the polynomial expression in string
	private char var;// Polynomial variable as char
	private Node[] map;// to map power with coefficient through hashing
	private Heap power;//storing the power in a minheap
	private int degree;//degree of the polynomial

	public Polynomial(char v, String e) {
		var = v;
		expr = e;
		map = new Node[10];
		degree = 0;
		insert(expr, var);
	}

	Polynomial() {
		map = new Node[10];
		power = new Heap(10);
		degree = 0;
		var = 'x';

	}

	private void insert(String s, char v) {
		s = s.replace("-", "+-");
		s = s.replace("x-", "xp1-");
		s = s.replace("x+", "xp1+");

		String token[] = s.split("\\+");//tokenizing the regex with '+' as delimiter
		power = new Heap(token.length);
		for (int i = 0; i < token.length; i++) {
			String cp[] = token[i].split(v + "p");//tokenizing the power and coefficient
			if (cp.length == 1) {
				insert(0, Double.parseDouble(cp[0]));
			} else {
				int k = Integer.parseInt(cp[1]);
				insert(k, Double.parseDouble(cp[0]));//insert the key-value in the map and populate heap 
				if (k > degree) {
					degree = k;
				}
			}

		}

	}

	public void insert(int key, double value) {
		if (key < 0)//neglet negative power
			return;
		if (value == 0)//neglet zeroed terms
			return;

		int loc = hash(key);//location of the map
		Node n = new Node(key, value);
		boolean flag = false;

		Node p = map[loc];
		if (map[loc] != null) {
			
			while (p.next != null || p.key == n.key) {
				if (p.key == n.key) {
					p.value += n.value;//adding the coef when  they power is same
					flag = true;
					break;
				}
				p = p.next;
			}

			if (flag == false) {
				power.insert(key);
				p.next = n;
				if (key > degree) {
					degree = key;
				}
			}

		} else {
			map[loc] = new Node(key, value);//create new node
			power.insert(key);
			if (key > degree) {
				degree = key;
			}
			return;
		}
	}

	private int hash(int n) {
		return (("" + n).hashCode()) % 10;
	}

	public double coef(int pow) {
		int loc = hash(pow);
        // traverse through the bucket
		for (Node temp = map[loc]; temp != null; temp = temp.next) {
			if (temp.key == pow) {
				return temp.value;
			}
		}
		return Double.NaN;
	}

	public void print() {
		for (int i = 0; i <= power.len(); i++) {
			System.out.println(power.data(i) + " " + coef(power.data(i)));
		}
	}

	public void set(int pow, double cof) {
		int loc = hash(pow);

		for (Node temp = map[loc]; temp != null; temp = temp.next) {
			if (temp.key == pow) {
				temp.value = cof;
				break;
			}
		}

	}

	public String toString() {
		String d = "";

		Heap temp = power.clone();
		while (temp.len() >= 0) {
			int k = temp.minExtract();
			double v = coef(k);

			if (k == 0) {
				d = d + v;
			} else if (k == 1) {
				if (v == 1.0)
					d = d + " + " + var;
				else
					d = d + " + " + v + var;
			} else {
				if (v == 1.0)
					d = d + " + " + var + "^" + (k);
				else
					d = d + " + " + v + var + "^" + (k);
			}

		}
		d = (d.replace("+ -", "-"));

		d = d.strip();

		if (Character.isDigit(d.charAt(0))) {
			return d;
		} else {
			return d.replaceFirst("\\+", "").strip();
			//return d.strip();
		}

	}

	public int getDegree() {
		return degree;
	}

	public int len() {
		return power.len();
	}

	

	public Polynomial mult(Polynomial that) {

		Polynomial p3 = new Polynomial();

		for (int i = 0; i <= this.len(); i++) {
			for (int j = 0; j <= that.len(); j++) {
				int temp1 = this.power.data(i);
				int temp2 = that.power.data(j);

				p3.insert(temp1 + temp2, this.coef(temp1) * that.coef(temp2));

			}

		}
		return p3;

	}
	public Polynomial mult(double k) {
		Polynomial that=new Polynomial();
		for (int i = 0; i <= power.len(); i++) {
			int key=power.data(i);
			double value=coef(key);
			that.insert(key,k*value);
		}
		return that;
	}

	public Polynomial add(Polynomial p1) {

		Heap temp = p1.power.clone();
		Polynomial p2 = this.clone();
		Polynomial p3 = new Polynomial();
		while (temp.len() >= 0) {
			int key = temp.minExtract();
			double cef = p1.coef(key);
			p2.insert(key, cef);
		}

		while (p2.len() >= 0) {
			int key = p2.power.minExtract();
			double cef = p2.coef(key);
			if (cef != 0)
				p3.insert(key, cef);
		}
		return p3;
	}

	public Polynomial sub(Polynomial p2) {
		Heap temp = p2.power.clone();
		Polynomial p1 = this.clone();
		Polynomial p3 = new Polynomial();
		while (temp.len() >= 0) {
			int key = temp.minExtract();
			double cef = p2.coef(key);
			cef = -cef;

			p1.insert(key, cef);

		}
		while (p1.len() >= 0) {
			int key = p1.power.minExtract();
			double cef = p1.coef(key);
			if (cef != 0)
				p3.insert(key, cef);
		}
		return p3;
	}

	public double root() {

		double x = 2.889;
		for (int i = 0; i <= 20; i++) {
			x = x - (compute(x) / (diffrentiate()).compute(x));
		}
		return x;

	}

	
	public double compute(double n) {
		double sum = 0;
		for (int i = 0; i <= len(); i++) {
			int k = power.data(i);
			sum = sum + coef(k) * Math.pow(n, k);
		}
		return sum;
	}

	public Polynomial diffrentiate() {
		Heap temp = power.clone();
		Polynomial d = new Polynomial();
		while (temp.len() >= 0) {
			int key = temp.minExtract(); // key
			double value = coef(key);
			if (key == 0) {
				continue;
			} else {
				d.insert(key - 1, key * value);
			}
		}
		return d;
	}

	public double integrate(double a, double b) {
		Heap temp = power.clone();
		Polynomial d = new Polynomial();
		while (temp.len() >= 0) {
			int key = temp.minExtract(); // key
			double value = coef(key);
			
			d.insert(key + 1, value / (key + 1));
			
		}
		return d.compute(b) - d.compute(a);
	}

	public Polynomial clone() {
		Heap temp = power.clone();
		Polynomial tp = new Polynomial();
		while (temp.len() >= 0) {
			int key = temp.minExtract();
			tp.insert(key, coef(key));

		}
		return tp;
	}
	
	public Polynomial pow(int n) {
		Polynomial that=new Polynomial();
		that.insert(0,1);
		for(int i=0;i<n;i++) {
			that=this.mult(that);
		}
		return that;
	}

	public static void main(String args[]) {

		Polynomial p = new Polynomial('x',"3xp4-5xp3+2x+7");
		Polynomial p1 = p.diffrentiate();
		System.out.println(p.toString());
		System.out.println(p1.toString());
		
		
		
	
	}
}
