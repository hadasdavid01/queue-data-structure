public class CoronaQueue {

    Person[] data;
    int[] next;
    int[] prev;
    int[] ref;             // the reference array
    int size;             // the current number of subjects in the queue
    int head;             // the index of the lists 'head'. -1 if the list is empty.
    int tail;             // the index of the lists 'tail'. -1 if the list is empty.
    int free;             // the index of the first 'free' element.

    /**
     * Creates an empty data structure with the given capacity.
     * The capacity dictates how many different subjects may be put into the data structure.
     * Moreover, the capacity gives an upper bound to the ID number of a Person to be put in the data structure.
     */
    public CoronaQueue(int capacity) {
        this.data = new Person[capacity];
        this.next = new int[capacity];
        for (int i = 0; i < data.length - 1; i++) {
            this.next[i] = i + 1;
        }
        this.prev = new int[capacity];
        this.ref = new int[capacity + 1];
        for (int i = 0; i < ref.length; i++) {
            this.ref[i] = -1;
        }
        this.size = 0;
        this.head = -1;
        this.tail = -1;
        this.free = 0;

    }

    /**
     * Returns the size of the queue.
     *
     * @return the size of the queue
     */
    public int size() {
        return this.size;
    }

    /**
     * Inserts a given Person into the queue.
     * Inesertion should be done at the tail of the queue.
     * If the given person is already in the queue this function should do nothing.
     * Throws an illegal state exception if the queue is full.
     *
     * @param - the Task to be inserted.
     * @throws IllegalStateException - if queue is full.
     */
    public void enqueue(Person p) {
        if (this.free == -1) {
            throw new IllegalStateException("queue is full");
        }
        // Checks if the person is already on the queue
        if (this.ref[p.id] != -1) {
            return;
        }

        this.data[this.free] = p;
        this.ref[p.id] = this.free;

        // check if the queue is empty for update free
        if (this.head == -1) {
            this.head = this.free;
        }
        //update the prev and next
        if (this.tail != -1) {
            this.prev[this.free] = this.tail;
            this.next[this.tail] = this.free;
        } else {
            this.prev[this.free] = -1;
        }
        this.tail = this.free;
        this.size++;

        // Checks if the queue is full
        if (this.size == this.data.length) {
            this.free = -1;
        } else {
            this.free = this.next[this.free];
        }
    }


    /**
     * Removes and returns a Person from the queue.
     * The person removed is the one which sits at the head of the queue.
     * If the queue is empty returns null.
     */
    public Person dequeue() {
        // check if the queue is empty
        if (this.head == -1) {
            return null;
        }

        Person current = this.data[this.head];
        // delete the person from the data and update the reference array
        this.data[head] = null;
        this.ref[current.id] = -1;

        int nextIndex = this.next[this.head];

        // Change the head to be free
        this.next[this.head] = this.free;
        this.free = this.head;
        this.prev[this.head] = -1;
        this.size--;

        //update the new head. if the queue is empty update the tail else update the prev.
        if (this.size == 0) {
            this.head = -1;
            this.tail = -1;
        } else {
            this.head = nextIndex;
            this.prev[this.head] = -1;
        }
        return current;
    }

    /**
     * Removes a Person from (possibly) the middle of the queue.
     * <p>
     * Does nothing if the Person is not already in the queue.
     * Recall that you are not allowed to traverse all elements in the queue. In particular no loops or recursion.
     * Think about all the different edge cases and the variables which need to be updated.
     * Make sure you understand the role of the reference array for this function.
     *
     * @param p - the Person to remove
     */
    public void remove(Person p) {
        int personIndex = this.ref[p.id];

        //check if the person is in the queue
        if (personIndex == -1) {
            return;
        }
        //checks if the person is at the head of the queue and use dequeue function to remove
        if (personIndex == this.head) {
            this.dequeue();
            return;
        }

        this.next[this.prev[personIndex]] = this.next[personIndex];
        this.data[personIndex] = null;
        this.ref[p.id] = -1;
        this.size--;
        int prevNext = this.next[personIndex];
        this.next[personIndex] = this.free;
        this.free = personIndex;

        //checks if the person is at the tail or middle of the queue
        if (personIndex == this.tail) {
            this.tail = this.prev[personIndex];
        } else {
            this.prev[prevNext] = this.prev[personIndex];
        }

        this.prev[personIndex] = -1;
    }

    /*
     * The following functions may be used for debugging your code.
     */
    private void debugNext() {
        for (int i = 0; i < next.length; i++) {
            System.out.println(next[i]);
        }
        System.out.println();
    }

    private void debugPrev() {
        for (int i = 0; i < prev.length; i++) {
            System.out.println(prev[i]);
        }
        System.out.println();
    }

    private void debugData() {
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
        System.out.println();
    }

    private void debugRef() {
        for (int i = 0; i < ref.length; i++) {
            System.out.println(ref[i]);
        }
        System.out.println();
    }

    /*
     * Test code; output should be:
		Aaron, ID number: 1
		Baron, ID number: 2
		Cauron, ID number: 3
		Dareon, ID number: 4
		Aaron, ID number: 1
		Baron, ID number: 2
		Aaron, ID number: 1
		
		Baron, ID number: 2
		Cauron, ID number: 3
		
		Aaron, ID number: 1
		Dareon, ID number: 4
		
		Aaron, ID number: 1
		Cauron, ID number: 3
     */
    public static void main(String[] args) {
        CoronaQueue demo = new CoronaQueue(4);

        Person a = new Person(1, "Aaron");
        Person b = new Person(2, "Baron");
        Person c = new Person(3, "Cauron");
        Person d = new Person(4, "Dareon");

        demo.enqueue(a);
        demo.enqueue(b);
        demo.enqueue(c);
        demo.enqueue(d);
        System.out.println(demo.dequeue());
        System.out.println(demo.dequeue());
        demo.enqueue(a);
        System.out.println(demo.dequeue());
        demo.enqueue(b);
        System.out.println(demo.dequeue());
        System.out.println(demo.dequeue());
        System.out.println(demo.dequeue());
        demo.enqueue(a);
        System.out.println(demo.dequeue());

        System.out.println();
        demo.enqueue(a);
        demo.enqueue(b);
        demo.enqueue(c);
        demo.enqueue(d);

        demo.remove(a);
        System.out.println(demo.dequeue());
        demo.remove(d);
        System.out.println(demo.dequeue());

        System.out.println();
        demo.enqueue(a);
        demo.enqueue(b);
        demo.enqueue(c);
        demo.enqueue(d);
        demo.remove(b);
        demo.remove(c);
        System.out.println(demo.dequeue());
        System.out.println(demo.dequeue());

        System.out.println();
        demo.enqueue(a);
        demo.enqueue(b);
        demo.enqueue(c);
        demo.enqueue(d);
        demo.remove(b);
        demo.remove(d);
        System.out.println(demo.dequeue());
        System.out.println(demo.dequeue());
    }
}
