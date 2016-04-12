### Ufo Sightings Android App README

This file discusses how to create the Ufo Sightings Android 
application.  This application's apk file is 52 Megabytes 
and therefore is too big to upload to Google Play.  Most of 
this README discusses the imperfect creation of the SQLite 
database from the data text file.  That data file was made 
by someone else, Ganglion, and is available on the 
Infochimps.com web site.  The Java source code used to 
create the app is not discussed much here.  

The script ufoTableMakerSqlite4.txt contains sqlite3 
commands that will run and will create the 
table, read the data in the data file, create 
a few indexes. 

Make it like this, all on one line:

```{bash}
cat  ufo_awesome.tsv |sed 's/^0000/00000000/'|
sed 's/\(....\)\(..\)\(..\)/\1\t\2\t\3/'|  
sed 's/^/INSERT INTO ufosightings (sighted_at_year, 
sighted_at_month, sighted_at_day, reported_at, location, 
shape, duration, description) VALUES (/'|
sed "s/$/');/"|sed "s/\t/','/g"|sed "s/','/,/"|sed "s/'//"|
sed "s/'//"|sed "s/'//"|sed "s/'//"|sed "s/'//" > ufo_awesome.sql
```
Or instead, like this to split the city and state out of location:
```{bash}
cat ufo_awesome.tsv |sed 's/^0000/00000000/'|
sed 's/\(....\)\(..\)\(..\)/\1\t\2\t\3/'|  
sed 's/^/INSERT INTO ufosightings (sighted_at_year, sighted_at_month, 
sighted_at_day, reported_at, location, shape, duration, description) 
VALUES (/'|sed "s/$/');/"|sed "s/\t/','/g"|sed "s/','/,/"|sed "s/'//"|
sed "s/'//"|sed "s/'//"|sed "s/'//"|sed "s/'//"|
sed "s/,' \([^,]*\), \([^']*\)','/,' \1','\2','/" > ufo_awesome.sql
```

There's alot of strangeness in there, like, for example, the 
"""SPACEcityname,statename""" always has a leading space and always 
has a comma.  Foreign countries have a location value that ENDS with a 
comma like: """(India),""", except for Canada which looks like 
"""(Canada), ON""" with trailing province code.  Also notice that some 
sighted_at values have """0000""" which sed extends to 8 characters so 
that the subsequent sed can split out the year month day for every record.  

The database can be created by running the code in the file 
named ufoTableMakerSqlite3.txt, shown here, with the just 
create database populator file we just created, named 
ufo_awesome.sql, in the current directory:
```{sql}
CREATE TABLE ufosightings (sighted_at_year integer default null, 
sighted_at_month integer default null, sighted_at_day integer default null,  
reported_at integer default null,  location_city text default null, 
location_state text default null, shape text default null,  
duration text default null,  description text default null); 

.read ufo_awesome.sql

CREATE INDEX idxsighted_at_year ON ufosightings(sighted_at_year);
CREATE INDEX idxsighted_at_month ON ufosightings(sighted_at_month);
CREATE INDEX idxsighted_at_day ON ufosightings(sighted_at_day);
CREATE INDEX idxlocation_city ON ufosightings(location_city);
CREATE INDEX idxlocation_state ON ufosightings(location_state);
CREATE INDEX idxdescription ON ufosightings(description);
```

The resulting sqlite3 database is suitable for 
use with an Android application.  

To turn the text files into an sqlite3 database, 
run this command with everything in the current 
directory:
```{bash}
sqlite3 ufos < ufoTableMakerSqlite3.txt
```
Lots of errors will stream by indicating a problem 
with that particular line in the text file.  Fix 
them or try again.  The number of records you have 
can be found like this:
```{bash}
sqlite3 ufos
> select count(*) from ufosightings;
```
And should match the number of lines in the 
database populator (SQL INSERTS, one per line) 
which you can find like this:
```{bash}
cat ufo_awesome.sql |wc
```
The resulting sqlite3 database, named ufos, is 
suitable for use in an Android application by 
placing the file in the "assets" directory of the 
Android application. 

Test out the database like this:
```
sqlite3 ufos
.help
.schema
select count(*) from ufosighting;
.quit
```
This png file looks good in android in res/drawable-mdpi
ba6e5cf41991fa7cc6e8c1ad391aea9a  ../alien.png

Unfortunately, the application file zoomed to 52 megabytes 
and cannot be uploaded to Google Play.  Efforts to shrink 
the size have been unsuccessful.  Bah!
