
package campuspaths;

import com.google.gson.Gson;
import campuspaths.utils.CORSFilter;
import pathfinder.CampusMap;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Spark;


public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.

        // initializes the CampusMap object
        CampusMap campusMap = new CampusMap();


        // Makes a get request to return a json object that contains the Path of the shortest-path
        // between two buildings
        Spark.get("/find-shortest-path", (request, response) -> {
            String startName = request.queryParams("startName");
            String endName = request.queryParams("endName");
            Path<Point> shortestPath = campusMap.findShortestPath(startName, endName);
            Gson gson = new Gson();
            return gson.toJson(shortestPath);
        });

        // Get function that makes a get request for a json of campus buildings information
        Spark.get("/campus-buildings", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(campusMap.listOfBuildings());
        });


    }

}
