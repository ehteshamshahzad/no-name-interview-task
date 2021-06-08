## Country Holidays


### Execute
In order to execute this application on your machine, first you have to make sure Java is installed.
Then run the following commands.
* To clone the repository:
```BASH
git clone https://github.com/ehteshamshahzad/no-name-interview-task.git
```
* Navigate to `.jar` file:
```Bash
cd no-name-interview-task/build/libs
```
* Execute `.jar` file:
```BASH
java -jar ehtesham-0.0.1.jar
```

### API End Points

* `/` - GET Method: 

This endpoint will just return a string.

* `/countries` - GET Method:

Returns a list of all countries stored in database.

* `/country/{code}`- GET Method:

Passing country code will return information of a single country. You can test it out by passing `est`: `/country/est`.

* `/holidays/{code}/{year}` - GET Method:

Returns the holidays of a country in a given year. Holidays will be grouped by months.

* `/streak/{code}/{year}` - GET Method:

Returns holiday streak of a given country in a year.