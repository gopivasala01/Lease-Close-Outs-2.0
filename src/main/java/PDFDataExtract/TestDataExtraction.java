package PDFDataExtract;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.PDFReader;

public class TestDataExtraction 
{
public static String text;
	public static void extractIncreasedRentDetails(String text)  throws Exception
    //public static void main(String[] args)throws Exception
	{
		       // File file = new File("F:\\Lease_1223_625_1171_Hayward_Loop_AL_Moore.pdf");
				//FileInputStream fis = new FileInputStream(file);
				//PDDocument document = PDDocument.load(fis);
			    //text = new PDFTextStripper().getText(document);
			    //text = text.replaceAll(System.lineSeparator(), " ");
			    //text = text.trim().replaceAll(" +", " ");
			    //System.out.println(text);
			   // System.out.println("------------------------------------------------------------------");
		        String monthlyRent = "";
		        String rentSection = "";
			    try
			    {
			    rentSection = text.substring(text.indexOf("Rent:"));
			    monthlyRent = rentSection.substring(rentSection.indexOf("for a total of $", 1)+"for a total of $".length()).split(" ")[0].trim();
			    System.out.println(monthlyRent);
			    }
			    catch(Exception e)
			    {
			    	e.printStackTrace();
			    }
			    if(monthlyRent.endsWith("."))
			    	monthlyRent = monthlyRent.substring(0,monthlyRent.length()-1);
			    String increasedMonthlyRent = "";
			   try
			   {
			    increasedMonthlyRent = rentSection.substring(StringUtils.ordinalIndexOf(rentSection, "for a total of $", 2)+"for a total of $".length()).split(" ")[0].trim();
			    System.out.println(increasedMonthlyRent);
			   }
			   catch(Exception e)
			   {
				   
			   }
			    if(increasedMonthlyRent.endsWith("."))
			    	increasedMonthlyRent = increasedMonthlyRent.substring(0,increasedMonthlyRent.length()-1);
			    if(!monthlyRent.equals(increasedMonthlyRent))
			    {
			    	PDFReader.incrementRentFlag = true;
			    	PDFReader.increasedRent_amount = increasedMonthlyRent;
			    	//Previous rent end date
			    	String monthlyRentEndDate ="";
			    	try
			    	{
			    		 monthlyRentEndDate = rentSection.substring(StringUtils.ordinalIndexOf(rentSection, "to Month", 1)+"to Month".length(),rentSection.indexOf("Monthly Rent due")).trim();
						 System.out.println(monthlyRentEndDate);
			    	}
			    	catch(Exception e)
			    	{
			    		monthlyRentEndDate = "Error";
			    		e.printStackTrace();
			    	}
			    	PDFReader.increasedRent_previousRentEndDate = monthlyRentEndDate;
			    	//Increased rent start date
			    	String increasedRentStartDate ="";
			    	try
			    	{
			    		increasedRentStartDate = rentSection.substring(StringUtils.ordinalIndexOf(rentSection, "governmental taxes of", 1)+"governmental taxes of".length(),StringUtils.ordinalIndexOf(rentSection, "Monthly Rent due", 2)).trim();
			    		increasedRentStartDate=increasedRentStartDate.substring(StringUtils.ordinalIndexOf(increasedRentStartDate, "Month", 1)+"Month".length(), StringUtils.ordinalIndexOf(increasedRentStartDate, "to Month", 1)).trim();
						 System.out.println(increasedRentStartDate);
			    	}
			    	catch(Exception e)
			    	{
			    		increasedRentStartDate = "Error";
			    		e.printStackTrace();
			    	}
			    	PDFReader.increasedRent_newStartDate = increasedRentStartDate;
			    }

	}

}
