# preamble
this project started as a quick & simple technical assignment - i made the most minimal version of code which however met all given requirements.
but the code was not enough good (not of "Production Quality") - and it was eventually rejected :) ok, thought i, - now the challenge is really accepted ;)
so i decided to make this code a shiny sample of API polling in the Android app with all possible bells & whistles - just to have it and touch the limits of what could be done in scope of the task.

# status
this project is currently under active development and is far from being finished. however i'm trying to achieve complete functional & working state of the code in every commit.
the repo is and will stay public - so you are free to copy/fork the code and use it on your own. but if you find this project interesting/good/useful - please give it a star - i appreciate this.
also if you have any brilliant or just good ideas of how to make the solutions here even better - i'll welcome all merge-requests or messages from you, my dear guest.

# idea of the given task
Create a simple Android application written in Java or Kotlin that will monitor trucks approaching and leaving a location in Lausanne (CH).
The location is : latitude: 46.5223916, longitude: 6.6314437

# initial requirements:
* Display a list of all vehicles (the API to get those vehicles is described at the end of this file);
* For each vehicle item, display their vehicleId and if they are closer than one kilometre of flying distance from the target location or not (you can just wrote "close" and "far away" for that);
* Each vehicle in the list should have its location checked every 5 seconds (and their status updated in the list if necessary);
* These updates don't have to happen while the app is in background;
* On the top of the screen, the total number of vehicles being currently "close" to the target location should be displayed;
* Clicking on an item should open a vehicle details fragment with the id, live status and live location coordinates of the selected vehicle;
* The vehicle details view has also a counter of the number of vehicles being currently `close` to the target location displayed on top.

# initial advices:
* Make sure that your code is robust and follow some software engineer best practice;
* The app UI doesn't have to look pretty, it just have to be functional.

# given API specifications
initial hostname: "https://5w53f1x8oa.execute-api.eu-west-1.amazonaws.com/dev/".
the API key was provided separately (and of course kept in secret, i'm sorry but it's a good practice) - and must be specified with the HTTP header "x-api-key".
after a month i began to receive HTTP response code 403 - and i guess that the API key expired. so i decided to replace this data source with a number of stubs and other APIs.

# Endpoints:
1./ Vehicle lists: "GET {URL}/vehicles"
```
[
  {
    "vehicleId": String
  }
]
```
2./ Vehicle details: "GET {URL}/vehicles/:{vehicleId}"
```
{
  "vehicleId": String,
  "location": {
    latitude: Double,
    longitude: Double
  }
}
```
