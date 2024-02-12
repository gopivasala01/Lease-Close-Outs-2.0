package PDFDataExtract;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.PDFReader;

public class TestDataExtraction {
    public static String text;

  public static void extractIncreasedRentDetails(String text)  throws Exception
  
  {
    //public static void main(String[] args) throws Exception {
        //File file = new File("C:\\Users\\beerim\\Downloads\\Lease_1223_625_1171_Hayward_Loop_AL_Moore.pdf");
        //FileInputStream fis = new FileInputStream(file);
        //PDDocument document = PDDocument.load(fis);
        //text = new PDFTextStripper().getText(document);
        //text = text.replaceAll(System.lineSeparator(), " ");
        //text = text.trim().replaceAll(" +", " ");
        //System.out.println(text);
       // System.out.println("------------------------------------------------------------------");

        
        String monthlyRent = "";
        String increasedMonthlyRent = "";
        String rentSection = "";

        try {
            rentSection = text.substring(text.indexOf("Rent:"));

            if (!rentSection.contains("for a total of $")) 
            {
            	monthlyRent = rentSection.substring(StringUtils.ordinalIndexOf(rentSection, "Monthly Rent due in the amount of", 1)+"Monthly Rent due in the amount of".length()).trim().split(" ")[0];
                //monthlyRent = monthlyRentMatcher.group(1);
                System.out.println("Monthly Rent: " + monthlyRent);
                increasedMonthlyRent = rentSection.substring(StringUtils.ordinalIndexOf(rentSection, "Monthly Rent due in the amount of", 2)+"Monthly Rent due in the amount of".length()).trim().split(" ")[0];
                //increasedMonthlyRent = monthlyRentMatcher.group(1);
                System.out.println("Increased Monthly Rent: " + increasedMonthlyRent);
                if(increasedMonthlyRent.matches(".*[a-zA-Z]+.*")==true||increasedMonthlyRent.trim().equals("$"))
                {
                	increasedMonthlyRent = "Error";
                }
                /*
                // Use the pattern for this case
                //Pattern monthlyRentPattern = Pattern.compile("Monthly Rent due in the amount of \\$(\\d+\\.\\d+)");
               // Matcher monthlyRentMatcher = monthlyRentPattern.matcher(rentSection);

                // Find the first occurrence for monthlyRent
                //if (monthlyRentMatcher.find()) {
            	monthlyRent = rentSection.substring(StringUtils.ordinalIndexOf(text, "Monthly Rent due in the amount of", 1)+"Monthly Rent due in the amount of".length()).trim().split(" ")[0];
                    //monthlyRent = monthlyRentMatcher.group(1);
                    System.out.println("Monthly Rent: " + monthlyRent);

                    // Try to find the second occurrence for increasedMonthlyRent
                    //if (monthlyRentMatcher.find()) {
                    increasedMonthlyRent = rentSection.substring(StringUtils.ordinalIndexOf(text, "Monthly Rent due in the amount of", 2)+"Monthly Rent due in the amount of".length()).trim().split(" ")[0];
                        //increasedMonthlyRent = monthlyRentMatcher.group(1);
                        System.out.println("Increased Monthly Rent: " + increasedMonthlyRent);
                    } else {
                        System.out.println("Increased Monthly Rent: Not Found");
                    }
                }*/
            } else {
                // Use the existing logic
                int startIndex = rentSection.indexOf("for a total of $") + "for a total of $".length();
                int endIndex = rentSection.indexOf("for a total of $", startIndex);
                System.out.println(" ");
                monthlyRent = rentSection.substring(startIndex, endIndex).split(" ")[0].trim();
                System.out.println("Monthly Rent: " + monthlyRent);

                startIndex = rentSection.indexOf("for a total of $", endIndex) + "for a total of $".length();
                increasedMonthlyRent = rentSection.substring(startIndex).split(" ")[0].trim();
                System.out.println("Increased Monthly Rent: " + increasedMonthlyRent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Your logic for checking if monthlyRent and increasedMonthlyRent differ
        if (!monthlyRent.equals(increasedMonthlyRent)&&!increasedMonthlyRent.equals("")&&!increasedMonthlyRent.equals("Error")) {
            PDFReader.incrementRentFlag = true;
            PDFReader.increasedRent_amount = increasedMonthlyRent;
            
        

        // Extracting dates
        String monthlyRentEndDate = "";
        String increasedRentStartDate = "";

        try {
            if (rentSection.contains("Monthly Rent due in the amount of")) {
                // Format 1
                int startIndex = rentSection.indexOf("Month", rentSection.indexOf("to Month") + 1) + "Month".length();
                int endIndex = rentSection.indexOf("Monthly Rent due");

                monthlyRentEndDate = rentSection.substring(startIndex, endIndex).trim();

                // Adjust the extraction for Increased Rent Start Date
                startIndex = rentSection.indexOf("Month", endIndex) + "Month".length();
                endIndex = rentSection.indexOf("Monthly Rent due", startIndex);
                increasedRentStartDate = rentSection.substring(startIndex, endIndex).trim();
            } else if (rentSection.contains("Monthly Rent due in the amount of $")) {
                // Format 2
                int startIndex = rentSection.indexOf("Month", rentSection.indexOf("to Month") + 1) + "Month".length();
                int endIndex = rentSection.indexOf("Monthly Rent due");

                monthlyRentEndDate = rentSection.substring(startIndex, endIndex).trim();

                // Adjust the extraction for Increased Rent Start Date
                startIndex = rentSection.indexOf("Month", endIndex) + "Month".length();
                endIndex = rentSection.indexOf("Monthly Rent due", startIndex);
                increasedRentStartDate = rentSection.substring(startIndex, endIndex).trim();
            }

            // Additional processing if needed for date conversion
            // Extract only the date portion
            increasedRentStartDate = extractDateFromText(increasedRentStartDate);

            System.out.println("Monthly Rent End Date: " + monthlyRentEndDate);
            System.out.println("Increased Rent Start Date: " + increasedRentStartDate);
        } catch (Exception e) {
            monthlyRentEndDate = "Error";
            increasedRentStartDate = "Error";
            e.printStackTrace();
        }

        // Assign the extracted values to your variables or use them as needed
        PDFReader.increasedRent_previousRentEndDate = monthlyRentEndDate;
        PDFReader.increasedRent_newStartDate = increasedRentStartDate;
  }
    }

    // Function to extract date from text
    private static String extractDateFromText(String text) {
        Pattern datePattern = Pattern.compile("\\b(?:January|February|March|April|May|June|July|August|September|October|November|December) \\d{1,2}, \\d{4}\\b");
        Matcher dateMatcher = datePattern.matcher(text);

        if (dateMatcher.find()) {
            return dateMatcher.group();
        }

        return text;
    }
}
