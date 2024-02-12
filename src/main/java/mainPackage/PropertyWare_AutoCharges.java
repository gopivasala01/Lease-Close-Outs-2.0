package mainPackage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PropertyWare_AutoCharges 
{
	public static boolean addingAutoCharges()
	{
	      try
	      {
			DataBase.getAutoCharges();
			RunnerClass.driver.navigate().refresh();
			//Pop up after clicking Lease Name
			PropertyWare.intermittentPopUp();
			RunnerClass.driver.findElement(Locators.summaryTab).click();
			RunnerClass.driver.findElement(Locators.summaryEditButton).click();
			Thread.sleep(2000);
			RunnerClass.js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.newAutoCharge)).build().perform();
			
			
			//List<WebElement> startDates = RunnerClass.driver.findElements(Locators.autoCharge_List_startDates);
			//List<WebElement> endDates = RunnerClass.driver.findElements(Locators.autoCharge_List_EndDates);
			for(int i=0;i<RunnerClass.autoCharges.length;i++)
			{
				boolean availabilityCheck = false;
				String chargeCode = RunnerClass.autoCharges[i][0];
				String amount = RunnerClass.autoCharges[i][1];
				String startDate = RunnerClass.autoCharges[i][2];
				String endDate = RunnerClass.autoCharges[i][3];
				String description = RunnerClass.autoCharges[i][4];
				
			// for a temporary patch until issue gets figured out of adding a Increased rent charge code with "$"
				if(chargeCode.equals("40010 - Rent Income")&&amount.equals("$"))
					continue;
				
				//If amount is Captive Insurance, Need to add decimal values to the amount as it is not coming along with it
				if(!amount.contains("."))
					amount = amount+".00";
				
				if(amount.trim().equals("Error")||amount.trim().equals("0.00")||amount==null||amount.trim().equals("")||amount.trim().matches(".*[a-zA-Z]+.*"))
				{
					System.out.println(" issue in adding Auto Charge - "+description);
					RunnerClass.failedReason = RunnerClass.failedReason+","+" Auto Charge - "+description;
					RunnerClass.statusID=1;
					continue;
				}
				
				try
				{
				List<WebElement> existingAutoCharges = RunnerClass.driver.findElements(Locators.autoCharge_List);
				List<WebElement> existingAutoChargeAmounts = RunnerClass.driver.findElements(Locators.autoCharge_List_Amounts);
				for(int k=0;k<existingAutoCharges.size();k++)
				{
					existingAutoCharges = RunnerClass.driver.findElements(Locators.autoCharge_List);
					existingAutoChargeAmounts = RunnerClass.driver.findElements(Locators.autoCharge_List_Amounts);
					//startDates = RunnerClass.driver.findElements(Locators.autoCharge_List_startDates);
					//endDates = RunnerClass.driver.findElements(Locators.autoCharge_List_EndDates);
					
					String autoChargeCodes = existingAutoCharges.get(k).getText();
					String autoChargeAmount = existingAutoChargeAmounts.get(k).getText();
					//String autoChargeStartDate = startDates.get(k).getText();
					//String autoChargeEndDate = endDates.get(k).getText();
					
					/*try
					{
						autoChargeAmount = autoChargeAmount.substring(1,autoChargeAmount.indexOf("."));
						amount = amount.substring(0,amount.indexOf("."));
					}
					catch(Exception e)
					{}*/ // Here add removed comment close
					if(chargeCode.contains(autoChargeCodes.replaceAll(".", ""))&&autoChargeAmount.replaceAll("[^0-9]", "").equals(amount.replaceAll("[^0-9]", ""))||amount.trim().equals("0.00"))//&&(startDate.equals(autoChargeStartDate)||autoChargeEndDate.trim().equals("")))
					{
						availabilityCheck = true;
						System.out.println(description+" already available");
						break;
					}
				}
				}
				catch(Exception e)
				{}
	      
				if(availabilityCheck==false)
				{
					PropertyWare_AutoCharges.addingAnAutoCharge(chargeCode, amount, startDate,endDate, description);
				}
				
			}
			 if(AppConfig.saveButtonOnAndOff==true)
					RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.saveLease)).click(RunnerClass.driver.findElement(Locators.saveLease)).build().perform();
				  else
					RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.cancelLease)).click(RunnerClass.driver.findElement(Locators.cancelLease)).build().perform();
	  Thread.sleep(2000);
			return true;
	      }
	      catch(Exception e)
	      {
	    	  e.printStackTrace();
			  RunnerClass.failedReason = RunnerClass.failedReason+","+"Something went wrong in adding auto charges";
			  System.out.println("Something went wrong in adding auto charges");
			  RunnerClass.driver.navigate().refresh();
			  return true;
	      }
			
		}
		
		
		
		public static boolean addingAnAutoCharge(String accountCode, String amount, String startDate,String endDate,String description) throws Exception
		{
			try
			{
			RunnerClass.driver.findElement(Locators.newAutoCharge).click();
			 try
			 {
		    //Charge Code
			Select autoChargesDropdown = new Select(RunnerClass.driver.findElement(Locators.accountDropdown_AutoCharge));
			autoChargesDropdown.selectByVisibleText(accountCode); //
			 }
			 catch(org.openqa.selenium.NoSuchElementException e)
			 {
				if( PropertyWare_AutoCharges.selectCharCodeUsingList(accountCode)==false)
				{
					e.printStackTrace();
					RunnerClass.statusID=1;
					System.out.println("Issue in adding Move in Charge"+description);
					RunnerClass.failedReason =  RunnerClass.failedReason+","+"Issue in adding Auto Charge - "+description;
					RunnerClass.driver.findElement(Locators.autoCharge_CancelButton).click();
					return false;	
				}
				 /*((JavascriptExecutor)RunnerClass.driver).executeScript("arguments[0].click();", RunnerClass.driver.findElement(Locators.accountDropdown));
				 accountCode = accountCode.split("-")[0].trim();
				 RunnerClass.driver.findElement(By.xpath("//*[@class='select2-search__field']")).sendKeys(accountCode);
				 RunnerClass.driver.findElement(By.xpath("//*[@class='select2-results']/ul/li/ul/li")).click();*/
			 }
			//Start Date
			RunnerClass.driver.findElement(Locators.autoCharge_StartDate).clear();
			Thread.sleep(500);
			RunnerClass.driver.findElement(Locators.autoCharge_StartDate).sendKeys(startDate);
						
			//click this to hide calendar UI
			RunnerClass.driver.findElement(Locators.autoCharge_refField).click();
			//Amount
			RunnerClass.driver.findElement(Locators.autoCharge_Amount).click();
			RunnerClass.actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
			RunnerClass.driver.findElement(Locators.autoCharge_Amount).sendKeys(amount);
			Thread.sleep(500);
						
			//End Date
			RunnerClass.driver.findElement(Locators.autoCharge_EndDate).clear();
			Thread.sleep(500);
			RunnerClass.driver.findElement(Locators.autoCharge_EndDate).sendKeys(endDate);
			
			//Description
			RunnerClass.driver.findElement(Locators.autoCharge_Description).sendKeys(description);
			
			//Save or Cancel
			if(AppConfig.saveButtonOnAndOff==false)
			RunnerClass.driver.findElement(Locators.autoCharge_CancelButton).click();
			else 
			RunnerClass.driver.findElement(Locators.autoCharge_SaveButton).click();
			Thread.sleep(2000);
			}
			catch(Exception e)
			{
				try
				{
				e.printStackTrace();
				RunnerClass.statusID=1;
				System.out.println("Issue in adding Move in Charge"+description);
				RunnerClass.failedReason =  RunnerClass.failedReason+","+"Issue in adding Auto Charge - "+description;
				RunnerClass.driver.findElement(Locators.autoCharge_CancelButton).click();
				return false;	
				}
				catch(Exception e2)
				{
					RunnerClass.driver.navigate().refresh();
				}
			}
			return true;
		}
		public static boolean selectCharCodeUsingList(String chargeCode)
		{
			RunnerClass.driver.findElement(By.xpath("//*[@id='editAutoChargeForm']/table[2]/tbody/tr[1]/td")).click();
			List<WebElement> chargeCodesList = RunnerClass.driver.findElements(By.xpath("//*[@id='editAutoChargeForm']/table[2]/tbody/tr[1]/td/select/optgroup/option"));
			for(int i=0;i<chargeCodesList.size();i++)
			{
				RunnerClass.actions.moveToElement(chargeCodesList.get(i)).build().perform();
				String charge = chargeCodesList.get(i).getText();
				if(charge.trim().equals(chargeCode.trim()))
				{
					chargeCodesList.get(i).click();
					return true;
				}
			}
			return false;
		}

}
