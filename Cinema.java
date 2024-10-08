import java.util.Scanner;
class Cinema
{
    static Scanner input=new Scanner(System.in);
    static String Films[]= {"Film X", "Film Y", "Film Z", "Film F"}; //Array available films
    static double filmPrices[]= {80, 70, 60, 90}; //Array of Price films
    static char Places[]=new char[] {'L','F','M','R','D'}; //Array of available Places
    static String[] snacks = {"Snack X", "Snack Y", "Snack Z", "Snack F","Snack P"};//Array of snaks
    static double[] snackPrices = {80, 70, 60, 90, 50};//Array of price snacks
    static int Agestore=0;
    static String namestore;
    static String phonestore;
    static int Choosefilmstore=0;
    static double totalSnackCost = 0;
    static char placestore;
    static String[] snacksstore= null;
    static double[] snacksstoreprice=null;
    
    //Clear Console
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Color Console
    public class ConsoleColor {
        // Reset
        public static final String RESET = "\033[0m";  // Reset to default color
    
        // Text colors
        public static final String BLACK = "\033[30m"; // BLACK
        public static final String RED = "\033[31m";   // RED
        public static final String GREEN = "\033[32m"; // GREEN
        public static final String YELLOW = "\033[33m"; // YELLOW
        public static final String BLUE = "\033[34m";  // BLUE
        public static final String MAGENTA = "\033[35m"; // MAGENTA
        public static final String CYAN = "\033[36m";   // CYAN
        public static final String WHITE = "\033[37m";  // WHITE
    
        // Background colors
        public static final String BLACK_BG = "\033[40m"; // BLACK
        public static final String RED_BG = "\033[41m";   // RED
        public static final String GREEN_BG = "\033[42m"; // GREEN
        public static final String YELLOW_BG = "\033[43m"; // YELLOW
        public static final String BLUE_BG = "\033[44m";  // BLUE
        public static final String MAGENTA_BG = "\033[45m"; // MAGENTA
        public static final String CYAN_BG = "\033[46m";   // CYAN
        public static final String WHITE_BG = "\033[47m";  // WHITE
    }

    //1- ask the user about his info and save it
    public static void TakeInfo()
    {
        System.out.print("Enter your name: ");
        String name=input.nextLine();
        System.out.print("\nEnter your phone number: ");
        String phone=input.nextLine();
        while(true)
        {
            if(phone.length()!=11)
            {
                System.out.print("\nPhone number must be exactly 12 digits. Please enter again: ");
                phone=input.nextLine();
            }
            else
            {
                break;
            }
        }
        System.out.print("\nEnter your age: ");
        int age=input.nextInt();
        Agestore=age;
        namestore=name;
        phonestore=phone;

    }

    //2- show the Details of films that are available today
    public static void ShowDetailsFilms()
    {
        System.out.println("\nThere are films available today: ");
        System.out.println("__________________________");
        for(int i=0; i<Films.length; i++)
        {
            System.out.println("| "+(i+1)+" | "+Films[i]+" | "+filmPrices[i]+" $ "+" | ");
            System.out.println("__________________________");
        }

    }

    //3- let the user chose one of them and save his choice
    public static void ChooseFilms()
    {
        System.out.print("\n(if your age less than 18 you can't choose 1 and 3!)");
        while(true)
        {
            System.out.print("\nEnter the Film you want please choose (1:4): ");
            int chooseFilm=input.nextInt();
            if(Agestore<18 && (chooseFilm==3||chooseFilm==1))
            {
                System.out.print("You can not choose 1 and 3. please choose another film");

            }
            else if(chooseFilm < 1 || chooseFilm > 4)
            {
                System.out.print("\nThis film does not exist. ");

            }
            else
            {
                Choosefilmstore=chooseFilm;
                break;
            }

        }

    }

    //4- ask the user about the place he wants to set in
    public static void ChoosePlaces()
    {
        System.out.print("\nPlease choose a place (L, F, M, R, D): ");
        boolean invalid=false;
        while(true)
        {
            char choosePlace=input.next().charAt(0);
            placestore=choosePlace;

            for(int i=0; i<Places.length; i++)
            {
                if(Places[i]==choosePlace)
                {
                    invalid=true;
                    break;
                }

            }
            if(!invalid)
            {
                System.out.print("\nInvalid choice! Please choose Again from: L, F, M, R, D: ");
            }
            else
            {
                break;
            }
        }


    }

