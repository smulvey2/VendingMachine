public class VendingMachine {
    private Item[] items; // store items in a min-heap
    private int itemCount; // number of items in this heap
    // Note use of min-heap here, to prioritize the smallest (soonest) expiration day.
    // You may decide to use either 0 or 1 as the top-index in this items array.

    public VendingMachine(int capacity) {
        items = new Item[capacity + 1];
        itemCount = 0;
    }

    private int getParent(int childIndex) {
        // return the parent index of the given child index
        return childIndex / 2;
    }

    private int getLeftChild(int parentIndex) {
        // return the left child index of the given parent index
        return parentIndex * 2;
    }

    private int getRightChild(int parentIndex) {
        // return the right child index of the given parent index
        return parentIndex * 2 + 1;
    }

    private void swap(int itemOneIndex, int itemTwoIndex) {
        // swaps the Item references at itemOneIndex and itemTwoIndex in the items array
        Item temp = items[itemOneIndex];
        items[itemOneIndex] = items[itemTwoIndex];
        items[itemTwoIndex] = temp;
    }

    private void addHelper(int index) {
        // Propagates the min-heap order property from the node at position index,
        // up through it's ancestor nodes. Assumes that only the node at position
        // index may be in violation of this property. This is useful when adding
        // a new item to the bottom of the heap.
        int i = index;
        if (items[getParent(i)] != null && items[i] != null) {
            if (i > 0 && items[getParent(i)].getExpirationDay() > items[i].getExpirationDay()) {
                swap(i, getParent(i));
                addHelper(getParent(i));
            }
        }
    }

    private void removeHelper(int index) {
        // Propagates the min-heap order property from the node at position index,
        // down through it's highest priority descendant nodes. Assumes that the
        // children of the node at position index conform to this heap property.
        // This is useful when removing an item from the top of the heap.
        int i = index;
        if (getLeftChild(i) < itemCount && getRightChild(i) < itemCount) {
            int smaller = i;
            int left = getLeftChild(i);
            int right = getRightChild(i);
            if (items[left] != null && items[right] != null) {
                if (items[left].getExpirationDay() >= items[right].getExpirationDay()
                 && items[left].getExpirationDay() >= items[i].getExpirationDay()) {
                    smaller = left;
                }
                if (items[right].getExpirationDay() >= items[left].getExpirationDay()
                 && items[right].getExpirationDay() >= items[i].getExpirationDay()) {
                    smaller = right;
                }
                swap(i, smaller);
                removeHelper(smaller);
            }
            if (items[left] == null && items[right] == null) {
                swap(i, smaller);
            }
            if (items[left] != null || items[right] != null) {
                if (items[left] != null) {
                    smaller = left;
                }
                if (items[right] != null) {
                    smaller = right;
                }
                swap(i, smaller);
                removeHelper(smaller);
            }
        }
    }

    public void addItem(Item item) {
        // Add the given item to the items array and perform the necessary
        // actions to maintain the min-heap properties.
        if (itemCount == items.length) {
            throw new IllegalStateException("WARNING: Item not added.  This vending machine is already filled to capacity.");
        }
        items[++itemCount] = item;
        int current = itemCount;
        addHelper(current);
         while (items[current].getExpirationDay() < items[getParent(current)].getExpirationDay())
         {
         swap(current, getParent(current));
         current = getParent(current);
         }
    }

    public Item dispenseNextItem() {
        // Dispense the item with the smallest expiration date from this
        // vending machine, while maintaining the min-heap properties.
        // This method removes the item returned from the heap.
        if (itemCount == 0) {
            throw new IllegalStateException("WARNING: Operation not allowed.  This vending machine is empty.");
        }
        Item popped = items[1];
        items[1] = items[itemCount--];
        removeHelper(1);
        return popped;
    }

    public Item getNextItem() {
        // This method returns a reference to the next item that will be dispensed.
        // This method does NOT change the state of the Vending Machine or its heap.
        if (itemCount == 0) {
            throw new IllegalStateException("WARNING: Operation not allowed.  This vending machine is empty.");
        }
        return items[1];
    }

    public Item dispenseItemAtIndex(int index) {
        // Dispense the item from a particular array index, while maintaining
        // the min-heap properties. This method removes that item from the heap.
        // This index parameter assumes the top-index is zero. So you'll need to
        // add one to this index, if you are using the top-index = 1 convention.
        if (itemCount == 0) {
            throw new IllegalStateException("WARNING: Operation not allowed.  This vending machine is empty.");
        }
        if (index >= itemCount || index != (int) index) {
            throw new IndexOutOfBoundsException("WARNING: Operation not allowed.  Index is invalid.");
        }
        int i = index + 1;
        Item popped = items[i];
        items[i] = items[itemCount--];
        removeHelper(i);
        return popped;
    }

    public Item getItemAtIndex(int index) {
        // This method returns a reference to the item at position index.
        // This method does not change the state of the vending machine.
        // This index parameter assumes the top-index is zero. So you'll need to
        // add one to this index, if you are using the top-index = 1 convention.
        if (itemCount == 0) {
            throw new IllegalStateException("WARNING: Operation not allowed.  This vending machine is empty.");
        }
        if (index >= itemCount || index != (int) index) {
            throw new IndexOutOfBoundsException("WARNING: Operation not allowed.  Index is invalid.");
        }
        int i = index + 1;
        return items[i];
    }
}