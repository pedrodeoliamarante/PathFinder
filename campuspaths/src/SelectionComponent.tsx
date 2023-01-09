
import React, {Component} from 'react';
import {Building} from "./types";

// Internal state of SelectionComponent, used to store user input
// ↪ startingPointInput: stores the starting path point
// ↪ startingPointInput: stores the ending path point
// ↪ wayOfTransport    : stores the current way of transport
interface SelectionComponentState {
    startingPointInput : string;
    endingPointInput : string;
    wayOfTransport: string;
}

// SelectionComponent props
// ↪ buildingList: is a list of buildings, used to create the dropdown menu
// ↪ updatePoints: function, returns void, used as a callback
interface SelectionComponentProps {
    buildingList: Building[];
    updatePoints: (startingBuilding:string, endingBuilding:string, isHidden:boolean) => void;
    updateSpeed: (speed:string) => void;
}

// Component menus that allow user to interact with the map
class SelectionComponent extends Component<SelectionComponentProps, SelectionComponentState> {
    // constructor initialize local input to default state
    constructor(props:SelectionComponentProps) {
        super(props);
        this.state = {
            startingPointInput: "BAG",
            endingPointInput: "BAG",
            wayOfTransport: "walk",
        }
    }

    // draw function, uses callback function to pass back inputted buildings and update App
    draw = () => {
        this.props.updatePoints(this.state.startingPointInput, this.state.endingPointInput, false);
        this.props.updateSpeed(this.state.wayOfTransport);
    }
    // clear function, uses callback function to pass back inputted buildings and update App
    clear = () => {
        this.props.updatePoints("BAG", "BAG", true);
    }

    // renders the dropdown menu
    render() {
        // creates an array of JSX elements which are the option to be used in the dropdown
        let arr: JSX.Element[] = []
        for (let i=0; i < this.props.buildingList.length; i++) {
            let longName : string = this.props.buildingList[i].longName;
            let shortName : string = this.props.buildingList[i].shortName;
            let element : JSX.Element =
                <option value={shortName} key={"" + i}>{longName}</option>;
                arr.push(element);
        }

        return (
            <div>
                <h3 id="selection-component"> Start Location </h3>
                <div id="selection-component">
                    <select id={"selectList"} onChange={(event) => {
                        this.setState(
                            { startingPointInput: event.target.value}
                        )
                    }}>
                        {arr}
                    </select>
                </div>
                <h3 id="selection-component"> End Location </h3>
                <div id="selection-component">
                    <select id={"selectList"} onChange={(event) => {
                        this.setState(
                            { endingPointInput: event.target.value}
                        )
                    }}>
                        {arr}
                    </select>
                </div>
                <h3 id="selection-component"> Method of Transportation </h3>
                <div id="selection-component">
                    <select id={"selectList"} onChange={(event) => {
                        this.setState(
                            { wayOfTransport: event.target.value}
                        )
                    }}>
                        <option value="walk" key="0">Walk</option>;
                        <option value="bike" key="1">Bike</option>;
                        <option value="crawler" key="2">Crawler Transport</option>;
                    </select>
                </div>
                <div id="selection-component">
                    <button onClick={this.draw}>Draw Path</button>
                </div>
                <div id="selection-component">
                    <button onClick={this.clear}>Clear Path</button>
                </div>
            </div>
        );
    }
}

export default SelectionComponent;
