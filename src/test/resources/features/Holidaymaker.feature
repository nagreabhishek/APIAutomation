Feature: Holidaymaker weather forecast
Description: I like to check the weather before embarking on a holiday

@holidaymaker
Scenario Outline: A happy holidaymaker
Given I like to holiday in "<city>"
And I only like to holiday on "<dayOfWeek>"
When I look up the weather forecast
Then I receive the weather forecast
And The temperature is warmer than <temperature> degrees

Examples:
| city	 | dayOfWeek | temperature |
| Sydney | Thursday  | 10          |
#Additional example, please uncomment the row to test
#| Mumbai | Friday 	 | 12				   | 