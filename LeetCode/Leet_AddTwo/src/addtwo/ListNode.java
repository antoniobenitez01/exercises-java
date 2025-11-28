package addtwo;

public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    @Override
    public String toString() {
    	return String.valueOf(sumListNode(this));
    }
	public static int sumListNode(ListNode l1) {
		int num = 0;
		int zeroes = 0;
		boolean hasNext = true;
		ListNode initial = l1;
		while(hasNext) {
			num += initial.val * Math.pow(10, zeroes);
			zeroes++;
			if(initial.next != null) {
				initial = initial.next;
			}else {
				hasNext = false;
			}
		}
		return num;
	}
}


