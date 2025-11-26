package duplicates;

import java.util.ArrayList;
import java.util.Arrays;

public class Main 
{
	public static void main(String[] args) 
	{
		int[] nums = {0,0,1,1,1,2,2,3,3,4}; // Input array
		int[] expectedNums = {0,1,2,3,4}; // The expected answer with correct length

		System.out.println(Arrays.toString(nums));
		int k = removeDuplicates(nums); // Calls your implementation
		System.out.println(Arrays.toString(nums));

		assert k == expectedNums.length;
		for (int i = 0; i < k; i++) {
		    assert nums[i] == expectedNums[i];
		}
		System.out.println(Arrays.toString(nums));
	}
	
	public static int removeDuplicates(int[] nums) 
    {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=0;i<nums.length;i++){
            if(!list.contains(nums[i])){
                list.add(nums[i]);
            }
        }
        nums = new int[nums.length];
        for(int i=0; i<nums.length;i++){
            if(i < list.size()){
                nums[i] = list.get(i);
            }
        }
        System.out.println(list);
        System.out.println(Arrays.toString(nums));
        System.out.println(list.size());
        return list.size();
    }
}
