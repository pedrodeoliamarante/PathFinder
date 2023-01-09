

import React, {Component} from 'react';
import Map from "./Map";
import {Building, PathSegment} from "./types";
import Information from "./Information"
// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import SelectionComponent from "./SelectionComponent";

// State of App
// ↪ buildingList: list of Building objects contained in a CampusPath object
// ↪ path        : current path to be showcased in the map
// ↪ startPoint  : current starting point selected, represents the starter building of the path
// ↪ endPoint    : current end point selected, represents the end building of the path
// ↪ isHidden    : determines if the information component is shown
// ↪ speed       : current speed of user
interface AppState {
    buildingsList : Building[];
    path: PathSegment[];
    distance: number;
    startPoint: string;
    endPoint: string;
    isHidden: boolean;
    speed: number;
}

class App extends Component<{}, AppState> {

    // Initializes the state of App.
    // everything is initialized to an empty state.
    constructor(props:any) {
        super(props);
        let emptyArrayB: Building[] = [];
        let emptyArrayC: PathSegment[] = [];
        let startPoint: string = "";
        let endPoint: string = "";
        let distance: number = 0;

        this.state = {
            buildingsList: emptyArrayB,
            path: emptyArrayC,
            endPoint: endPoint,
            startPoint: startPoint,
            distance: distance,
            isHidden: true,
            speed: 0,
        };
    }

    // Called when component is mounted to parse the buildings,
    // necessary to get the dropdown menu to work
    componentDidMount() {
        this.parseBuildings();
    }

    // Parses a list of buildings stored in the Spark server
    // Modifies the state to store the parsed list.
    // If the fetch request returns a not ok response run an alert and doesn't do anything
    // If it can't contact the server will also alert the user.
    parseBuildings = async () => {
        try {
            let response = await fetch("http://localhost:4567/campus-buildings");
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }
            let buildingList: Building[] = (await response.json()) as Building[];
            this.setState({
                buildingsList: buildingList,
            });
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    // Parses a path stored in the Spark Server based upon inputted strings.
    // Stores the Path in the state.
    // StartingBuilding is the build at the start, and endingBuilding the building at the end of the path.
    // If the fetch request returns a not ok response run an alert and doesn't do anything
    // If it can't contact the server will also alert the user.
    parsePath = async (startingBuilding:string, endingBuilding:string) => {
        try {
            let response = await fetch("http://localhost:4567/find-shortest-path?startName="
                + startingBuilding + "&endName=" + endingBuilding);
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }

            let path: PathSegment[] = (await response.json()).path as PathSegment[];
            this.setState({
                path: path,
            });
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    // Parses the cost of a path stored in the Spark Server based upon inputted strings.
    // Stores the cost in the state as distance.
    // StartingBuilding is the build at the start,
    //     and ending Building the building at the end of the path.
    // If the fetch request returns a not ok response run an alert and doesn't do anything
    // If it can't contact the server will also alert the user.
    parseCost = async (startingBuilding:string, endingBuilding:string) => {
        try {
            let response = await fetch("http://localhost:4567/find-shortest-path?startName="
                + startingBuilding + "&endName=" + endingBuilding);
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }

            let cost: number = (await response.json()).cost as number;
            cost = Math.round(cost);
            this.setState({
                distance: cost,
            });
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    // Function used by components to call parsePath to update the state
    updatePoints = (startingBuilding:string, endingBuilding:string, isHidden:boolean) => {
        this.parsePath(startingBuilding, endingBuilding);
        this.parseCost(startingBuilding, endingBuilding);
        this.toggleHidden(isHidden);
        let newState = {
            startPoint: startingBuilding,
            endPoint: endingBuilding,
        };
        this.setState(newState);
    }

    /// Function used by components to update the speed in state
    updateSpeed = (wayOfTransportation:string) => {
        let speed:number = 0;
        switch (wayOfTransportation) {
            case "walk":
                speed = 6;
                break;
            case "bike":
                speed = 30;
                break;
            case "crawler":
                speed = 2;
                break;
        }
        let newState = {
            speed: speed,
        }
        this.setState(newState);
    }

    // function that sets the isHidden value in state
    toggleHidden (isHidden:boolean) {
        this.setState({
            isHidden: isHidden,
        })
    }

    render() {
        return (
            <div>
                <h1 id="app-title">Campus Pathfinder</h1>
                <div>
                    <Map paths={this.state.path} />
                </div>
                <div>
                    <Information start={this.state.startPoint}
                                 end={this.state.endPoint} distance={this.state.distance}
                                 isHidden={this.state.isHidden} speed={this.state.speed}/>
                </div>
                <SelectionComponent buildingList={this.state.buildingsList} updatePoints={this.updatePoints}
                updateSpeed={this.updateSpeed}/>
            </div>
        );
    }
}

export default App;
