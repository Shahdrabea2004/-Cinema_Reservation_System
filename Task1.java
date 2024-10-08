import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) 
    {

        Scanner input = new Scanner(System.in);
        
        /* 
        //First Code (V)
        int n1 = input.nextInt();
        char op = input.next().charAt(0);
        int n2 = input.nextInt();
        if ((n1 > n2 && op == '>') || (n1 < n2 && op == '<'))
            System.out.println("Right");
        else if (n1 == n2 && op == '=')
            System.out.println("Right");
        else
            System.out.println("Wrong");
        */
        
        /* 
        //Second Code (K)
        int n = input.nextInt();
        int min=n ,max=n;
        for(int i=0;i<2;i++)
        {
             n = input.nextInt();
            if(max<n)
            {
                max=n;
            }
            if(min>n)
            {
                min=n;
            }

        }
        System.out.println(min+" "+max);
        */
        
        /* 
        //Third Code (H)
        int n1 = input.nextInt();
        int n2 = input.nextInt();
        double result=(double)n1/n2;
        System.out.println("floor "+n1+" / "+n2+" = "+(int)Math.floor(result));
        System.out.println("ceil "+n1+" / "+n2+" = "+(int)Math.ceil(result));
        System.out.println("round "+n1+" / "+n2+" = "+Math.round(result));
        */
        
        /* 
        //Fourth Code (E)
        final double Pi= 3.141592653;
        double radiuas = input.nextDouble();
        System.out.println(Pi*radiuas*radiuas);
        */
 
        
        //Fiveth Code (C)
        long n1 = input.nextLong();
        long n2 = input.nextLong();
        System.out.println(n1+" + "+n2+" = "+ (n1+n2));
        System.out.println(n1+" * "+n2+" = "+ (n1*n2));
        System.out.println(n1+" - "+n2+" = "+ (n1-n2));
        

    }
}