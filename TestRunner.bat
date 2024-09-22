echo mvn clean test -Dcucumber.filter.tags="%1" -DclientName="%2"
call mvn clean test -Dcucumber.filter.tags=%1 -DclientName=%2