import java.io.File;
import java.util.Scanner;

public class FileParser {
    public static void main(String[] args){
        System.out.println("Input Filename");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.next();
        File results = new File(fileName);
        int total = 0;
        try( Scanner scan2 = new Scanner(results)){
            while(scan2.hasNext()){
                total+=scan2.nextInt();
            }
        } catch(Exception e){
            System.out.println(e);
        }
        System.out.println(total);

    }

}
