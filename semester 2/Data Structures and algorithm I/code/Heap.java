package project;

public class Heap {
	// Array the implements the CBT...	
			private int[] heap;
			// number of values in the array
			private int hSize;
			
			public Heap(int size) {
				heap = new int[size];
				hSize = -1;
			}
			
			// Parent node ...
			public int parent(int i) { return (i-1)/2; }
			// Left child ...
			public int left(int i) { return 2*i + 1; }
			// Right child...
			public int right(int i) {  return 2*i + 2; }
			// Data at node i ...
			public int data(int i) { return heap[i]; }
			
			public void swap(int a, int b) {
				int t = heap[a];
				heap[a] = heap[b];
				heap[b] = t;
			}
			
			public void insert(int e) {
				if(hSize==heap.length-1) {
					int [] temp=new int[2*(hSize+1)];
					for(int i=0;i<=hSize;i++) {
						temp[i]=heap[i];
					}
					heap=temp;
				}
				heap[++hSize] = e;
				
				int c = hSize;
				int p = parent(c);
				
				// Bottom up Heapification ...
				// Bubble up Heapify ...
				while(p >= 0 && c > 0) {
					if(heap[p] > heap[c])
						swap(p,c);
					c = p;
					p = parent(c);	
				}
			}

			public int minExtract() {
				int min = heap[0];
				
				heap[0] = heap[hSize--];
				minHeapify(0);
				
				return min;
			}

			private void minHeapify(int n) {
				int l = left(n);
				int r = right(n);
				
				int small = heap[n];
				int sIndex = n;
				
				// Checking the left child ...
				if(l<=hSize && small > heap[l]) {
					small = heap[l];
					sIndex = l;
				}
				// Checking the right child...
				if(r<=hSize && small > heap[r]) {
					small = heap[r];
					sIndex = r;
				}
				
				if(sIndex != n  && sIndex <= hSize) {
					swap(n,sIndex);
					minHeapify(sIndex);
				}

			}

			public boolean isEmpty() {
				return hSize==-1;
			}

			public void print() {
				System.out.println();
				for(int j =0; j<=hSize; j++) {
					System.out.print(heap[j] + " ");
				}
			}
			
			public void inorder(int root) {
				if(root > hSize) return;
				
				inorder(left(root));
				System.out.print(data(root) +" ");
				inorder(right(root));	
				
			}
			
			public void sort() {
				System.out.println();
				while (hSize >=0)
					System.out.print(minExtract()+ " ");
			}
			
			public int len() {
				return hSize;
			}
			
			public Heap clone() {
				Heap temp=new Heap(heap.length)	;
				for(int i=0;i<=hSize;i++) {
					temp.insert(heap[i]);
				}
				return temp;
			}
}
