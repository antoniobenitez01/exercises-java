package addtwo;

import java.util.ArrayList;

public class Main 
{
	public static void main(String[] args) 
	{
		ListNode l1 = new ListNode(2,new ListNode(4,new ListNode(3)));
		ListNode l2 = new ListNode(5,new ListNode(6,new ListNode(4)));
		System.out.println(addTwoNumbers(l1,l2));
	}
	
	public static ListNode addTwoNumbers(ListNode l1, ListNode l2) 
    {
		int num1 = sumListNode(l1);
		int num2 = sumListNode(l2);
		int newNum = num1 + num2;
		String[] stringNumbers = String.valueOf(newNum).split("");
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(int i=0;i<stringNumbers.length;i++) {
			if(stringNumbers[i].matches("[0-9]{1}")) {
				numbers.add(Integer.valueOf(stringNumbers[i]));
			}
		}
		numbers.removeLast();
		return createListNode(numbers);
    }
	
	public static ListNode createListNode(ArrayList<Integer> numbers) {
		ListNode current = new ListNode();
		ListNode created = new ListNode(numbers.getFirst(),current);
		for(Integer num : numbers) {
			if(numbers.indexOf(num) > 0) { 
				current.val = num;
				ListNode next = new ListNode();
				current.next = next;
				current = next;
			}
		}
		return created;
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
