import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EToFileReader {
    public static void main(String[] args) {
        String filePath = "/home/shatam-100/Down/WaterView_Data/FTP_DATA/VS_Code/scripts/ETo.asc";
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int ncolsCount = 0; // Variable to count occurrences of "ncols"
            int count = 0; 
            // Read through the file line by line
            while ((line = br.readLine()) != null) {
                // Check if the line contains the keyword "ncols"
                if (line.contains("ncols")) {
                    ncolsCount++; // Increment the count when "ncols" is found
                }
                count++;
            }
            System.out.println(count);

            // Output the count of occurrences of "ncols"
            System.out.println("Occurrences of 'ncols': " + ncolsCount);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