    //5- if the user wants any extra snacks add it to his recept
    public static void ShowDetailsSnacks()
    {
        System.out.println("\nThere is snacks available today: ");
        System.out.println("__________________________");
        for(int i=0; i<Films.length; i++)
        {
            System.out.println("| "+(i+1)+" | "+snacks[i]+" | "+snackPrices[i]+" $ "+" | ");
            System.out.println("__________________________");
        }

    }
    //6-Choose the snakes
    public static void ChooseSnacks()
    {

        System.out.print("\nEnter the number of Sancks you want please choose (1:5): ");
        int NoOfSnacks=input.nextInt();


        snacksstore=new String[NoOfSnacks];
        snacksstoreprice = new double[NoOfSnacks];
        for(int i=0; i<NoOfSnacks; i++)
        {
            System.out.print("\nEnter the Sanck you want please choose (1:5): ");
            int chooseSnack=input.nextInt();

            while(chooseSnack < 1 || chooseSnack > 5)
            {
                System.out.print("\nThe snack does not exist.please Enter the snack you want please choose (1:5): ");
                chooseSnack=input.nextInt();
            }

            snacksstore[i] = snacks[chooseSnack - 1];
            snacksstoreprice[i]=snackPrices[chooseSnack-1];
            totalSnackCost += snackPrices[chooseSnack - 1];

        }


    }
    //7-check if you want snacks
    static public boolean IsWantSnake()
    {
        input.nextLine();// Consume the leftover newline character
        System.out.print("\nDo you want any snack? If yes, write YES; if not, write NO (Capital letters): ");
        String IsWant = input.nextLine();
        if(IsWant.equals("YES"))
            return true;
        else if(IsWant.equals("NO"))
            return false;
        else
        {
            while(true)
            {
                System.out.print("\nInvalid input. Please enter YES or NO (Capital letters): ");
                IsWant = input.nextLine();
                if(IsWant.equals("YES"))
                    return true;
                else if(IsWant.equals("NO"))
                    return false;
            }
        }

    }
    //8- caculate recipt
    public static double calculateTotal()
    {
        return totalSnackCost+filmPrices[Choosefilmstore-1];
    }

    //9- print the recept and the information that the user chose
    public static void Recipt() {
        // Thank the user for reserving
        System.out.print(ConsoleColor.MAGENTA + "\n                            Thanks " + namestore + " for reserving at the cinema. Your information is as follows: " + ConsoleColor.RESET + "\n\n");
        
        // Print header
        System.out.print(ConsoleColor.BLACK_BG + ConsoleColor.MAGENTA + "_____________________________________________________________________________________________________________________" + ConsoleColor.RESET + "\n\n");
        
        // Print user information
        System.out.print(ConsoleColor.BLUE + "Name: " + namestore + ConsoleColor.RESET + "\n\n");
        System.out.print(ConsoleColor.BLUE + "Phone number: " + phonestore + ConsoleColor.RESET + "\n\n");
        System.out.print(ConsoleColor.BLUE + "Age: " + Agestore + ConsoleColor.RESET + "\n\n");
        
        // Print film and place information
        System.out.print(ConsoleColor.BLACK_BG + ConsoleColor.MAGENTA + "______________________________________________________" + ConsoleColor.RESET + "\n\n");
        System.out.print(ConsoleColor.YELLOW + Films[Choosefilmstore - 1] + " : " + filmPrices[Choosefilmstore - 1] + "$" + ConsoleColor.RESET + "\n\n");
        System.out.print(ConsoleColor.YELLOW + "Place: " + placestore + ConsoleColor.RESET + "\n\n");
        System.out.print(ConsoleColor.BLACK_BG + ConsoleColor.MAGENTA + "______________________________________________________" + ConsoleColor.RESET + "\n\n");
        
        // Print snacks if available
        if (snacksstore != null)
        {
            System.out.print(ConsoleColor.GREEN + "Snacks: " + ConsoleColor.RESET + "\n\n");
            for (int i = 0; i < snacksstore.length; i++) 
            {
                boolean IsDuplicate = false;
                double Quantity=1;
                for (int j = 0; j < i; j++) 
                {
                    if(snacksstore[i].equals(snacksstore[j]))
                    {
                        Quantity++;
                        IsDuplicate=true;
                        break;
                    }

                }

                if(IsDuplicate)
                {
                    continue;
                }
                for (int k = i + 1; k < snacksstore.length; k++) 
                {
                    if (snacksstore[i].equals(snacksstore[k])) 
                    {
                        Quantity++;  
                    }
                }
                double totalprice=snacksstoreprice[i]*Quantity;
                System.out.print(ConsoleColor.CYAN + snacksstore[i] + "| units: " +(int)Quantity+"| Price: "+ totalprice + " $" + ConsoleColor.RESET + "\n\n");
            }
            System.out.print(ConsoleColor.BLACK_BG + ConsoleColor.MAGENTA + "______________________________________________________" + ConsoleColor.RESET + "\n\n");
        }
    
        // Print total reservation cost
        System.out.print(ConsoleColor.RED + "Your reservation cost: " + calculateTotal() + " $" + ConsoleColor.RESET + "\n\n");
    }
    
    public static void main(String[] args)
    {
        //1- ask the user about his info and save it (call)
        TakeInfo();
        clearConsole();
        //2- show the Details of films that are available today (call)
        ShowDetailsFilms();
        //3- let the user chose one of them and save his choice (call)
        ChooseFilms();
        clearConsole();
        //4- ask the user about the place he wants to set in (call)
        ChoosePlaces();
        clearConsole();
        //5- if the user wants any extra snacks add it to his recept (call)
        //6-Choose the snakes
        //7-check if you want snacks
        boolean IsTrue=IsWantSnake();
        if(IsTrue)
        {
            ShowDetailsSnacks();
            ChooseSnacks();
        }
        clearConsole();
        //8- print the recept and the information that the user chose
        clearConsole();
        Recipt();

    }
}
