package plusone;

public class Main 
{
	public static void main(String[] args) 
	{
		int[] digits = {9,8,7,6,5,4,3,2,1,0};
		plusOne(digits);
	}
	
	public static int[] plusOne(int[] digits) {
        int number = 0;
        int zeroes = digits.length-1;
        for(int i=0;i<digits.length;i++){
            number += digits[i] * Math.pow(10,zeroes);
            zeroes--;
        }
        System.out.println(number);
        String[] strNumbers = String.valueOf(number+1).split("");
        for(String str : strNumbers) {
        	System.out.println(str);
        }
        int[] export = new int[strNumbers.length];
        for(int i=0;i<export.length;i++){
            System.out.println(Integer.valueOf(strNumbers[i]));
            export[i] = Integer.valueOf(strNumbers[i]);
        }
        return export;
    }
}
