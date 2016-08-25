#Solari Panel

This is a minimalistic example that shows how to use the TransitServer SDK (available here on GitHub) to query for realtime transit data.

The project is built combining a very simple [Play!](https://www.playframework.com/) controller, and the FogCreek [Solari Board](https://github.com/FogCreek/solari-board).
The board is re-purposed back to its original goal: display arrival and departure data for bus and train stations :)

An example displaying BUS arrival data for the Bolzano central station (waypoint id 1000)
![](http://i.imgur.com/oseIEvS.png)

Another one displaying TRAIN arrival data for the Bolzano central station (waypoint id 30299)
![](http://i.imgur.com/E2jyxcR.png)

#Notes

Please note that this is just an example; in a real life product, results should be cached. Possibly, if there are multiple monitors displaying the same arrival or departure information, they should point to the same server; callint the TransitServer directly is possible, but not suggested (the server applies rate limiting, so you may run into the rate limitator if you exceed the default 60 requests per minute).

#Access credentials

Please contact `developer_support` at `servizist.it` to request your access credentials.

##Test

A test-only (fake) waypoint is available on the panel/on TransitServer. Just use "test" (`http://localhost:9000/departures/test` or `http://localhost:9000/arrivals/test`) as the waypoint ID.
This test request does not provide real data, but it does not need authentication either.

