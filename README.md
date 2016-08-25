#Solari Panel

This is a minimalistic example that shows how to use the TransitServer SDK (available here on GitHub) to query for realtime transit data.

The project is built combining a very simple [Play!](https://www.playframework.com/) controller, and the FogCreek [Solari Board](https://github.com/FogCreek/solari-board).
The board is re-purposed back to its original goal: display arrival and departure data for bus and train stations :)

An example displaying BUS arrival data for the Bolzano central station (waypoint id 1000)
![](http://i.imgur.com/oseIEvS.png)

Another one displaying TRAIN arrival data for the Bolzano central station (waypoint id 30299)
![](http://i.imgur.com/E2jyxcR.png)

#Getting started

##Obtrain access credentials

Please contact `developer-support` at `servizist.it` to request your access credentials.

Once you have them, copy `application.conf.example` to `application.conf`:

    ldematte:/projects/SolariPanel$ cp conf/application.conf.example conf/application.conf
    
Edit the newly created `conf/application.conf`. At the bottom, you will find these lines:

    transit_service.base_url="https://mocobus.sii.bz.it/transit-service"
    transit_service.username=<your_username>
    transit_service.password=<your_password>

Substitute `<your_username>` and `<your_password>` with your new credentials.

###Test

A test-only (fake) waypoint is available on the panel/on TransitServer. Just use "test" (`http://localhost:9000/departures/test` or `http://localhost:9000/arrivals/test`) as the waypoint ID.
This test request does not provide real data, but it does not need authentication either.

![](http://i.imgur.com/xuipd6f.png)

##Build

The project uses Play!, which in turn uses `sbt` for build and dependecies management.

Dependecies are automatically resolved; the only two libraries you need to handle yourself are the one we provide: [TransitServerSDK](https://github.com/servizist/TransitServerSDK/) and [RestClient](https://github.com/servizist/RestClient).

These projects are built with Maven. You just need to follow the usual steps:

    ldematte@client13-207:/projects/RestClient$ mvn install

Then you are ready to go!

    ldematte@client13-207:/projects/SolariPanel$ sbt
    [info] Loading project definition from /projects/SolariPanel/project
    [info] Updating {file:/projects/SolariPanel/project/}solaripanel-build...
    ...
    [info] Done updating.
    [info] Set current project to SolariPanel (in build file:/projects/SolariPanel/)
    [SolariPanel] $ 

In the SBT console:

    [SolariPanel] $ compile
    [SolariPanel] $ run
    
Open a browser and navigate to `localhost:9000` (for example: `http://localhost:9000/arrivals/1000`)

#Notes

Please note that this is just an example; in a real life product, results should be cached. Possibly, if there are multiple monitors displaying the same arrival or departure information, they should point to the same server; callint the TransitServer directly is possible, but not suggested (the server applies rate limiting, so you may run into the rate limitator if you exceed the default 60 requests per minute).

